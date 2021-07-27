package com.oracolo.findmycar.telegram.mqtt.converter;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import com.oracolo.findmycar.telegram.entities.KeyChat;
import com.oracolo.findmycar.telegram.mqtt.message.KeyChatPair;
import com.oracolo.findmycar.telegram.mqtt.message.KeyChatValuesMessage;

@ApplicationScoped
public class SyncConverter {

	public KeyChatValuesMessage to(List<KeyChat> keyChats, String replyId) {
		KeyChatValuesMessage keyChatValuesMessage = new KeyChatValuesMessage();
		keyChatValuesMessage.replyId = replyId;
		keyChatValuesMessage.uniqueKeyValues = keyChats.stream().map(this::to).collect(Collectors.toList());
		return keyChatValuesMessage;
	}

	private KeyChatPair to(KeyChat keyChat) {
		KeyChatPair keyChatPair = new KeyChatPair();
		keyChatPair.chatId = keyChat.getChatId();
		keyChatPair.uniqueKeyValue = keyChat.getUniqueKeyValue();
		return keyChatPair;
	}
}
