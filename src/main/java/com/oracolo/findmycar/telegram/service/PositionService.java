package com.oracolo.findmycar.telegram.service;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.oracolo.findmycar.telegram.dao.PositionDao;
import com.oracolo.findmycar.telegram.entities.Position;
import com.oracolo.findmycar.telegram.entities.selectmappings.PositionVehicle;

@ApplicationScoped
public class PositionService {

	@Inject
	PositionDao positionDao;

	@Transactional
	public void insertPosition(Position position){
		positionDao.insert(position);
	}

	public Optional<Position> getLastPosition(Integer vehicleId){
		return positionDao.getLastPosition(vehicleId);
	}
	public List<PositionVehicle> getPositionVehiclesForUserId(String userId){
		return positionDao.getPositionVehicles(userId);
	}
	public Optional<PositionVehicle> getPositionVehicle(Integer vehicleId, String userId){
		return positionDao.getPositionVehicle(userId,vehicleId);
	}

	public List<PositionVehicle> getPositionVehiclesByChatId(Integer chatId) {
		return positionDao.getPositionVehicles(chatId);
	}

	public Optional<PositionVehicle> getPositionVehicle(Integer vehicleId, long chatId) {
		return positionDao.getPositionVehicle(vehicleId,chatId);
	}

	public long getPositionCount(long chatId) {
		return positionDao.countPositionsByChatId(chatId);
	}

	/**
	 * This method remove all position that have vehicleId and userId as declared as parameters. Vehicle data is not removed in general, only for the user
	 * @param vehicleId
	 * @param userId
	 */
	@Transactional
	public void deleteVehicleData(Integer vehicleId, String userId) {
		positionDao.deleteVehiclePositionByVehicleId(vehicleId,userId);
	}
}
