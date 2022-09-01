package com.revature.pokemondb.dtos;

import java.sql.Timestamp;

import com.revature.pokemondb.models.BannedUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannedUserDTO {
	private Long userId = 0l; 
	private Timestamp banDuration;
	private String banReason;

    public BannedUserDTO(BannedUser user) {
        this.userId = user.getUserId();
        this.banDuration = user.getBanDuration();
        this.banReason = user.getBanReason();
    }
}
