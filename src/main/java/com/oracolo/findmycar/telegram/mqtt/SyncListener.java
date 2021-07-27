package com.oracolo.findmycar.telegram.mqtt;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.mqtt.message.RetrySyncMessage;
import com.oracolo.findmycar.telegram.service.KeyChatService;
import com.oracolo.findmycar.telegram.service.MqttClientService;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

@ApplicationScoped
public class SyncListener implements IMqttMessageListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	KeyChatService keyChatService;

	@Inject
	Event<RetrySyncMessage> retrySyncMessageEvent;



	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try{
			RetrySyncMessage syncMessage = Json.decodeValue(Buffer.buffer(message.getPayload()),RetrySyncMessage.class);
			retrySyncMessageEvent.fire(syncMessage);
		}catch (Exception e){
			logger.error("Error handling message.",e);
		}

	}
}
