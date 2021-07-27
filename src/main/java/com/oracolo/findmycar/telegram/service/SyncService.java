package com.oracolo.findmycar.telegram.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.entities.KeyChat;
import com.oracolo.findmycar.telegram.mqtt.converter.SyncConverter;
import com.oracolo.findmycar.telegram.mqtt.message.KeyChatValuesMessage;
import com.oracolo.findmycar.telegram.mqtt.message.RetrySyncMessage;
import com.oracolo.findmycar.telegram.mqtt.message.TelegramUserMessage;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@RegisterForReflection
public class SyncService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	KeyChatService keyChatService;
	@Inject
	MqttClientService mqttClientService;
	@Inject
	SyncConverter syncConverter;

	void onRetryMessage(@Observes RetrySyncMessage retrySyncMessage) {
		List<KeyChat> keyChats = keyChatService.getByUniqueKeyValues(retrySyncMessage.uniqueKeys);
		KeyChatValuesMessage keyChatValuesMessage = syncConverter.to(keyChats, retrySyncMessage.messageId);
		logger.debug("Sending reply for sync message {}",keyChatValuesMessage);
		mqttClientService.sendKeyChatValuesMessage(keyChatValuesMessage);
	}

	public void sendMergeMessage(TelegramUserMessage telegramUserMessage) {
		logger.debug("Sending new user message {}",telegramUserMessage);
		mqttClientService.sendTelegramUserMessage(telegramUserMessage);
	}

}
