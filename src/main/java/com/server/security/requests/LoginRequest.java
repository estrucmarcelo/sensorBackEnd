package com.server.security.requests;

import lombok.Getter;
import lombok.Setter;

public class LoginRequest {
	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private String password;

	public LoginRequest() {}

	public LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
