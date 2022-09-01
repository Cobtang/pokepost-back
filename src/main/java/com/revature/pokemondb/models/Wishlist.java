package com.revature.pokemondb.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.revature.pokemondb.dtos.PokemonDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pokemon_wishlists", schema = "pokemon_db")
public class Wishlist {
    @Id
    @Column(name = "id", updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @OneToOne(targetEntity = UserMini.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserMini user;

    @ManyToOne()
    @JoinColumn(name = "pokemon_id")
    private PokemonDTO pokemon;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

}
