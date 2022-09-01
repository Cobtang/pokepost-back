package com.revature.pokemondb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid user input.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Invalid input is given!")
public class InvalidInputException extends Exception {
    public InvalidInputException () {
        super ("Invalid input is given!");
    }
}
