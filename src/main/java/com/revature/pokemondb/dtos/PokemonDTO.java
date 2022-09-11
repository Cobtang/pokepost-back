package com.revature.pokemondb.dtos;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.revature.pokemondb.models.Pokemon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Barry Norton
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pokemon")
public class PokemonDTO {
	@Id
	@Column
	private int id;

	@Column(name="name")
    private String name;

	
	@Column(name="sprite")
	private String imageUrl;


	@Column(name="gen")
    private int generation;

	public PokemonDTO(int id) {
		this.id = id;
	}

	public PokemonDTO(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public PokemonDTO(int id, String name, String imageUrl) {
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
	}

	public PokemonDTO (Pokemon pokemon) {
		this.id = pokemon.getId();
		this.name = pokemon.getName();
		this.imageUrl = pokemon.getImageUrl();
		this.generation = pokemon.getGeneration();
	}
}
