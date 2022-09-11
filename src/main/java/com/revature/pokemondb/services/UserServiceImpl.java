package com.revature.pokemondb.services;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.pokemondb.exceptions.BannedException;
import com.revature.pokemondb.exceptions.EmailAlreadyExistsException;
import com.revature.pokemondb.exceptions.FailedAuthenticationException;
import com.revature.pokemondb.exceptions.InvalidInputException;
import com.revature.pokemondb.exceptions.RecordNotFoundException;
import com.revature.pokemondb.exceptions.UsernameAlreadyExistsException;
import com.revature.pokemondb.models.BannedUser;
import com.revature.pokemondb.models.User;
import com.revature.pokemondb.repositories.BanRepository;
import com.revature.pokemondb.repositories.UserRepository;
import com.revature.pokemondb.utils.SecurityUtils;

@Service("userService")
public class UserServiceImpl implements UserService {
	private UserRepository userRepo;
	private BanRepository banRepo;
	private SecurityUtils securityUtils;

	public UserServiceImpl(UserRepository repo, BanRepository banRepo, SecurityUtils utils) {
		this.userRepo = repo;
		this.banRepo = banRepo;
		this.securityUtils = utils;
	}

	/**
	 * Retrieves a user by its ID.
	 * 
	 * @param id
	 * @return an Optional User is returned
	 */
	public User getUserById(long id) throws RecordNotFoundException {
		Optional<User> user = userRepo.findById(id);
		if (user.isEmpty())
			throw new RecordNotFoundException();
		return user.get();
	}

