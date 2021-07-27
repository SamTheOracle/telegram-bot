package com.oracolo.findmycar.telegram.entities.selectmappings;

public class PositionVehicle {
	private final Integer vehicleId;
	private final String vehicleName;

	public PositionVehicle(Integer vehicleId, String vehicleName) {
		this.vehicleId = vehicleId;
		this.vehicleName = vehicleName;
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	@Override
	public String toString() {
		return "PositionVehicle{" + "vehicleId=" + vehicleId + ", vehicleName='" + vehicleName + '\'' + '}';
	}
}