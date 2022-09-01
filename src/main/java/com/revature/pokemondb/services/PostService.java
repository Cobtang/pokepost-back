package com.revature.pokemondb.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.pokemondb.exceptions.RecordNotFoundException;
import com.revature.pokemondb.models.Post;
import com.revature.pokemondb.repositories.PostRepository;

@Service
public class PostService {

	private PostRepository postRepository;
	
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	
	/** 
	 * @param id
	 * @return Post
	 * @throws RecordNotFoundException
	 */
	public Post getPost(long id) throws RecordNotFoundException {
		Optional<Post> postOpt = postRepository.findById(id);
		if (postOpt.isPresent()) {
			return postOpt.get();
		}
		throw new RecordNotFoundException();
	}

	
	/** 
	 * @return List<Post>
	 */
	public List<Post> getAll() {
		return this.postRepository.findNonCommentPosts();
	}

	
	/** 
	 * @param post
	 * @return Post
	 */
	public Post upsert(Post post) {
		return this.postRepository.save(post);
	}

	/** 
	 * Get all the posts from a user.
	 * @param id
	 * @return List<Post>
	 */
	public List<Post> getUserPosts(long id) {
		return this.postRepository.findAllByAuthorId(id);
	}
}
