package com.oracolo.findmycar.telegram.mqtt.converter;

import javax.enterprise.context.ApplicationScoped;

import com.oracolo.findmycar.telegram.entities.Position;
import com.oracolo.findmycar.telegram.mqtt.message.PositionMessage;

@ApplicationScoped
public class PositionMessageConverter {

	public Position from(PositionMessage message){
		Position position = new Position();
		position.setLatitude(message.latitude);
		position.setLongitude(message.longitude);
		position.setChatId(message.chatId);
		position.setTimezone(message.timezone);
		position.setTimeStamp(message.timeStamp);
		position.setUserId(message.userId);
		position.setVehicleId(message.vehicleId);
		position.setVehicleName(message.vehicleName);
		return position;
	}
}
