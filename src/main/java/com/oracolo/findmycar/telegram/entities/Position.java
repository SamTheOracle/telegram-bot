package com.oracolo.findmycar.telegram.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "positions")
public class Position {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "latitude")
	private String latitude;

	@Column(name = "longitude")
	private String longitude;

	@Column(name = "chatid")
	private Long chatId;

	@Column(name = "userid")
	private String userId;

	@Column(name = "timezone")
	private String timezone;

	@Column(name = "user_timestamp")
	private String timeStamp;

	@Column(name = "vehicle_name")
	private String vehicleName;

	@Column(name = "vehicle_id")
	private Integer vehicleId;

	public Position(){}

	public Position(Integer vehicleId, String vehicleName){
		this.vehicleId = vehicleId;
		this.vehicleName = vehicleName;
	}
	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Position))
			return false;
		Position position = (Position) o;
		return id.equals(position.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Position{" + "id=" + id + ", latitude='" + latitude + '\'' + ", longitude='" + longitude + '\'' + ", chatId=" + chatId
				+ ", userId='" + userId + '\'' + ", timezone='" + timezone + '\'' + ", timeStamp='" + timeStamp + '\'' + ", vehicleName='"
				+ vehicleName + '\'' + ", vehicleId=" + vehicleId + '}';
	}
}
