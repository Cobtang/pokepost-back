package com.revature.pokemondb.repositories;

import org.springframework.stereotype.Repository;

import com.revature.pokemondb.dtos.ArtCommDTO;
import com.revature.pokemondb.dtos.UserIdDTO;
import com.revature.pokemondb.models.ReportArtComm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author
 *
 */
@Repository
public interface ReportArtCommRepository extends JpaRepository<ReportArtComm, Integer>{
	/**
	 * Retrieve comment ratings with the given comment Id and author
	 * @param commDtoObj
	 * @param userDtoObj
	 * @return
	 */
	List<ReportArtComm> findByCommentIdAndAuthor(ArtCommDTO commDtoObj, UserIdDTO userDtoObj);
	
}
