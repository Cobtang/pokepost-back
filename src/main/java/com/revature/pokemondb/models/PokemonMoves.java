package com.revature.pokemondb.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonMoves {
    @Id
    private int id;
    @ElementCollection
    private Set<Move> levelMoves;
    @ElementCollection
    private Set<Move> eggMoves;
    @ElementCollection
    private Set<Move> tutorMoves;
    @ElementCollection
    private Set<Move> machineMoves;
    @ElementCollection
    private Set<Move> otherMoves;

    public Set<Move> getAllMoves () {
        Set<Move> retMoves = new HashSet<>();
        retMoves.addAll(levelMoves);
        retMoves.addAll(eggMoves);
        retMoves.addAll(tutorMoves);
        retMoves.addAll(machineMoves);
        retMoves.addAll(otherMoves);
        return retMoves;
    }
}
