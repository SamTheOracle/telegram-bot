package com.oracolo.findmycar.telegram.handler;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.entities.KeyChat;
import com.oracolo.findmycar.telegram.handler.enums.TelegramCommand;
import com.oracolo.findmycar.telegram.mqtt.message.TelegramUserMessage;
import com.oracolo.findmycar.telegram.service.KeyChatService;
import com.oracolo.findmycar.telegram.service.SyncService;
import com.oracolo.findmycar.telegram.service.TelegramService;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class StartCommandHandler implements TelegramCommandHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@ConfigProperty(name = "telegram.user.key-name")
	String uniqueKeyName;

	@Inject
	TelegramService telegramService;

	@Inject
	ManagedExecutor executor;

	@Inject
	SyncService syncService;

	@Inject
	KeyChatService keyChatService;

	@Override
	public boolean canHandle(TelegramCommand command) {
		return command == TelegramCommand.START;
	}

	@Override
	@Transactional
	public CompletableFuture<Void> handle(JsonObject message) {
		JsonObject chat = message.getJsonObject("message").getJsonObject("chat");
		Long chatId = chat.getLong("id");
		String name = Optional.ofNullable(chat.getString("first_name")).orElse("");
		String lastName = Optional.ofNullable(chat.getString("last_name")).orElse("");
		String fullName = name + " " + lastName;
		boolean isFullNameEmpty = fullName.replace(" ", "").isEmpty();
		String startMessage =
				"Benvenuto" + (isFullNameEmpty ? "" : " " + fullName) + "! Usa /position per ottenere la posizione della macchina";
		String text = message.getJsonObject("message").getString("text");
		Runnable sendMessage = () -> telegramService.sendMessage(startMessage, chatId);
		if (text.contains(uniqueKeyName)) {
			executor.submit(() -> handleMergeMessage(text,chatId));
		}
		return CompletableFuture.runAsync(sendMessage, executor);
	}

	private void handleMergeMessage(String startText,long chatId){
		try {
			String[] uniqueKeyArray = startText.split(uniqueKeyName);
			String uniqueKeyValue = uniqueKeyArray[1];
			Optional<KeyChat> keyChatOptional = keyChatService.getKeyChat(chatId);
			if (keyChatOptional.isPresent()) {
				KeyChat keyChat = keyChatOptional.get();
				keyChat.setUniqueKeyValue(uniqueKeyValue);
				logger.debug("Updating keychat {}",keyChat);
				keyChatService.update(keyChat);
			} else {
				KeyChat keyChat = new KeyChat();
				keyChat.setChatId(chatId);
				keyChat.setUniqueKeyValue(uniqueKeyValue);
				logger.debug("Creating new keychat entry {}",keyChat);
				keyChatService.insert(keyChat);
			}
			TelegramUserMessage telegramUserMessage = new TelegramUserMessage();
			telegramUserMessage.uniqueKeyValue = uniqueKeyValue;
			telegramUserMessage.chatId = chatId;
			syncService.sendMergeMessage(telegramUserMessage);
		} catch (Exception e) {
			logger.error("Error during merge op.", e);
		}
	}
}
