package com.revature.pokemondb.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** 
 * @author Barry Norton
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class UserMini {
	@Id
	private Long id;
	private String username;

    public UserMini(Long id) {
		this.id = id;
	}

	public UserMini(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
	}
}