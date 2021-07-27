package com.oracolo.findmycar.telegram.mqtt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.mqtt.enums.PersistenceAction;
import com.oracolo.findmycar.telegram.mqtt.message.VehicleMessage;
import com.oracolo.findmycar.telegram.service.PositionService;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

@ApplicationScoped
public class VehicleMessageListener implements IMqttMessageListener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Inject
	PositionService positionService;

	@Inject
	ManagedExecutor executor;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.submit(()->{
			VehicleMessage vehicleMessage = Json.decodeValue(Buffer.buffer(message.getPayload()),VehicleMessage.class);
			logger.debug("Received message {}", vehicleMessage);
			if (vehicleMessage.action == PersistenceAction.DELETE) {
				logger.debug("Deleting vehicle data");
				positionService.deleteVehicleData(vehicleMessage.vehicleId, vehicleMessage.userId);
			}
		});
	}
}
