package com.workable.matchmakers.web.dto.users;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * UserBaseDto
 */
@JsonPropertyOrder({ "username", "password", "name", "email" })
public class UserBaseDto {

	@NotEmpty
	@ApiModelProperty(example = "panto", required = true)
	private String username;

	// @NotEmpty
	@ApiModelProperty(example = "password", required = true)
	private String password;

	@NotEmpty
	@ApiModelProperty(example = "panos@antonakos.com", required = true)
	private String email;

	@ApiModelProperty(example = "Panos Antonakos")
	private String name;

	public UserBaseDto() {
	}

	public UserBaseDto(String username, String password, String name, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	// Factory methods
	public UserBaseDto username(String username) {
		this.username = username;
		return this;
	}

	public UserBaseDto password(String password) {
		this.password = password;
		return this;
	}

	public UserBaseDto name(String name) {
		this.name = name;
		return this;
	}

	public UserBaseDto email(String email) {
		this.email = email;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
