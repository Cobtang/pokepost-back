package com.revature.pokemondb.models;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Barry Norton
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pokemon_fanart")
public class Fanart {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "pokemon_id")
	private Pokemon pokemon;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserMini author;
	private String title;
	private String tags;
	@Column(name = "image")
	private String url;
	private Integer likes;
	private Integer reports;
	@Column(name = "is_flagged")
	private Boolean isFlagged;
	@Column(name="uploaded_at")
	private Date postDate;
	
	/*Overrides*/
	
	@Override
	public int hashCode() {
		return Objects.hash(author, id, isFlagged, likes, pokemon, postDate, reports,
				tags, title, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fanart other = (Fanart) obj;
		return author == other.author && id == other.id	&& Objects.equals(isFlagged, other.isFlagged)
				&& Objects.equals(likes, other.likes) && pokemon == other.pokemon
				&& Objects.equals(reports, other.reports) 
				&& Objects.equals(tags, other.tags)	&& Objects.equals(title, other.title) 
				&& Objects.equals(url, other.url);
	}

	@Override
	public String toString() {
		return "Fanart [id=" + id + ", pokemon=" + pokemon + ", author=" + author + ", title=" + title + ", tags="
				+ tags + ", url=" + url + ", likes=" + likes + ", reports=" + reports + ", isFlagged=" + isFlagged
				+ ", postDate=" + postDate + "]";
	}
}