	/**
	 * Retrieves a user by its username.
	 * 
	 * @param username
	 * @return a User is returned
	 */
	public User getUserByUsername(String username) throws RecordNotFoundException {
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isEmpty())
			throw new RecordNotFoundException();
		return user.get();
	}

	/**
	 * Returns all users from the database.
	 * 
	 * @return
	 */
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	/**
	 * A user is retrieved from the database if it matches the
	 * username parameter. If password matches then the user is
	 * returned. If either of these fails, null is returned.
	 * A token should be generated for authentication.
	 * 
	 * @throws BannedException
	 */
	public User loginUser(String username, String password)
			throws RecordNotFoundException, FailedAuthenticationException, NoSuchAlgorithmException, BannedException {
		Optional<User> oUser = userRepo.findByUsername(username);
		if (oUser.isEmpty())
			throw new RecordNotFoundException();
		User user = oUser.get();
		String dbPass = user.getPassword();
		byte[] dbSalt = user.getSalt();
		String encodedPassword = password;
		if (dbSalt != null) {
			try {
				encodedPassword = securityUtils.encodePassword(encodedPassword, dbSalt);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				throw new NoSuchAlgorithmException();
			}
		}

		// If password does not match, then throw a FailedAuthenticationException.
		if (!dbPass.equals(encodedPassword))
			throw new FailedAuthenticationException();

		Optional<BannedUser> oBannedUser = banRepo.findById(user.getId());

		// Is user not in the ban table? If so, return user.
		if (oBannedUser.isEmpty())
			return user;

		BannedUser bannedUser = oBannedUser.get();
		Timestamp banDuration = bannedUser.getBanDuration();

		// Has user's ban duration expired?
		if (banDuration.getTime() < Timestamp.from(Instant.now()).getTime()) {
			unBanUser(user.getId());
			return user;
		}

		// Nope lol stay banned
		throw new BannedException();
	}

	/**
	 * Inserts the user into the database. Throws an exception if username/email
	 * already exists.
	 * 
	 * @throws InvalidInputException
	 */
	public User registerUser(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException,
			NoSuchAlgorithmException, InvalidInputException {
		// Does the username already exist in the database
		if (userRepo.existsByUsername(user.getUsername())) {
			throw new UsernameAlreadyExistsException();
		}

		// Does user's email already exist in the database
		if (userRepo.existsByEmail(user.getEmail())) {
			throw new EmailAlreadyExistsException();
		}

		// Is the password null or empty?
		if (user.getPassword() == null || user.getPassword().equals("")) {
			throw new InvalidInputException();
		}

		// Encode the password with the salt
		byte[] salt;
		try {
			salt = securityUtils.generateSalt();
			String encodedPassword = securityUtils.encodePassword(user.getPassword(), salt);
			user.setPassword(encodedPassword);
			user.setSalt(salt);

			// Save the user to the database
			return userRepo.save(user);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new NoSuchAlgorithmException();
		}
	}

	/**
	 * Updates the user. Throws a RecordNotFound exception if the user is
	 * not in the database. Returns the same object that was passed in if
	 * successful.
	 * 
	 * @param user
	 * @return
	 * @throws RecordNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws EmailAlreadyExistsException
	 * @throws UsernameAlreadyExistsException
	 */
	public User updateUser(User user) throws RecordNotFoundException, NoSuchAlgorithmException,
			EmailAlreadyExistsException, UsernameAlreadyExistsException {
		Optional<User> oUser = userRepo.findById(user.getId());
		if (oUser.isEmpty())
			throw new RecordNotFoundException();
		User dbUser = oUser.get();
		// Username
		// If usernames are different and if it already exists in the database, throw an
		// UsernameAlreadyExistsException.
		if (!dbUser.getUsername().equals(user.getUsername()) && userRepo.existsByUsername(user.getUsername())) {
			throw new UsernameAlreadyExistsException();
		}

		// Email
		// If emails are different and if it already exists in the database, throw an
		// EmailAlreadyExistsException.
		if (!dbUser.getEmail().equals(user.getEmail()) && userRepo.existsByEmail(user.getEmail())) {
			throw new EmailAlreadyExistsException();
		}

		// Password
		// Make sure password is not the same, empty, or null.
		if (!user.getPassword().equals(dbUser.getPassword()) && !user.getPassword().equals("")
				&& user.getPassword() != null) {
			byte[] salt = securityUtils.generateSalt();
			dbUser.setSalt(salt);
			String encodedPassword = securityUtils.encodePassword(user.getPassword(), dbUser.getSalt());
			dbUser.setPassword(encodedPassword);
		}

		// Role
		// Make sure role is not the same, empty, or null.
		if (!user.getRole().equals(dbUser.getRole()) && !user.getRole().equals("") && user.getRole() != null)
			dbUser.setRole(user.getRole());

		// Update user in DB
		userRepo.save(dbUser);
		return dbUser;
	}

	/**
	 * Removes the user from the database. Throws a RecordNotFound
	 * exception if the user is not in the database. Returns the
	 * same object that was passed in if successful.
	 * 
	 * @param user
	 * @return
	 * @throws RecordNotFoundException
	 */
	public User deleteUser(User user) throws RecordNotFoundException {
		Optional<User> oUser = userRepo.findById(user.getId());
		if (oUser.isEmpty())
			throw new RecordNotFoundException();
		User dbUser = oUser.get();
		userRepo.delete(dbUser);
		return dbUser;
	}

	/**
	 * Inserts the user into the ban table to indicate
	 * a user has been banned.
	 * 
	 * @throws UsernameAlreadyExistsException
	 * @throws RecordNotFoundException
	 */
	public User banUser(BannedUser bannedUser) throws UsernameAlreadyExistsException, RecordNotFoundException {
		Optional<User> oUser = userRepo.findById(bannedUser.getId());
		if (oUser.isEmpty())
			throw new RecordNotFoundException();
		User dbUser = oUser.get();

		// If user is already banned throw a UsernameAlreadyExistsException
		if (banRepo.existsById(dbUser.getId()))
			throw new UsernameAlreadyExistsException();

		banRepo.save(bannedUser);
		return dbUser;
	}

	/**
	 * Removes a user from the ban table to indicate
	 * a user has been unbanned.
	 * 
	 * @throws RecordNotFoundException
	 */
	public User unBanUser(long id) throws RecordNotFoundException {
		Optional<BannedUser> oBannedUser = banRepo.findById(id);
		if (oBannedUser.isEmpty())
			throw new RecordNotFoundException();
		Optional<User> oUser = userRepo.findById(id);

		// If user is not in the users table
		if (oUser.isEmpty())
			throw new RecordNotFoundException();

		banRepo.delete(oBannedUser.get());
		return oUser.get();
	}
}
