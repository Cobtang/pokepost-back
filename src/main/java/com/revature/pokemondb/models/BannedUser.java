package com.revature.pokemondb.models;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "banned_users", schema = "pokemon_db")
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

    public BannedUser() {
        userId = 0l;
        banDuration = Timestamp.from(Instant.now());
        banReason = "";
    }

    public BannedUser(Long userId) {
        this.userId = userId;
    }

    public BannedUser(Long userId, Timestamp banDuration, String banReason) {
        this.userId = userId;
        this.banDuration = banDuration;
        this.banReason = banReason;
    }

    public BannedUser(User user) {
        this.userId = user.getUserId();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
