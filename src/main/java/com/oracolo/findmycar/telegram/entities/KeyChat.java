package com.oracolo.findmycar.telegram.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unique_keys_chat_ids")
public class KeyChat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "unique_key")
	private String uniqueKeyValue;

	@Column(name = "chat_id")
	private Long chatId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUniqueKeyValue() {
		return uniqueKeyValue;
	}

	public void setUniqueKeyValue(String uniqueKeyValue) {
		this.uniqueKeyValue = uniqueKeyValue;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof KeyChat))
			return false;
		KeyChat keyChat = (KeyChat) o;
		return Objects.equals(id, keyChat.id) && Objects.equals(uniqueKeyValue, keyChat.uniqueKeyValue) && Objects.equals(chatId, keyChat.chatId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, uniqueKeyValue, chatId);
	}

	@Override
	public String toString() {
		return "KeyChat{" + "id=" + id + ", uniqueKey='" + uniqueKeyValue + '\'' + ", chatId=" + chatId + '}';
	}
}
