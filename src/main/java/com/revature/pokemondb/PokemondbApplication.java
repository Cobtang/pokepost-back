package com.revature.pokemondb;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemondbApplication {
	private static final Logger logger = LoggerFactory.getLogger(PokemondbApplication.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PokemondbApplication.class, args);

		Scanner keyboard = new Scanner(System.in);
		printLogo ();
		logger.info("Welcome to the PokePost Spring Application!");
		logger.info("Press Enter to stop the application: ");
		keyboard.nextLine();
		keyboard.close();
		logger.info("Exiting the PokePost Spring Application");
		System.exit(0);
	}

	public static void printLogo () throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\com\\revature\\pokemondb\\pokepost_logo.txt"))) {
			for (String line; (line = reader.readLine()) != null;) {
				logger.info(line); 
			}
		} catch (IOException e) {
			throw new IOException(e);
		}
	}
}
