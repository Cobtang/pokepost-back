package com.revature.pokemondb.dtos;

import com.revature.pokemondb.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * A Data Transfer version of User
 * @author Colby Tang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long userId;
	private String username;
	private String email;
	private String role = "user";
	private String token = "";

    public UserDTO(Long userId, String username, String email) {
		this.userId = userId;
		this.username = username;
		this.email = email;
	}

	public UserDTO(Long userId, String username) {
		this.userId = userId;
		this.username = username;
	}
	
	public UserDTO (User user) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.role = user.getRole();
	}
	 
	public UserDTO (User user, String token) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.token = token;
	}
}
