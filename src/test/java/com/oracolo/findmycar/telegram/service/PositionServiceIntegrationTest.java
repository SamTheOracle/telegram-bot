package com.oracolo.findmycar.telegram.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.oracolo.findmycar.telegram.entities.Position;
import com.oracolo.findmycar.telegram.entities.selectmappings.PositionVehicle;

import commons.BaseTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class PositionServiceIntegrationTest extends BaseTest {

	@Inject
	PositionService service;

	@Test
	@DisplayName("Correctly insert position")
	void insertPosition() {
		Position position = createPosition(123L,"asda",12,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),"kjsdfb");
		Assertions.assertDoesNotThrow(()->service.insertPosition(position));

	}

	@Test
	@DisplayName("Should get last position")
	void getLastPosition() {
		String userId = "user_1";
		Integer vehicleId = 1;
		Position position1 = createPosition(123L,"asda",vehicleId,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position1));

		Position position2 = createPosition(123L,"asda",1,"lat_2","long_2","sfd",ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position2));

		Optional<Position> lastPositionOptional = Assertions.assertDoesNotThrow(()->service.getLastPosition(vehicleId));
		Assertions.assertTrue(lastPositionOptional.isPresent());
		Position lastPosition = lastPositionOptional.get();
		Assertions.assertEquals("lat_2",lastPosition.getLatitude());
		Assertions.assertEquals("long_2",lastPosition.getLongitude());
	}

	@Test
	@DisplayName("Should get only one vehicle from two positions of same vehicles")
	void getPositionVehiclesForUserId() {
		String userId ="position_vehicle_userid";
		Integer vehicleId = new Random().nextInt(100000);
		String vehicleName = "cool_vehicle";
		Position position1 = createPosition(123L,vehicleName,vehicleId,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position1));
		Position position2 = createPosition(123L,vehicleName,vehicleId,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position2));

		List<PositionVehicle> positionVehicles = Assertions.assertDoesNotThrow(()->service.getPositionVehiclesForUserId(userId));
		Assertions.assertEquals(1,positionVehicles.size());
		PositionVehicle positionVehicle = positionVehicles.get(0);
		Assertions.assertEquals(vehicleName,positionVehicle.getVehicleName());
		Assertions.assertEquals(vehicleId,positionVehicle.getVehicleId());

	}

	@Test
	@DisplayName("Should get only two vehicles from three positions of two different vehicles")
	void getMultiplePositionVehiclesForUserId() {
		String userId ="position_vehicle_userid_random2";
		Integer vehicleId1 = new Random().nextInt(100000);
		String vehicleName1 = "cool_vehicle_2";
		Integer vehicleId2 = new Random().nextInt(100000);
		String vehicleName2 = "cool_vehicle_2";
		Position position1 = createPosition(123L,vehicleName1,vehicleId1,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position1));
		Position position2 = createPosition(123L,vehicleName1,vehicleId1,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position2));
		Position position3 = createPosition(123L,vehicleName2,vehicleId2,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position3));

		List<PositionVehicle> positionVehicles = Assertions.assertDoesNotThrow(()->service.getPositionVehiclesForUserId(userId));
		Assertions.assertEquals(2,positionVehicles.size());
		Assertions.assertTrue(positionVehicles.stream().anyMatch(positionVehicle -> positionVehicle.getVehicleId().equals(vehicleId1)));
		Assertions.assertTrue(positionVehicles.stream().anyMatch(positionVehicle -> positionVehicle.getVehicleId().equals(vehicleId2)));


	}

	@Test
	@DisplayName("Should find position vehicle")
	void getPositionVehicle() {
		String userId ="position_vehicle_userid_random1";
		Integer vehicleId = new Random().nextInt(100000);
		String vehicleName = "should_find_position_vehicle";
		Position position = createPosition(123L,vehicleName,vehicleId,"sdfs","sdsd","sfd", ZonedDateTime.now().toString(),userId);
		Assertions.assertDoesNotThrow(()->service.insertPosition(position));
		Optional<PositionVehicle> positionVehicleOptional = Assertions.assertDoesNotThrow(()->service.getPositionVehicle(vehicleId,userId));
		Assertions.assertTrue(positionVehicleOptional.isPresent());
		PositionVehicle positionVehicle = positionVehicleOptional.get();
		Assertions.assertEquals(vehicleId,positionVehicle.getVehicleId());
		Assertions.assertEquals(vehicleName,positionVehicle.getVehicleName());
	}


}