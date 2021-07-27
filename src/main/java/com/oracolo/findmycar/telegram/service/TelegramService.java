package com.oracolo.findmycar.telegram.service;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.client.TelegramHttpClient;
import com.oracolo.findmycar.telegram.converter.ResponseConverter;
import com.oracolo.findmycar.telegram.entities.Position;
import com.oracolo.findmycar.telegram.entities.selectmappings.PositionVehicle;
import com.oracolo.findmycar.telegram.utils.TelegramInlineKeyboard;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class TelegramService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	@RestClient
	TelegramHttpClient telegramHttpClient;

	@Inject
	ResponseConverter responseConverter;

	public JsonObject getUpdatesWithOffset(Long offset) throws IOException {
		Response updateResponse = telegramHttpClient.getUpdates(offset);
		if (updateResponse.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			JsonObject response = responseConverter.fromResponse(updateResponse);
			logger.debug("Received {}", response);
			return response;
		}
		logger.error("Could not make http request for updates");
		return new JsonObject();
	}

	public void sendMessage(String startMessage, Long chatId) {
		telegramHttpClient.sendMessageToChat(startMessage, chatId);
	}

	public void sendInlineKeyboardOfVehicles(List<PositionVehicle> positionVehicles, long chatId, String newText) {
		JsonArray inlineKeyboardArray = new JsonArray();
		List<List<PositionVehicle>> inlineKeyboard = TelegramInlineKeyboard.createInlineKeyboard(2, positionVehicles);
		inlineKeyboard.forEach(pair -> {
			JsonArray row = new JsonArray();
			pair.stream().map(vehicle -> new JsonObject().put("text", vehicle.getVehicleName()).put("callback_data",
					"/position/" + vehicle.getVehicleId())).forEach(row::add);
			inlineKeyboardArray.add(row);
		});
		JsonObject replyMarkup = new JsonObject().put("inline_keyboard", inlineKeyboardArray);
		telegramHttpClient.sendInlineKeyboard(newText, chatId, replyMarkup.encode());
	}

	public void sendPositionMessage(Position position, long chatId) {
		if (position == null) {
			String message = "Nessuna posizione trovata!";
			telegramHttpClient.sendMessageToChat(message, chatId);
			return;
		}
		logger.debug("Sending position {}",position);
		 telegramHttpClient.sendLocation(position.getLatitude(), position.getLongitude(), chatId);
	}
	public void answerCallback(String callbackQueryId, boolean showAlert){
		telegramHttpClient.answerCallback(callbackQueryId,showAlert);
	}
}
