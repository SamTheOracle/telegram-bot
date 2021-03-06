package com.oracolo.findmycar.telegram.mqtt.message;

import com.oracolo.findmycar.telegram.mqtt.enums.PersistenceAction;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PositionMessage {
	public Long chatId;
	public String timeStamp;
	public String timezone;
	public String userId;
	public String longitude;
	public String latitude;
	public PersistenceAction action;
	public Integer vehicleId;
	public String vehicleName;

	@Override
	public String toString() {
		return "PositionMessage{" + "chatId=" + chatId + ", timeStamp='" + timeStamp + '\'' + ", timezone='" + timezone + '\''
				+ ", userId='" + userId + '\'' + ", longitude='" + longitude + '\'' + ", latitude='" + latitude + '\'' + ", action="
				+ action + ", vehicleId=" + vehicleId + ", vehicleName='" + vehicleName + '\'' + '}';
	}

}
