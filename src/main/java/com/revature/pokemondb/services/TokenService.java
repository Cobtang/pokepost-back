package com.revature.pokemondb.services;

import java.util.Optional;

import com.revature.pokemondb.dtos.UserDTO;
import com.revature.pokemondb.exceptions.FailedAuthenticationException;
import com.revature.pokemondb.exceptions.TokenExpirationException;
import com.revature.pokemondb.models.User;

public interface TokenService {
    public String createToken(User user);
	public Optional<UserDTO> validateToken(String token) throws FailedAuthenticationException, TokenExpirationException;
	public int getDefaultExpiration();
	public boolean isUserBanned (Long id);
}
