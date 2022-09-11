package com.revature.pokemondb.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.revature.pokemondb.dtos.UserBodyDTO;
import com.revature.pokemondb.dtos.UserDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author Colby Tang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="id", updatable=false, insertable=false, nullable=false)
	private long id;
	private String username;
	private String email;
	private String role;
	private String password;
	private byte[] salt;
	@CreationTimestamp
	private Timestamp createdAt;
	@UpdateTimestamp
	private Timestamp updatedAt;

	public User(long id) {
		this.id = id;
		this.username = "";
		this.email = "";
		this.password = "";
		this.role = "user";
	}

	public User(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User(Long userId, String username, String email, String password) {
		this.id = userId;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User(Long userId, String username, String email, String password, byte[] salt) {
		this.id = userId;
		this.username = username;
		this.email = email;
		this.salt = salt;
		this.password = password;
	}

	public User(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.salt = user.getSalt();
		this.password = user.getPassword();
		this.role = user.getRole();
	}

	public User(UserDTO user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.role = user.getRole();
	}

	public User(Map<String, String> map) {
		if ((map.get("userId") != null)) {
			this.id = Long.valueOf(map.get("userId"));
		}

		this.username = map.get("username");

		this.email = map.get("email");

		if (map.get("salt") != null) {
			this.salt = map.get("salt").getBytes();
		}

		this.password = map.get("password");

		if (map.get("role") != null) {
			this.role = map.get("role");
		} else {
			this.role = "user";
		}
	}

	public User(UserBodyDTO user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.salt = user.getSalt();
		this.password = user.getPassword();
		this.role = user.getRole();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(salt);
		result = prime * result + Objects.hash(email, password, id, username);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(password, other.password)
				&& Arrays.equals(salt, other.salt) && Objects.equals(id, other.id)
				&& Objects.equals(username, other.username);
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		String retString = "UserId=%d, Username=%s, Email=%s, Password=%s, Salt=%s";
		return String.format(retString, getId(), getUsername(), getEmail(), getPassword(), getSalt());
	}
}
