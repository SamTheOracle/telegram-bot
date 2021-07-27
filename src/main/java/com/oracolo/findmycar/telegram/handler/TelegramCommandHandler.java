package com.oracolo.findmycar.telegram.handler;

import java.util.concurrent.CompletableFuture;

import com.oracolo.findmycar.telegram.handler.enums.TelegramCommand;

import io.vertx.core.json.JsonObject;

public interface TelegramCommandHandler {
	boolean canHandle(TelegramCommand command);
	CompletableFuture<Void> handle(JsonObject message);
}
