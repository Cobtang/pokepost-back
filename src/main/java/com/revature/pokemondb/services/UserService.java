package com.revature.pokemondb.services;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.revature.pokemondb.exceptions.EmailAlreadyExistsException;
import com.revature.pokemondb.exceptions.FailedAuthenticationException;
import com.revature.pokemondb.exceptions.InvalidInputException;
import com.revature.pokemondb.exceptions.RecordNotFoundException;
import com.revature.pokemondb.exceptions.UsernameAlreadyExistsException;
import com.revature.pokemondb.models.User;

public interface UserService {
	public User getUserById (Long id) throws RecordNotFoundException;
	
	public User getUserByUsername (String username) throws RecordNotFoundException;

	public List<User> getAllUsers();

	public User loginUser(String username, String password) throws FailedAuthenticationException, NoSuchAlgorithmException, RecordNotFoundException;
	
	public User registerUser(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException, NoSuchAlgorithmException, InvalidInputException;

	public User updateUser(User user) throws RecordNotFoundException;
	
	public User deleteUser(User user) throws RecordNotFoundException;

	public User banUser(User user);
	
	public User unBanUser(User user);
}
