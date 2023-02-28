package com.server.dto;

import lombok.Getter;
import lombok.Setter;

public class UserSessionResponse {
	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String email;

	public UserSessionResponse(String id, String email) {
		this.id = id;
		this.email = email;
	}
}