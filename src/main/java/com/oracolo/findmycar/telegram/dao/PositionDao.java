package com.oracolo.findmycar.telegram.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.oracolo.findmycar.telegram.entities.Position;
import com.oracolo.findmycar.telegram.entities.selectmappings.PositionVehicle;

@ApplicationScoped
public class PositionDao extends BaseDao<Position> {

	public Optional<Position> getLastPosition(Integer vehicleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> positionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Order descendingOrder = cb.desc(root.get("timeStamp"));
		Predicate vehicleIdPredicate = cb.equal(root.get("vehicleId"), vehicleId);
		positionCriteriaQuery.where(vehicleIdPredicate).orderBy(descendingOrder);
		TypedQuery<Position> query = em.createQuery(positionCriteriaQuery);
		query.setMaxResults(1);
		return query.getResultStream().findFirst();
	}

	public List<PositionVehicle> getPositionVehicles(String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> positionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Predicate userIdPredicate = cb.equal(root.get("userId"), userId);
		positionCriteriaQuery.distinct(true).multiselect(root.get("vehicleId"), root.get("vehicleName")).where(userIdPredicate);
		TypedQuery<Position> query = em.createQuery(positionCriteriaQuery);
		return query.getResultStream().map(position -> new PositionVehicle(position.getVehicleId(), position.getVehicleName())).collect(
				Collectors.toUnmodifiableList());
	}

	public Optional<PositionVehicle> getPositionVehicle(String userId, Integer vehicleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> positionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Predicate userIdPredicate = cb.equal(root.get("userId"), userId);
		Predicate vehicleIdPredicate = cb.equal(root.get("vehicleId"), vehicleId);
		positionCriteriaQuery.distinct(true).multiselect(root.get("vehicleId"), root.get("vehicleName")).where(
				cb.and(userIdPredicate, vehicleIdPredicate));
		TypedQuery<Position> query = em.createQuery(positionCriteriaQuery);
		query.setMaxResults(1);
		return query.getResultStream().findFirst().map(position -> new PositionVehicle(position.getVehicleId(), position.getVehicleName()));
	}

	public List<PositionVehicle> getPositionVehicles(Integer chatId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> positionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Predicate chatIdPredicate = cb.equal(root.get("chatId"), chatId);
		positionCriteriaQuery.distinct(true).multiselect(root.get("vehicleId"), root.get("vehicleName")).where(chatIdPredicate);
		TypedQuery<Position> query = em.createQuery(positionCriteriaQuery);
		return query.getResultStream().map(position -> new PositionVehicle(position.getVehicleId(), position.getVehicleName())).collect(
				Collectors.toUnmodifiableList());
	}

	public Optional<PositionVehicle> getPositionVehicle(Integer vehicleId, long chatId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Position> positionCriteriaQuery = cb.createQuery(Position.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Predicate chatIdPredicate = cb.equal(root.get("chatId"), chatId);
		Predicate vehicleIdPredicate = cb.equal(root.get("vehicleId"), vehicleId);
		positionCriteriaQuery.distinct(true).multiselect(root.get("vehicleId"), root.get("vehicleName")).where(
				cb.and(chatIdPredicate, vehicleIdPredicate));
		TypedQuery<Position> query = em.createQuery(positionCriteriaQuery);
		query.setMaxResults(1);
		return query.getResultStream().findFirst().map(position -> new PositionVehicle(position.getVehicleId(), position.getVehicleName()));
	}

	public long countPositionsByChatId(long chatId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> positionCriteriaQuery = cb.createQuery(Long.class);
		Root<Position> root = positionCriteriaQuery.from(Position.class);
		Predicate chatIdPredicate = cb.equal(root.get("chatId"), chatId);
		positionCriteriaQuery.select(cb.count(positionCriteriaQuery.from(Position.class)));
		positionCriteriaQuery.where(chatIdPredicate);
		TypedQuery<Long> query = em.createQuery(positionCriteriaQuery);
		return query.getSingleResult();
	}

	public void deleteVehiclePositionByVehicleId(Integer vehicleId, String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<Position> criteriaDelete = cb.createCriteriaDelete(Position.class);
		Root<Position> positionRoot = criteriaDelete.from(Position.class);
		Predicate vehicleIdPredicate = cb.equal(positionRoot.get("vehicleId"),vehicleId);
		Predicate userIdPredicate = cb.equal(positionRoot.get("userId"),userId);
		criteriaDelete.where(cb.and(vehicleIdPredicate,userIdPredicate));
		em.createQuery(criteriaDelete).executeUpdate();
	}
}
