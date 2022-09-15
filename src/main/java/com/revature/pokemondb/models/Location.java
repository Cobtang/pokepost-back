package com.revature.pokemondb.models;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    private int id;

    private String name;
    private String url;

    @ElementCollection
    private Set<String> methods;
    
    private int maxChance;
    private String versionName;
}
