package com.oracolo.findmycar.telegram.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.oracolo.findmycar.telegram.entities.KeyChat;

@ApplicationScoped
public class BaseDao<T> {

	@Inject
	EntityManager em;

	public void insert(T entity){
		em.persist(entity);
	}

	public void update(T entity) {
		em.merge(entity);
	}
}
