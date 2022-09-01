package com.revature.pokemondb.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.revature.pokemondb.dtos.PokemonDTO;
import com.revature.pokemondb.models.UserMini;
import com.revature.pokemondb.models.Pokemon;
import com.revature.pokemondb.models.User;
import com.revature.pokemondb.models.Wishlist;
import com.revature.pokemondb.repositories.WishlistRepository;
import com.revature.pokemondb.repositories.PokemonRepository;
import com.revature.pokemondb.repositories.UserRepository;

@Service
public class WishlistService {
    private WishlistRepository listRepo;

    public WishlistService(WishlistRepository listRepo) {
        this.listRepo = listRepo;
    }

    // add pokemon to wish list
    public Wishlist addPokemonToWishlist(Wishlist pokemonid) {
        return listRepo.save(pokemonid);
    }

    // delete pokemon from wish list
    public boolean deletePokemonFromWishlist(int pokemonid, int userid) {
        try {
            Wishlist wishlistdelete = listRepo.findByUserIdAndPokemonId(userid, pokemonid);
            listRepo.delete(wishlistdelete);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //
    public List<Wishlist> getAllWishlists() {
        return listRepo.findAll();
    }

    public List<Wishlist> findByUserId(long id) {
        return listRepo.findByUser(new UserMini (id));
    }
}
