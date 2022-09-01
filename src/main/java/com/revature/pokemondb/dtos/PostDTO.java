package com.revature.pokemondb.dtos;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.revature.pokemondb.models.Post;
import com.revature.pokemondb.models.UserMini;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
	
	private long id;
	private String text;
	private String imageUrl;
	private Set<PostDTO> comments;
	private UserMini author;
	private Set<UserMini> users;
	private Timestamp postedAt = new Timestamp(System.currentTimeMillis());
	
	/**
	 * Convert Post into a DTO
	 * @param post
	 */
	public PostDTO(Post post) {
		setId(post.getId());
		setText(post.getText());
		setImageUrl(post.getImageUrl());
		setComments(new HashSet<>());
		for (Post comment : post.getComments()) {
			this.comments.add(new PostDTO(comment));
		}
		this.author = post.getAuthor();
		setPostedAt(post.getPostedAt());
	}
}