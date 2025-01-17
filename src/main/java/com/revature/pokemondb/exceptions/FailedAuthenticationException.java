package com.revature.pokemondb.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
/**
 * Exception for when authentication fails like a wrong password
 * or bad token. HttpStatus.UNAUTHORIZED (401)
 */
@ResponseStatus (value = HttpStatus.UNAUTHORIZED, reason = "Authentication failed!")
public class FailedAuthenticationException extends Exception {
    public FailedAuthenticationException () { super(); }

    public FailedAuthenticationException (String message) {
        super(message);
    }
}
