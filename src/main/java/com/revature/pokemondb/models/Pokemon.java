package com.revature.pokemondb.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.*;

// @JsonIgnoreProperties({"hibernateLazyInitializer"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "pokemon")
public class Pokemon {
	
	@Id
	@Column
    private int id;

    private String name;
    private int height;
    private int weight;
    private String primaryType;
    private String secondaryType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id", referencedColumnName = "id")
	private PokemonStats baseStats;
    
	private String imageUrl;
    private int generation;
    private String category;
    private String description;
    private int baseExperience;
    
    // Pokemon have 1 or 2 abilities and then a 3rd hidden ability sometimes.
    @ManyToMany
    @JoinTable(
        name="pokemon_abilities",
        joinColumns = @JoinColumn(name="pokemon_id"),
        inverseJoinColumns = @JoinColumn(name="ability_name")
    )
    private Set<Ability> abilities;

    @ElementCollection
    private Set<EvolutionChain> evolutionChain;

    @ManyToMany
    @JoinTable(
        name="location_versions",
        joinColumns = @JoinColumn(name="pokemon_id"),
        inverseJoinColumns = @JoinColumn(name="location_version")
    )
    private List<Location> locationVersions;

    @Transient
    private PokemonMoves moves;
    
    public Pokemon (int id) {
        this.id = id;
        this.name = "";
        this.height = 0;
        this.weight = 0;
        this.primaryType = "";
        this.secondaryType = "";
        this.baseStats = new PokemonStats();
        this.imageUrl = "";
        this.generation = 1;
        this.category = "";
        this.description = "";
        this.evolutionChain = new HashSet<>();
        this.locationVersions = new ArrayList<>();
        this.baseExperience = 0;
        this.abilities = new HashSet<>();
        this.moves = new PokemonMoves();
    }

    public void setHeightFromInches(float heightInInches) {
        this.height = (int) (heightInInches * 0.254f);
    }

    public float getHeightInInches() {
        return getHeight() * 3.937f;
    }

    public float getHeightInFeet() {
        return getHeight() * 0.328084f;
    }

    public String getHeightInFeetInches() {
        float heightInInches = getHeight() * 3.937f;
        int feet = (int) (heightInInches / 12);
        String inches = String.valueOf(Math.round(heightInInches % 12));
        return feet + "\'" + inches + "\"";
    }

    public float getWeightInPounds() {
        return weight / 4.536f;
    }

    public String getWeightInPoundsString() {
        float pounds = weight / 4.536f;
        return pounds + "lb";
    }

    public void setWeightFromPounds(float weight) {
        this.weight = (int) (weight * 4.53592f);
    }

    @Override
    public String toString() {
        String retString = name + " (" + id + ") \n" +
        "[weight=" + getWeightInPoundsString() + ", height=" + getHeightInFeetInches() + "] \n"+ 
        "[category=" + category + ", types=[" + primaryType + "," + secondaryType + "], base experience: " + baseExperience + "] \n" +
        "[generation=" + generation + "]\n";
        
        // Base Stats
        if (baseStats != null) createBaseStatsString();

        // Image
        retString += "[image: " + imageUrl + "] \n";

        // Description
        retString += "[description=" + description + "]\n";

        // Abilities
        if (abilities != null) retString += createAbilitiesString ();

        // Evolution Chain
        if (evolutionChain != null) retString += createEvolutionString();

        // Moves
        StringJoiner joiner = new StringJoiner(",");
        retString += createMoveString ("levelMoves", moves.getLevelMoves(), joiner);

        joiner = new StringJoiner(",");
        retString += createMoveString ("eggMoves", moves.getEggMoves(), joiner);

        joiner = new StringJoiner(",");
        retString += createMoveString ("tutorMoves", moves.getTutorMoves(), joiner);

        joiner = new StringJoiner(",");
        retString += createMoveString ("machineMoves", moves.getMachineMoves(), joiner);

        joiner = new StringJoiner(",");
        retString += createMoveString ("otherMoves", moves.getOtherMoves(), joiner);
        
        // Locations/Versions
        if (locationVersions != null) retString += createLocationString ();

        return retString;
    }

    private String createBaseStatsString () {
        String retString = "";
        retString += "[baseStats:";

        StringBuilder builder = new StringBuilder();
        builder.append("\t" + "HP" + ": " + baseStats.getHp());
        builder.append("\t" + "Attack" + ": " + baseStats.getAttack());
        builder.append("\t" + "Defense" + ": " + baseStats.getDefense());
        builder.append("\t" + "Special Attack" + ": " + baseStats.getSpecialAttack());
        builder.append("\t" + "Special Defense" + ": " + baseStats.getSpecialDefense());
        builder.append("\t" + "Speed" + ": " + baseStats.getSpeed());
        retString += builder.toString();
        retString += "] \n";
        return retString;
    }

    private String createAbilitiesString () {
        String retString = "";
        retString += "[abilities: ";
        StringBuilder builder = new StringBuilder();
        for (Ability ability : abilities) {
            builder.append("\t" + ability);
        }
        retString += builder.toString();
        retString += "] \n";
        return retString;
    }

    private String createEvolutionString () {
        String retString = "";
        retString += "[evolutionChain: ";
        StringBuilder builder = new StringBuilder();
        for (EvolutionChain evolution : evolutionChain) {
            String evolutionName = evolution.getName();
            String evolutionURL = evolution.getUrl();
            builder.append("\t" + evolutionName + ": " + evolutionURL);
        }
        retString += builder.toString();
        retString += "] \n";
        return retString;
    }

    private String createMoveString (String moveName, Set<Move> moves, StringJoiner joiner) {
        String retString = "";
        retString += "["+ moveName + ": ";
        if (moves != null && !moves.isEmpty()) {
            for (Move move : moves) {joiner.add(move.getName()); }
            retString += joiner.toString();
        }
        retString += "] \n";
        return retString;
    }

    private String createLocationString () {
        String retString = "";
        retString += "[locations: ";
        StringBuilder builder = new StringBuilder();
        for (Location location : locationVersions) {
            String locationName = location.getName();
            String locationURL = location.getUrl();
            String versionName = location.getVersionName();
            String maxChance = String.valueOf(location.getMaxChance());
            String methods = location.getMethods().toString();
            builder.append("\t\t" + locationName + ": " + locationURL + versionName + ": " + maxChance + " " + methods);
        }
        retString += builder.toString();
        retString += "] \n";
        
        return retString;
    }
}