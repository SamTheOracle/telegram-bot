package com.oracolo.findmycar.telegram.handler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.entities.selectmappings.PositionVehicle;
import com.oracolo.findmycar.telegram.handler.enums.TelegramCommand;
import com.oracolo.findmycar.telegram.service.PositionService;
import com.oracolo.findmycar.telegram.service.TelegramService;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class PositionCommandHandler implements TelegramCommandHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

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
		Integer chatId = chat.getInteger("id");

		return CompletableFuture.supplyAsync(() -> positionService.getPositionVehiclesByChatId(chatId), executor).thenCompose(
				positionVehicles -> sendInlineKeyboard(positionVehicles, chatId));
	}

	private CompletableFuture<Void> sendInlineKeyboard(List<PositionVehicle> positionVehicles, long chatId) {
		long numberOfPosition = positionService.getPositionCount(chatId);
		if (numberOfPosition == 0) {
			return CompletableFuture.runAsync(() -> telegramService.sendMessage("Nessuna posizione registrata ancora!", chatId), executor);
		}
		String newText = "Seleziona uno dei tuoi veicoli con l'ultima posizione registrata";
		return CompletableFuture.runAsync(() -> telegramService.sendInlineKeyboardOfVehicles(positionVehicles, chatId, newText), executor);
	}
}
