package com.revature.pokemondb.dtos;

import com.revature.pokemondb.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This DTO is for accepting User objects through a request
 * @author Colby Tang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyDTO {
	private Long userId = 0l; 
	private String username;
	private String email;
	private String role;
	private String password;
	private String token;
	private byte[] salt;

	public UserBodyDTO (User user) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.salt = user.getSalt();
		this.password = user.getPassword();
		this.role = user.getRole();
	}

	public UserBodyDTO (UserDTO user) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.token = user.getToken();
	}
}
