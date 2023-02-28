package com.server.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthMessageResponse {
	@Getter
	@Setter
	private String message;

	public AuthMessageResponse(String message) {
		this.message = message;
	}
}
