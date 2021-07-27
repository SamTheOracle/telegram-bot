package com.oracolo.findmycar.telegram.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.telegram.mqtt.PositionMessageListener;
import com.oracolo.findmycar.telegram.mqtt.SyncListener;
import com.oracolo.findmycar.telegram.mqtt.VehicleMessageListener;
import com.oracolo.findmycar.telegram.mqtt.message.KeyChatValuesMessage;
import com.oracolo.findmycar.telegram.mqtt.message.TelegramUserMessage;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class MqttClientService implements IMqttActionListener, MqttCallbackExtended {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private static final String VEHICLE_TOPIC = "vehicles";
	private static final String POSITION_TOPIC = "positions";
	private static final String MERGE_TOPIC = "users/sync/merge";
	private static final String RETRY_SYNC_TOPIC = "users/u2t/sync/retry";
	private static final String RETRY_SYNC_TOPIC_REPLY = "users/t2u/sync/retry";

	@ConfigProperty(name = "mqtt.port")
	Integer mqttPort;

	@ConfigProperty(name = "mqtt.host")
	String mqttHost;

	@ConfigProperty(name = "mqtt.protocol")
	String mqttProtocol;

	@ConfigProperty(name = "mqtt.client.id")
	String mqttClientId;

	@ConfigProperty(name = "mqtt.connection.delay")
	Integer connectionDelay;

	@Inject
	PositionMessageListener positionMessageListener;

	@Inject
	VehicleMessageListener vehicleMessageListener;

	@Inject
	SyncListener syncListener;

	private MqttAsyncClient mqttClient;

	void init(@Observes StartupEvent event) {


		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setAutomaticReconnect(true);
		connOpts.setKeepAliveInterval(10);
		connOpts.setConnectionTimeout(60);

		connOpts.setUserName(mqttClientId);
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.schedule(() -> {
			try {
				String serverURI = mqttProtocol + "://" + mqttHost + ":" + mqttPort;
				logger.info("Connecting to broker {}.", serverURI);
				mqttClient = new MqttAsyncClient(serverURI, mqttClientId, new MemoryPersistence());
				mqttClient.setCallback(this);
				mqttClient.connect(connOpts, null, this);
				service.shutdown();
			} catch (MqttException e) {
				logger.error("Error when creating client.", e);
			}
		}, connectionDelay, TimeUnit.SECONDS);
	}

	@Override
	public void onSuccess(IMqttToken asyncActionToken) {
		logger.trace("Action success {}.", asyncActionToken);
	}

	@Override
	public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
		logger.error("Action failed.", exception);
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		logger.info("{}onnected to {}.", reconnect ? "Ric" : "C", serverURI);
		try {
			mqttClient.subscribe(POSITION_TOPIC, 0, positionMessageListener);
			mqttClient.subscribe(VEHICLE_TOPIC, 0, vehicleMessageListener);
			mqttClient.subscribe(RETRY_SYNC_TOPIC, 0, syncListener);
			logger.info("Done subscribing");
		} catch (MqttException e) {
			logger.error("Error during subscribe.", e);
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		logger.error("Lost connection.", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.trace("Received message {} from {}.", new String(message.getPayload()), topic);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.trace("Delivery complete {}.", token);

	}

	public void sendKeyChatValuesMessage(KeyChatValuesMessage keyChatValuesMessage) {
		sendMessage(RETRY_SYNC_TOPIC_REPLY, convert(keyChatValuesMessage));
	}

	public void sendTelegramUserMessage(TelegramUserMessage telegramUserMessage) {
		sendMessage(MERGE_TOPIC, convert(telegramUserMessage));
	}

	private synchronized void sendMessage(String topic, byte[] message) {
		try {
			mqttClient.publish(topic, message, 0, false);
		} catch (Exception e) {
			logger.error("Error sending message.", e);
		}
	}

	private static byte[] convert(Object message) {
		return JsonObject.mapFrom(message).encode().getBytes(StandardCharsets.UTF_8);
	}

}
