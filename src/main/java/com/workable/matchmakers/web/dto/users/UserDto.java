package com.workable.matchmakers.web.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import com.workable.matchmakers.dao.model.User;
import com.workable.matchmakers.web.dto.Dto;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AccountDto
 */
@JsonPropertyOrder({ "id", "username", "password", "name", "email" })
@Component
public class UserDto extends UserBaseDto implements Dto<User> {

	public static List<String> fields = Arrays.asList(UserBaseDto.class.getDeclaredFields())
			.stream()
			.map(field -> field.getName())
			.collect(Collectors.toList());

	@NotNull
	@ApiModelProperty(example = "eba3e4be-0d60-41ee-92e4-0c96b1af5b6e", required = true)
	private UUID id;


	public UserDto() {
	}

	public UserDto(UUID id, String username, String password, String name, String email) {
		this.id = id;
		setUsername(username);
		setPassword(password);
		setName(name);
		setEmail(email);
	}

	public UserDto(UUID id, UserBaseDto userBaseDto) {
		this(userBaseDto);
		this.id = id;
	}

	public UserDto(UserBaseDto userBaseDto) {
		super(userBaseDto.getUsername(), userBaseDto.getPassword(), userBaseDto.getName(), userBaseDto.getEmail());
	}

	// Factory methods
	public UserDto id(UUID id) {
		this.id = id;
		return this;
	}

	public UserDto username(String username) {
		setUsername(username);
		return this;
	}

	public UserDto password(String password) {
		setPassword(password);
		return this;
	}

	public UserDto name(String name) {
		setName(name);
		return this;
	}

	public UserDto email(String email) {
		setEmail(email);
		return this;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@Override
	public void setPassword(String password) {
		super.setPassword(password);
	}

	@Override
	public UserDto fromEntity(User user) {
		this.id = user.getExternalId();

		setUsername(user.getUsername());
		setPassword(user.getPassword());
		setEmail(user.getEmail());
		setName(user.getName());

		return this;
	}

	@Override
	public User toEntity() {
		User user = new User();

		return toEntity(user);
	}

	@Override
	public User toEntity(User user) {
		// Do not pass this.getId(), USER_EXTERNAL_ID is auto-generated!
		user.setUsername(this.getUsername());
		user.setName(this.getName());
		user.setEmail(this.getEmail());
		// DAO: Add hash password

		return user;
	}
}
