package com.oracolo.findmycar.telegram.callbacks;

import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.context.ManagedExecutor;

import com.oracolo.findmycar.telegram.handler.enums.TelegramCommand;
import com.oracolo.findmycar.telegram.service.PositionService;
import com.oracolo.findmycar.telegram.service.TelegramService;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class PositionCallbackHandler implements TelegramCallbackHandler {

	@Inject
	PositionService positionService;
	@Inject
	TelegramService telegramService;
	@Inject
	ManagedExecutor executor;

	@Override
	public boolean canHandle(TelegramCommand telegramCommand) {
		return telegramCommand == TelegramCommand.POSITION;
	}

	@Override
	@Transactional
	public CompletableFuture<Void> handleCallback(JsonObject message) {
		JsonObject callbackQuery = message.getJsonObject("callback_query");
		JsonObject chat = callbackQuery.getJsonObject("message").getJsonObject("chat");
		Integer chatId = chat.getInteger("id");
		String data = message.getJsonObject("callback_query").getString("data");
		String callbackQueryId = message.getJsonObject("callback_query").getString("id");

		int vehicleId = Integer.parseInt(data.split("/")[2]);

		return CompletableFuture.supplyAsync(() -> positionService.getLastPosition(vehicleId),executor).thenCompose(
				position -> CompletableFuture.runAsync(() -> telegramService.sendPositionMessage(position.orElse(null), chatId),
						executor)).thenCompose(
				unused -> CompletableFuture.runAsync(() -> telegramService.answerCallback(callbackQueryId, true), executor));
	}
}
