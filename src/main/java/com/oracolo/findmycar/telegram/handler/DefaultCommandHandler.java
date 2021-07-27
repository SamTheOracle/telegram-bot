package com.oracolo.findmycar.telegram.handler;

import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.handler.enums.TelegramCommand;
import com.oracolo.findmycar.telegram.service.PositionService;
import com.oracolo.findmycar.telegram.service.TelegramService;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class DefaultCommandHandler implements TelegramCommandHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private static final String DEFAULT_MESSAGE = "Non ho capito quello che hai detto. Prova ad utilizzare i comandi:\n1) /position\n2) /start";

	@Inject
	PositionService positionService;

	@Inject
	TelegramService telegramService;

	@Inject
	ManagedExecutor executor;

	@Override
	public boolean canHandle(TelegramCommand command) {
		return command == TelegramCommand.POSITION;
	}

	@Override
	public CompletableFuture<Void> handle(JsonObject message) {
		JsonObject chat = message.getJsonObject("message").getJsonObject("chat");
		Long chatId = chat.getLong("id");

		return CompletableFuture.runAsync(() -> telegramService.sendMessage(DEFAULT_MESSAGE, chatId),executor);
	}

}
