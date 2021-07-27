package com.oracolo.findmycar.telegram.callbacks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.oracolo.findmycar.telegram.handler.enums.TelegramCommand;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface TelegramCallbackHandler {

  boolean canHandle(TelegramCommand telegramCommand);
  CompletableFuture<Void> handleCallback(JsonObject message);
}
