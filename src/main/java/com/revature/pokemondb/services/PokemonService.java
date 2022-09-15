package com.revature.pokemondb.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.revature.pokemondb.dtos.PokemonDTO;
import com.revature.pokemondb.models.Pokemon;

public interface PokemonService {
    public String getPokemonJSON(int pokemonId);
    public String getPokemonJSON(String pokemonName);
    public PokemonDTO getReferencePokemon(int id);
    public PokemonDTO getReferencePokemon(String name);
    public Pokemon createPokemon (int pokemonId);
    public Pokemon createPokemon (String pokemonName);
    public Pokemon createPokemonObject (String pokemonJSON);
    public PokemonDTO createReferencePokemon (String pokemonName);
    public PokemonDTO createReferencePokemon (Integer pokemonId);
    public List<PokemonDTO> getAllPokemonById (List<Integer> ids);
    public void saveJSONFilesToLocal(String string) throws JsonMappingException, JsonProcessingException, IOException;
}
