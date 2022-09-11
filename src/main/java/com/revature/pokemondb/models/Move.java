package com.revature.pokemondb.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "moves")
public class Move {
    @Id
    private String name;
    private String url;
    private String typeOfMove;
    private int levelLearnedAt;
}
