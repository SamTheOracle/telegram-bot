package com.oracolo.findmycar.telegram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.callbacks.PositionCallbackHandler;
import com.oracolo.findmycar.telegram.callbacks.TelegramCallbackHandler;
import com.oracolo.findmycar.telegram.handler.DefaultCommandHandler;
import com.oracolo.findmycar.telegram.handler.PositionCommandHandler;
import com.oracolo.findmycar.telegram.handler.StartCommandHandler;
import com.oracolo.findmycar.telegram.handler.TelegramCommandHandler;
import com.oracolo.findmycar.telegram.handler.enums.TelegramCommand;
import com.oracolo.findmycar.telegram.service.TelegramService;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.Startup;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
@Startup
@UnlessBuildProfile(value = "test")
public class TelegramClient {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	TelegramService telegramService;

	@Inject
	StartCommandHandler startCommandHandler;

	@Inject
	PositionCommandHandler positionCommandHandler;

	@Inject
	PositionCallbackHandler positionCallbackHandler;

	@Inject
	DefaultCommandHandler defaultCommandHandler;

	@Inject
	ManagedExecutor executor;

	private final List<TelegramCommandHandler> handlers = new ArrayList<>();
	private final List<TelegramCallbackHandler> callbackHandlers = new ArrayList<>();

	@PostConstruct
	void init() {
		logger.info("Telegram client started!");
		handlers.add(startCommandHandler);
		handlers.add(positionCommandHandler);

		callbackHandlers.add(positionCallbackHandler);
		longPolling(0);
	}

	private void longPolling(long offset) {
		try {
			JsonObject telegramResponse = telegramService.getUpdatesWithOffset(offset);
			JsonArray results = telegramResponse.getJsonArray("result");
			if (!results.isEmpty()) {
				List<CompletableFuture<Void>> parallelTasks = handleResults(results);
				//add default task in case the list is empty
				parallelTasks.add(CompletableFuture.runAsync(() -> logger.trace("Default empty task"), executor));
				CompletableFuture.allOf(parallelTasks.toArray(new CompletableFuture[0])).whenComplete(((unused, throwable) -> {
					if (throwable != null) {
						logger.error("Error executing tasks.", throwable);
					}
					long nextOffset = results.getJsonObject(results.size() - 1).getLong("update_id") + 1;
					longPolling(nextOffset);

				}));
			} else {
				longPolling(offset);
			}
		} catch (IOException e) {
			logger.error("Exception when getting updates.", e);
			longPolling(offset);
		}

	}

	private List<CompletableFuture<Void>> handleResults(JsonArray results) {
		List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
		results.stream().map(o -> (JsonObject) o).forEach(jsonMessage -> {
			if (jsonMessage.getJsonObject("message") != null) {
				completableFutures.add(handleCommand(jsonMessage));
			} else {
				completableFutures.add(handleCallback(jsonMessage));
			}
		});
		return completableFutures;
	}

	private CompletableFuture<Void> handleCallback(JsonObject jsonMessage) {
		String data = jsonMessage.getJsonObject("callback_query").getString("data");
		Optional<TelegramCommand> commandOptional = TelegramCommand.from(data.split("/")[1].replace("/", "").toUpperCase());
		if(commandOptional.isEmpty()){
			return CompletableFuture.failedFuture(new Exception("Error, callback query not implemented"));
		}
		TelegramCommand telegramCommand = commandOptional.get();
		Optional<TelegramCallbackHandler> callbackHandlerOptional = callbackHandlers.stream().filter(callback->callback.canHandle(telegramCommand)).findFirst();
		if(callbackHandlerOptional.isEmpty()){
			return CompletableFuture.failedFuture(new Exception("Error, no callback handler for command "+telegramCommand));
		}
		TelegramCallbackHandler telegramCallbackHandler = callbackHandlerOptional.get();
		return telegramCallbackHandler.handleCallback(jsonMessage);
	}

	private CompletableFuture<Void> handleCommand(JsonObject message) {
		String text = message.getJsonObject("message").getString("text");
		JsonObject contact = message.getJsonObject("message").getJsonObject("contact");
		if (contact != null)
			return CompletableFuture.failedFuture(new Exception("could not find handler for contact"));
		if (text == null) {
			return defaultCommandHandler.handle(message);
		}
		if(text.contains("/start")){
			return startCommandHandler.handle(message);
		}

		Optional<TelegramCommand> commandOptional = TelegramCommand.from(text.replace("/", "").toUpperCase());
		if (commandOptional.isPresent())
			return handlers.stream().filter(handler -> handler.canHandle(commandOptional.get())).findFirst().orElseThrow().handle(message);

		return defaultCommandHandler.handle(message);
	}

}
