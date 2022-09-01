package com.revature.pokemondb.dtos;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Barry Norton
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pokemon_fanart", schema = "pokemon_db")
public class FanartDTO {
	@Id
	private int id;
}