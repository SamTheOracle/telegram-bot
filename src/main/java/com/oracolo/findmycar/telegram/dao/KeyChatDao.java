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

import com.oracolo.findmycar.telegram.entities.KeyChat;
import com.oracolo.findmycar.telegram.entities.KeyChat_;
import com.oracolo.findmycar.telegram.entities.Position;
import com.oracolo.findmycar.telegram.entities.selectmappings.PositionVehicle;

@ApplicationScoped
public class KeyChatDao extends BaseDao<KeyChat> {

	public List<KeyChat> getKeyChat(List<String> uniqueKeyValues){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<KeyChat> cq = cb.createQuery(KeyChat.class);
		Root<KeyChat> root = cq.from(KeyChat.class);
		cq.where(root.get(KeyChat_.uniqueKeyValue).in(uniqueKeyValues));
		return em.createQuery(cq).getResultStream().collect(Collectors.toList());
	}

	public Optional<KeyChat> getKeyChatByChatId(Long chatId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<KeyChat> cq = cb.createQuery(KeyChat.class);
		Root<KeyChat> root = cq.from(KeyChat.class);
		cq.where(cb.equal(root.get("chatId"),chatId));
		return em.createQuery(cq).getResultStream().findFirst();
	}
}
