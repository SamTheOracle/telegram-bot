package com.oracolo.findmycar.telegram.service;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.oracolo.findmycar.telegram.dao.KeyChatDao;
import com.oracolo.findmycar.telegram.entities.KeyChat;

@ApplicationScoped
public class KeyChatService {
	@Inject
	KeyChatDao keyChatDao;

	public List<KeyChat> getByUniqueKeyValues(List<String> uniqueKeyValues){
		return keyChatDao.getKeyChat(uniqueKeyValues);
	}


	public Optional<KeyChat> getKeyChat(Long chatId){
		return keyChatDao.getKeyChatByChatId(chatId);
	}
	@Transactional
	public void insert(KeyChat keyChat){
		keyChatDao.insert(keyChat);
	}

	@Transactional
	public void update(KeyChat keyChat) {
		keyChatDao.update(keyChat);
	}
}
