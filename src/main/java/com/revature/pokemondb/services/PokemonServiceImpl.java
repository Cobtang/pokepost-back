package com.revature.pokemondb.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.pokemondb.dtos.PokemonDTO;
import com.revature.pokemondb.models.Ability;
import com.revature.pokemondb.models.EvolutionChain;
import com.revature.pokemondb.models.Location;
import com.revature.pokemondb.models.Move;
import com.revature.pokemondb.models.Pokemon;
import com.revature.pokemondb.models.PokemonMoves;
import com.revature.pokemondb.models.PokemonStats;
import com.revature.pokemondb.repositories.PokemonRepository;
import com.revature.pokemondb.utils.StringUtils;

@Service("PokemonService")
public class PokemonServiceImpl implements PokemonService {
    private PokemonRepository pokeRepo;
    private WebClientService webClient;

    @Autowired
    private ObjectMapper objMapper;


    public PokemonServiceImpl(PokemonRepository pRepo, WebClientService webClient) throws IOException {
        this.pokeRepo = pRepo;
        this.webClient = webClient;
    }

    /**
     * External API CALL - Grab Pokemon by Id
     * 
     * @param pokemonName
     * @return
     */
    public String getPokemonJSON(int pokemonId) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonId;
        return webClient.getRequestJSON(url);
    }

    /**
     * External API CALL - Grab Pokemon by Name
     * 
     * @param pokemonName
     * @return
     */
    public String getPokemonJSON(String pokemonName) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + StringUtils.convertToURIFormat(pokemonName);
        return  webClient.getRequestJSON(url);
    }

    /**
     * External API CALL - Grab Pokemon Species by Id
     * 
     * @param pokemonName
     * @return
     */
    public String getPokemonSpeciesJSON(int id) {
        String url = "https://pokeapi.co/api/v2/pokemon-species/" + id;
        return  webClient.getRequestJSON(url);
    }

    /**
     * External API CALL - Grab Pokemon Species by Id
     * 
     * @param pokemonName
     * @return
     */
    public String getPokemonSpeciesJSON(String pokemonName) {
        String url = "https://pokeapi.co/api/v2/pokemon-species/" + StringUtils.convertToURIFormat(pokemonName);
        return  webClient.getRequestJSON(url);
    }

    public void saveJSONFilesToLocal (String pokemonName) throws JsonMappingException, JsonProcessingException, IOException {
        String pokemonJson = getPokemonJSON (pokemonName);
        String speciesJson = getPokemonSpeciesJSON(pokemonName);
        String evolutionUrl = objMapper.readTree(speciesJson).get("evolution_chain").get("url").asText();
        String evolutionJson =  webClient.getRequestJSON(evolutionUrl);
        String locationsUrl = objMapper.readTree(pokemonJson).get("location_area_encounters").asText();
        String locationJson =  webClient.getRequestJSON(locationsUrl);

        final Path pokemonRoot = Paths.get("src/test/resources/json/pokemon/" + pokemonName);

        try {
            if (!Files.exists(pokemonRoot)) {
                Files.createDirectory(pokemonRoot);
            }
        } catch (IOException e) {
            throw new IOException("Could not initialize folder for upload!");
        }
        String pokemonPath = pokemonRoot.toString() + "/" + pokemonName;
        Files.write(Paths.get(pokemonPath + ".json"), pokemonJson.getBytes());
        Files.write(Paths.get(pokemonPath + "_species.json"), speciesJson.getBytes());
        Files.write(Paths.get(pokemonPath + "_evolution.json"), evolutionJson.getBytes());
        Files.write(Paths.get(pokemonPath + "_location.json"), locationJson.getBytes());
    }

    /**
     * 
     */
    public PokemonDTO getReferencePokemon(int id) {
        Optional<Pokemon> pokemon = pokeRepo.findById(id);
        if (pokemon.isPresent()) {return new PokemonDTO(pokemon.get());}
        return null;
    }

    /**
     * 
     */
    public PokemonDTO getReferencePokemon(String pokemonName) {
        Optional<Pokemon> pokemon = pokeRepo.findByName(pokemonName);
        if (pokemon.isPresent()) {return new PokemonDTO(pokemon.get());}
        return null;
    }

    /**
     * 
     */
    public Pokemon createPokemon (int pokemonId) {
        Pokemon pokemon = createPokemonObject(getPokemonJSON(pokemonId));
        pokeRepo.save(pokemon);
        return pokemon;
    }

    /**
     * 
     */
    public Pokemon createPokemon (String pokemonName) {
        Pokemon pokemon = createPokemonObject(getPokemonJSON(pokemonName));
        pokeRepo.save(pokemon);
        return pokemon;
    }

    /**
     * 
     */
    public List<PokemonDTO> getAllPokemonById (List<Integer> ids) {
        List<PokemonDTO> pokemonList = new ArrayList<>();
        // Retrieve pokemon one by one
        for (Integer id : ids) {
            Optional<Pokemon> oPokemon = pokeRepo.findById(id);
            if (oPokemon.isPresent())
                pokemonList.add(new PokemonDTO(oPokemon.get()));
        }
        return pokemonList;
    }

    public PokemonDTO createReferencePokemon (String pokemonName) {
        Optional<Pokemon> oPokemon = pokeRepo.findByName(pokemonName);
        if (oPokemon.isPresent()) {
            return new PokemonDTO(oPokemon.get());
        }
        return new PokemonDTO(createPokemon(pokemonName));
    }

    public PokemonDTO createReferencePokemon (Integer pokemonId) {
        Optional<Pokemon> oPokemon = pokeRepo.findById(pokemonId);
        if (oPokemon.isPresent()) {
            return new PokemonDTO(oPokemon.get());
        }
        return new PokemonDTO(createPokemon(pokemonId));
    }

    /**
     * Create a pokemon object using four different API calls from PokeAPI.
     */
    public Pokemon createPokemonObject (String pokemonJSON) {
        Pokemon pokemon = new Pokemon();
        try {
            // ---------------------------Pokemon JSON--------------------------------------
            JsonNode pokemonRoot = objMapper.readTree(pokemonJSON);
            
            // Id
            int id = pokemonRoot.get("id").asInt();
            pokemon.setId(id);

            // Name
            String name = pokemonRoot.get("name").asText();
            pokemon.setName(name);

            // Height
            int height = pokemonRoot.get("height").asInt();
            pokemon.setHeight(height);

            // Weight
            int weight = pokemonRoot.get("weight").asInt();
            pokemon.setWeight(weight);

            // Types
            String[] types = getTypes(pokemonRoot.get("types"));
            pokemon.setPrimaryType(types[0]);
            if (types.length > 1) pokemon.setSecondaryType(types[1]);

            // Base Stats
            PokemonStats baseStats = getBaseStats(id, pokemonRoot.get("stats"));
            pokemon.setBaseStats(baseStats);

            // Image URL
            String imageUrl = pokemonRoot.get("sprites").get("other").get("official-artwork").get("front_default").asText();
            pokemon.setImageUrl(imageUrl);

            // Base Experience
            int baseExperience = pokemonRoot.get("base_experience").asInt();
            pokemon.setBaseExperience(baseExperience);

            // Abilities
            Set<Ability> abilities = getAbilities(pokemonRoot.get("abilities"));
            pokemon.setAbilities(abilities);
            
            // Moves
            PokemonMoves moves = getPokemonMoves (pokemonRoot.get("moves"));
            pokemon.setMoves(moves);

            // ----------------------------Species JSON-----------------------------------------
            String speciesJSON = getPokemonSpeciesJSON(id);
            JsonNode speciesRoot = objMapper.readTree(speciesJSON);

            // Generation
            String generation = speciesRoot.get("generation").get("name").asText();
            String number = generation.split("-")[1];
            int genNum = StringUtils.getNumberFromRomanNumeral(number);
            pokemon.setGeneration(genNum);

            // Category
            String category = speciesRoot.get("genera").get(7).get("genus").asText();
            pokemon.setCategory(category);
            
            // Description
            String description = getDescription(speciesRoot.get("flavor_text_entries"));
            pokemon.setDescription(description);

            // ---------------------------------Evolution JSON-------------------------------
            String evolutionURL = objMapper.readTree(speciesJSON).get("evolution_chain").get("url").asText();
            String evolutionJSON =  webClient.getRequestJSON(evolutionURL);
            JsonNode evolutionRoot = objMapper.readTree(evolutionJSON);

            // Evolution Chain
            Set<EvolutionChain> evolutionChain = getEvolutionChain(evolutionRoot.get("chain"), evolutionRoot.get("id").asInt());
            pokemon.setEvolutionChain(evolutionChain);

            // ----------------------------Location JSON-----------------------------
            String locationsURL = objMapper.readTree(pokemonJSON).get("location_area_encounters").asText();
            String locationJSON =  webClient.getRequestJSON(locationsURL);
            JsonNode locationRoot = objMapper.readTree(locationJSON);

            // Locations/Versions
            List<Location> locations = getLocation(locationRoot);
            pokemon.setLocationVersions(locations);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return pokemon;
    }

    /**
     * 
     * @param node
     * @return
     */
    private String[] getTypes (JsonNode node) {
        int size = node.size();
        String[] types = new String[size];
        for (int i = 0; i < size; i++) {
            types[i] = node.get(i).get("type").get("name").asText();
        }
        return types;
    }

    /**
     * 
     * @param node
     * @return
     */
    private PokemonStats getBaseStats (int id, JsonNode node) {
        PokemonStats baseStats = new PokemonStats();
        baseStats.setId(id);
        for (JsonNode jsonNode : node) {
            String baseStatName = jsonNode.get("stat").get("name").asText();
            int value = jsonNode.get("base_stat").asInt();
            switch (baseStatName) {
                case "hp":
                    baseStats.setHp(value);
                    break;
                case "attack":
                    baseStats.setAttack(value);
                    break;
                case "defense":
                    baseStats.setDefense(value);
                    break;
                case "special-attack":
                    baseStats.setSpecialAttack(value);
                    break;
                case "special-defense":
                    baseStats.setSpecialDefense(value);
                    break;
                case "speed":
                    baseStats.setSpeed(value);
                    break;
                default:
                    break;
            }
        }
        return baseStats;
    }

    /**
     * 
     * @param node
     * @return
     */
    private Set<Ability> getAbilities (JsonNode node) {
        Set<Ability> abilities = new HashSet<>();
        for (JsonNode jsonNode : node) {
            Ability newAbility = new Ability(
                jsonNode.get("ability").get("name").asText(),
                jsonNode.get("ability").get("url").asText(),
                jsonNode.get("slot").asInt(),
                jsonNode.get("is_hidden").asBoolean()
            );
            abilities.add(newAbility);
        }
        return abilities;
    }

    /**
     * 
     * @param node
     * @return
     */
    private PokemonMoves getPokemonMoves (JsonNode node) {
        Set<Move> levelMoves = new HashSet<>();
        Set<Move> eggMoves = new HashSet<>();
        Set<Move> tutorMoves = new HashSet<>();
        Set<Move> machineMoves = new HashSet<>();
        Set<Move> otherMoves = new HashSet<>();
        for (JsonNode jsonNode : node) {
            Move move = new Move();

            move.setName(jsonNode.get("move").get("name").asText());
            move.setUrl(jsonNode.get("move").get("url").asText());

            for (JsonNode versionGroupDetailsNode : jsonNode.get("version_group_details")) {
                move.setTypeOfMove(versionGroupDetailsNode.get("move_learn_method").get("name").asText());
                move.setLevelLearnedAt(versionGroupDetailsNode.get("level_learned_at").asInt());

                switch (move.getTypeOfMove()) {
                    case ("level-up"):
                        levelMoves.add(move);
                        break;
                    case ("egg"):
                        eggMoves.add(move);
                        break;
                    case ("tutor"):
                        tutorMoves.add(move);
                        break;
                    case ("machine"):
                        machineMoves.add(move);
                        break;
                    default:
                        otherMoves.add(move);
                        break;
                }
            }
        }
        return new PokemonMoves(1, levelMoves, eggMoves, tutorMoves, machineMoves, otherMoves);
    }

    /**
     * 
     * @param flavorTextEntriesNode
     * @return
     */
    private String getDescription (JsonNode flavorTextEntriesNode) {
        String description = "No Description";
        int flavorTextSize = flavorTextEntriesNode.size();
        for (int i = 0; i < flavorTextSize; i++) {
            JsonNode currentNode = flavorTextEntriesNode.get(i);
            String languageName = currentNode.get("language").get("name").asText();

            // String uses equals instead of == so it won't be false
            // Grab the first English flavor text description
            if (languageName.equals("en")) {
                description = flavorTextEntriesNode.get(i).get("flavor_text").asText();
                description = description.replace("\f", " ");
                description = description.replace("\n", " ");
                break;
            }
        }
        return description;
    }

    /**
     * 
     * @param evolutionChainNode
     * @return
     */
    private Set<EvolutionChain> getEvolutionChain (JsonNode evolutionChainNode, int id) {
        Set<EvolutionChain> evolutionChain = new HashSet<>();
        do {
            EvolutionChain chain = new EvolutionChain();
            chain.setId(id);
            String speciesName = evolutionChainNode.get("species").get("name").asText();
            speciesName = StringUtils.convertFromURIFormat(speciesName);
            chain.setName(speciesName);
            String speciesURL = evolutionChainNode.get("species").get("url").asText();
            chain.setUrl(speciesURL);
            evolutionChain.add(chain);
            evolutionChainNode = evolutionChainNode.get("evolves_to").get(0);
        } while (evolutionChainNode != null);

        return evolutionChain;
    }

    /**
     * 
     * @param locationRoot
     * @return
     */
    private List<Map<String, String>> getLocationVersions (JsonNode locationRoot) {
        List<Map<String,String>> locationVersions = new ArrayList<>();
        for (JsonNode locationNode : locationRoot) {
            // Location
            String locationName = locationNode.get("location_area").get("name").asText();
            locationName = StringUtils.convertFromURIFormat(locationName);

            String locationURL = locationNode.get("location_area").get("url").asText();
            
            // Versions
            JsonNode versionNodes = locationNode.get("version_details");
            for (JsonNode versionNode : versionNodes) {
                Map<String, String> versionMap = new HashMap<>();

                // Location Name
                versionMap.put ("locationName", locationName);
                
                // URL
                versionMap.put ("locationURL", locationURL);

                // Encounter Method
                JsonNode encounterNode = versionNode.get("encounter_details");
                Set<String> encounterSet = new HashSet<>();
                
                for (JsonNode encounter : encounterNode) {
                    encounterSet.add(encounter.get("method").get("name").asText());
                }
                versionMap.put ("methods", encounterSet.toString());

                // Max Chance
                String maxChance = versionNode.get("max_chance").asText() + "%";
                versionMap.put("maxChance", maxChance);

                // Version
                String version = versionNode.get("version").get("name").asText();
                versionMap.put("versionName", version);

                locationVersions.add(versionMap);
            }
        }
        return locationVersions;
    }

    /**
     * 
     * @param locationRoot
     * @return
     */
    private List<Location> getLocation (JsonNode locationRoot) {
        List<Location> locationVersions = new ArrayList<>();
        for (JsonNode locationNode : locationRoot) {
            // Location
            String locationName = locationNode.get("location_area").get("name").asText();
            locationName = StringUtils.convertFromURIFormat(locationName);

            String locationUrl = locationNode.get("location_area").get("url").asText();
            
            // Versions
            JsonNode versionNodes = locationNode.get("version_details");
            for (JsonNode versionNode : versionNodes) {
                Location location = new Location();

                // Location Name
                location.setName(locationName);
                
                // URL
                location.setUrl(locationUrl);

                // Encounter Method
                JsonNode encounterNode = versionNode.get("encounter_details");
                Set<String> encounterSet = new HashSet<>();
                
                for (JsonNode encounter : encounterNode) {
                    encounterSet.add(encounter.get("method").get("name").asText());
                }
                location.setMethods(encounterSet);

                // Max Chance
                int maxChance = versionNode.get("max_chance").asInt();
                location.setMaxChance(maxChance);

                // Version
                String versionName = versionNode.get("version").get("name").asText();
                location.setVersionName(versionName);

                locationVersions.add(location);
            }
        }
        return locationVersions;
    }
}
