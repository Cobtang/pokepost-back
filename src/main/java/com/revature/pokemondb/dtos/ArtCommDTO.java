package com.revature.pokemondb.dtos;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author Barry Norton
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fanart_comments", schema = "pokemon_db")
public class ArtCommDTO {
	@Id
	private int id;
}