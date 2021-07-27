package com.oracolo.findmycar.telegram.mqtt.message;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class KeyChatPair{
	public String uniqueKeyValue;
	public Long chatId;

	@Override
	public String toString() {
		return "KeyChatPair{" + "uniqueKeyValue='" + uniqueKeyValue + '\'' + ", chatId=" + chatId + '}';
	}
}