package com.revature.pokemondb.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "abilities")
public class Ability {
    @Id
    private String name;
    private String url;
    private int slot;
    private boolean isHidden;

    @Override
    public String toString() {
        return "Ability [name=" + name + ", url=" + url + ", isHidden=" + isHidden
                + ", slot=" + slot + "]";
    }
}
