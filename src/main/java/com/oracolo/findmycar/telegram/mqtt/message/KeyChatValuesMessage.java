package com.oracolo.findmycar.telegram.mqtt.message;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class KeyChatValuesMessage {
	public String replyId;
	public List<KeyChatPair> uniqueKeyValues;


}
