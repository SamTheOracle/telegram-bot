package com.oracolo.findmycar.telegram.converter;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class ResponseConverter {

	public JsonObject fromResponse(Response response) throws IOException {
		byte[] responseInRawBytes = ((InputStream)response.getEntity()).readAllBytes();
		String responseAsString = new String(responseInRawBytes);
		return new JsonObject(responseAsString);
	}
}
