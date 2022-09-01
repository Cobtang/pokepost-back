package com.revature.pokemondb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.pokemondb.dtos.LikeRequest;
import com.revature.pokemondb.dtos.PostDTO;
import com.revature.pokemondb.exceptions.RecordNotFoundException;
import com.revature.pokemondb.models.Post;
import com.revature.pokemondb.models.User;
import com.revature.pokemondb.models.UserMini;
import com.revature.pokemondb.services.PostService;
import com.revature.pokemondb.services.UserService;

@RestController
@RequestMapping("/post")
public class PostController {

	private final PostService postService;
	private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
    
    
    /** 
     * @return ResponseEntity<List<PostDTO>>
     */
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
    	List<PostDTO> listDto = new ArrayList<>();
    	for(Post post : this.postService.getAll()) {
    		listDto.add(new PostDTO(post));
    	}
    	return ResponseEntity.ok(listDto);
    }
    
    
    /** 
     * @param post
     * @return ResponseEntity<Post>
     * @throws RecordNotFoundException
     */
    @PutMapping
    public ResponseEntity<PostDTO> upsertPost(@RequestBody PostDTO post) throws RecordNotFoundException {
    	User author = userService.getUserById(post.getAuthor().getId());
    	if (author == null) {
        	return ResponseEntity.badRequest().build();
        }
    	UserMini authMini = new UserMini(author);
        post.setAuthor(authMini);
        Post upsertPost = new Post(post);
        this.postService.upsert(upsertPost);
    	return ResponseEntity.ok(post);
    }
    
    
    /** 
     * @param like
     * @return ResponseEntity<PostDTO>
     * @throws RecordNotFoundException
     */
    @PutMapping("/like")
    public ResponseEntity<PostDTO> likePost(@RequestBody LikeRequest like) throws RecordNotFoundException {
    	User user = userService.getUserById(like.getUserId());
    	Post post = postService.getPost(like.getPostId());
    	if (user == null || post == null) {
    		return ResponseEntity.badRequest().build();
    	}
        Set<UserMini> users = post.getUsers();
        users.add(new UserMini(user));
        post.setUsers(users);
        PostDTO postDto = new PostDTO(postService.upsert(post));
        return ResponseEntity.ok(postDto);
    }
    
    
    /** 
     * @param unlike
     * @return ResponseEntity<PostDTO>
     * @throws RecordNotFoundException
     */
    @PutMapping("/unlike")
    public ResponseEntity<PostDTO> unlikePost(@RequestBody LikeRequest unlike) throws RecordNotFoundException {
    	try {
            User user = userService.getUserById(unlike.getUserId());
            Post post = postService.getPost(unlike.getPostId());
            Set<UserMini> users = post.getUsers();
            users.remove(new UserMini(user));
            post.setUsers(users);
            PostDTO postDto = new PostDTO(postService.upsert(post));
            return ResponseEntity.ok(postDto);
        } catch (RecordNotFoundException e) {
            throw new RecordNotFoundException(e);
        }
    }
    
    @GetMapping("/get/{userId}")
    public ResponseEntity<List<PostDTO>> getUsersPosts(@PathVariable("userId") long userId) {
    	List<Post> posts = postService.getUserPosts(userId);
    	List<PostDTO> postsDto = posts.stream()
    	        .map(PostDTO::new)
    	        .collect(Collectors.toList());
    	return ResponseEntity.ok(postsDto);
    }

}
