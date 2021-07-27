package com.oracolo.findmycar.telegram.mqtt;

import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.entities.Position;
import com.oracolo.findmycar.telegram.mqtt.converter.PositionMessageConverter;
import com.oracolo.findmycar.telegram.mqtt.enums.PersistenceAction;
import com.oracolo.findmycar.telegram.mqtt.message.PositionMessage;
import com.oracolo.findmycar.telegram.service.PositionService;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class PositionMessageListener implements IMqttMessageListener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Inject
	PositionService positionService;

	@Inject
	PositionMessageConverter positionMessageConverter;

	@Inject
	ManagedExecutor executor;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.submit(()->{
			PositionMessage positionMessage = Json.decodeValue(Buffer.buffer(message.getPayload()),PositionMessage.class);
			logger.debug("Received message {}", positionMessage);
			PersistenceAction action = positionMessage.action;
			if (action == PersistenceAction.CREATE) {
				Position position = positionMessageConverter.from(positionMessage);
				positionService.insertPosition(position);
			}
		});
	}
}
