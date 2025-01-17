package com.revature.pokemondb.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.pokemondb.auth.Auth;
import com.revature.pokemondb.dtos.UserDTO;
import com.revature.pokemondb.exceptions.BannedException;
import com.revature.pokemondb.exceptions.FailedAuthenticationException;
import com.revature.pokemondb.exceptions.RecordNotFoundException;
import com.revature.pokemondb.models.User;
import com.revature.pokemondb.services.TokenService;
import com.revature.pokemondb.services.UserService;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping(path="/auth")
public class AuthController {
    private UserService userService;
    private TokenService tokenService;

    public AuthController (UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }
    
    @Auth(requiredRole = "admin")
    @GetMapping
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("Hello World Auth PokemonDB!");
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
		String password = credentials.get("password");
		try {
            User user = userService.loginUser(username, password);
            if (user != null) {
                UserDTO userDto = new UserDTO(user);
                String jws = tokenService.createToken(user);
                userDto.setToken(jws);
                return ResponseEntity.status(200).header("Auth", jws).body(userDto);
            }
        } catch (FailedAuthenticationException | NoSuchAlgorithmException | RecordNotFoundException e) {
            e.printStackTrace();
        } catch (BannedException e1) {
            e1.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
