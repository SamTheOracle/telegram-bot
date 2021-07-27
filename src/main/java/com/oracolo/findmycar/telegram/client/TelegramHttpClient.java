package com.oracolo.findmycar.telegram.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface TelegramHttpClient {

	@GET
	@Path("/getUpdates")
	Response getUpdates(@QueryParam("offset") Long offset);

	@GET
	@Path("/sendMessage")
	Response sendMessageToChat(@QueryParam("text") String text,@QueryParam("chat_id") long chatId);

	@GET
	@Path("/sendMessage")
	Response sendInlineKeyboard(@QueryParam("text") String text, @QueryParam("chat_id") long chatId,@QueryParam("reply_markup") String replyMarkup);

	@GET
	@Path("/sendLocation")
	Response sendLocation(@QueryParam("latitude") String latitude, @QueryParam("longitude") String longitude, @QueryParam("chat_id") long chatId);

	@GET
	@Path("/answerCallbackQuery")
	Response answerCallback(@QueryParam("callback_query_id") String callbackQueryId, @QueryParam("show_alert") boolean showAlert);
}
