package com.revature.pokemondb.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.revature.pokemondb.dtos.PostDTO;
import com.revature.pokemondb.models.UserMini;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String text;
	private String imageUrl;
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Post> comments;
	@ManyToOne
	private UserMini author;
	@ManyToMany
	@JoinTable(name = "liked_posts", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<UserMini> users;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime postedAt;
	@Column(name = "is_flagged")
	private Boolean isFlagged;

	public Post(PostDTO dto) {
		this.id = dto.getId();
		this.text = dto.getText();
		this.imageUrl = dto.getImageUrl();
		this.comments = new HashSet<>();
		for(PostDTO postDto: dto.getComments()) {
			this.comments.add(new Post(postDto));	
		}

		this.author = dto.getAuthor();
		this.users = dto.getUsers();
		this.postedAt = dto.getPostedAt();
	}
}
