CREATE TABLE unique_keys_chat_ids (
	id MEDIUMINT auto_increment NOT NULL,
	unique_key varchar(255) NOT NULL,
	chat_id BIGINT NOT NULL,
	CONSTRAINT unique_keys_chat_ids_PK PRIMARY KEY (id),
	CONSTRAINT unique_keys_chat_ids_UN UNIQUE KEY (chat_id)
);