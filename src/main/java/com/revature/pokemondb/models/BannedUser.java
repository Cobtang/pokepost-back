package com.revature.pokemondb.models;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.revature.pokemondb.dtos.BannedUserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "banned_users")
public class BannedUser {
    @Id
	@Column(name="user_id", updatable=false, insertable=false)
	private Long userId = 0l; 

    @Column(name="ban_duration")
	private Timestamp banDuration;

    @Column(name="ban_reason")
	private String banReason;

    @Column(name="banned_at")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Timestamp bannedAt = new Timestamp(System.currentTimeMillis());

    public BannedUser(Long userId) {
        this.userId = userId;
    }

    public BannedUser(BannedUserDTO dto) {
        this.userId = dto.getId();
        this.banDuration = dto.getBanDuration();
        this.banReason = dto.getBanReason();
    }

    public BannedUser(User user) {
        this.userId = user.getId();
    }

    public Long getId() {
        return userId;
    }

    public void setId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getBanDuration() {
        return banDuration;
    }

    public void setBanDuration(Timestamp banDuration) {
        this.banDuration = banDuration;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public Timestamp getBannedAt () {
        return bannedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((banDuration == null) ? 0 : banDuration.hashCode());
        result = prime * result + ((banReason == null) ? 0 : banReason.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BannedUser other = (BannedUser) obj;
        if (banDuration == null) {
            if (other.banDuration != null)
                return false;
        } else if (!banDuration.equals(other.banDuration))
            return false;
        if (banReason == null) {
            if (other.banReason != null)
                return false;
        } else if (!banReason.equals(other.banReason))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }
}
