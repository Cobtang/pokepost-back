package com.revature.pokemondb.models;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.pokemondb.dtos.BannedUserDTO;
import com.revature.pokemondb.dtos.UserBodyDTO;
import com.revature.pokemondb.dtos.UserDTO;

@SpringBootTest
public class UserTest {
    
    @Test
    void createDefaultUser () {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void createUserDTOToken() {
        User user = new User();
        UserDTO userDTO = new UserDTO(user, "token");
        userDTO.setToken("setToken");
        assertEquals("setToken", userDTO.getToken());
    }

    @Test
    void setUserDTO () {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1l);
        userDTO.setUsername("user");
        userDTO.setEmail("email");
        assertEquals(1l, userDTO.getId());
        assertEquals("user", userDTO.getUsername());
        assertEquals("email", userDTO.getEmail());
    }

    @Test
    void setUserBodyDTO () {
        Long expectedId = 1l;
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedPassword = "pass";
        byte[] expectedSalt = "pass".getBytes();
        String expectedRole = "user";
        String expectedToken = "token";
        UserBodyDTO user = new UserBodyDTO();
        user.setId(expectedId);
        user.setUsername(expectedUsername);
        user.setEmail(expectedEmail);
        user.setPassword(expectedPassword);
        user.setToken(expectedToken);
        user.setSalt(expectedSalt);
        assertEquals(user.getId(), expectedId);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
        assertEquals(user.getPassword(), expectedPassword);
        assertEquals(user.getSalt(), expectedSalt);
        assertEquals(user.getRole(), expectedRole);
        assertEquals(user.getToken(), expectedToken);
        User newUser = new User("lol", "email");
        UserDTO newUserDTO = new UserDTO(newUser);
        UserBodyDTO newBodyDTO = new UserBodyDTO(newUser);
        UserBodyDTO newBodyDTO2 = new UserBodyDTO(newUserDTO);
        assertNotNull(newBodyDTO);
        assertNotNull(newBodyDTO2);
    }

    @Test
    void createIdUser () {
        Long expectedId = 1l;
        User user = new User(expectedId);
        assertNotNull(user);
        assertEquals(user.getId(), expectedId);
    }

    @Test
    void createUsernameEmailUser () {
        String expectedUsername = "username";
        String expectedEmail = "email";
        User user = new User(expectedUsername, expectedEmail);
        assertNotNull(user);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
    }

    @Test
    void createUsernameEmailPasswordUser () {
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedPassword = "pass";
        User user = new User(expectedUsername, expectedEmail, expectedPassword);
        assertNotNull(user);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
        assertEquals(user.getPassword(), expectedPassword);
    }

    @Test
    void createIdUsernameEmailPasswordUser () {
        Long expectedId = 1l;
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedPassword = "pass";
        User user = new User(expectedId, expectedUsername, expectedEmail, expectedPassword);
        assertNotNull(user);
        assertEquals(user.getId(), expectedId);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
        assertEquals(user.getPassword(), expectedPassword);
    }

    @Test
    void createFullUser () {
        Long expectedId = 1l;
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedPassword = "pass";
        byte[] expectedSalt = "pass".getBytes();
        User user = new User(expectedId, expectedUsername, expectedEmail, expectedPassword, expectedSalt);
        assertNotNull(user);
        assertEquals(user.getId(), expectedId);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
        assertEquals(user.getPassword(), expectedPassword);
        assertEquals(user.getSalt(), expectedSalt);
    }

    @Test
    void createUserFromuser () {
        Long expectedId = 1l;
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedPassword = "pass";
        byte[] expectedSalt = "pass".getBytes();
        String expectedRole = "user";
        User user = new User(expectedId, expectedUsername, expectedEmail, expectedPassword, expectedSalt);
        user.setRole(expectedRole);
        assertNotNull(user);
        assertEquals(user.getId(), expectedId);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
        assertEquals(user.getPassword(), expectedPassword);
        assertEquals(user.getSalt(), expectedSalt);
        assertEquals(user.getRole(), expectedRole);
    }

    @Test
    void createUserFromDTO () {
        Long expectedId = 1l;
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedRole = "user";
        UserDTO userDTO = new UserDTO(expectedId, expectedUsername, expectedEmail);
        User user = new User(userDTO);
        user.setRole(expectedRole);
        assertNotNull(user);
        assertEquals(user.getId(), expectedId);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
        assertEquals(user.getRole(), expectedRole);
    }

    @Test
    void createUserFromMap () {
        Map<String, String> userMap = new HashMap<>();

        Long expectedId = 1l;
        userMap.put("userId", String.valueOf(expectedId));

        String expectedUsername = "username";
        userMap.put("username", expectedUsername);

        String expectedEmail = "email";
        userMap.put("email", expectedEmail);

        String expectedPassword = "pass";
        userMap.put("password", expectedPassword);

        byte[] expectedSalt = "pass".getBytes(StandardCharsets.UTF_8);
        userMap.put("salt", new String(expectedSalt, StandardCharsets.UTF_8));

        String expectedRole = "user";
        userMap.put("role", expectedRole);

        User user = new User(userMap);
        assertNotNull(user);
        assertEquals(user.getId(), expectedId);
        assertEquals(user.getUsername(), expectedUsername);
        assertEquals(user.getEmail(), expectedEmail);
        assertEquals(user.getPassword(), expectedPassword);
        assertEquals(new String(user.getSalt(), StandardCharsets.UTF_8), new String(expectedSalt, StandardCharsets.UTF_8));
        assertEquals(user.getRole(), expectedRole);
    }

    @Test
    void userHashCodeTest () {
        Long expectedId = 1l;
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedPassword = "pass";
        User user = new User(expectedId, expectedUsername, expectedEmail, expectedPassword);
        User user2 = new User(expectedId, expectedUsername, expectedEmail, expectedPassword);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void userEquals () {
        Long expectedId = 1l;
        String expectedUsername = "username";
        String expectedEmail = "email";
        String expectedPassword = "pass";
        User user = new User(expectedId, expectedUsername, expectedEmail, expectedPassword);
        User user2 = new User(expectedId, expectedUsername, expectedEmail, expectedPassword);
        assertEquals(user, user2);
    }

    @Test
    void bannedUserDTO () {
        BannedUser bannedUser = new BannedUser();
        BannedUserDTO dto = new BannedUserDTO(bannedUser);
        dto.setId(3l);
        assertEquals(3l, dto.getId());
        
        Timestamp now = Timestamp.from(Instant.now());
        dto.setBanDuration(now);
        assertEquals(now, dto.getBanDuration());

        String banReason = "lol";
        dto.setBanReason(banReason);
        assertEquals(banReason, dto.getBanReason());

        BannedUserDTO dto2 = new BannedUserDTO(bannedUser);
        BannedUserDTO dtoNull = new BannedUserDTO();
        dtoNull.setBanDuration(null);
        dtoNull.setBanReason(null);
        dtoNull.setId(null);
        assertNotEquals(dto, dto2);
        assertNotEquals(dto2, dto);
        assertNotEquals(dtoNull, dto2);
        assertNotEquals(dto2, dtoNull);

        dto2.setBanDuration(now);
        assertNotEquals(dto, dto2);
        assertNotEquals(dto2, dto);
        assertNotEquals(dtoNull, dto2);
        assertNotEquals(dto2, dtoNull);

        dto2.setBanReason(banReason);
        assertNotEquals(dto, dto2);
        assertNotEquals(dto2, dto);
        assertNotEquals(dtoNull, dto2);
        assertNotEquals(dto2, dtoNull);

        dto2.setId(3l);
        assertEquals(dto, dto2);
        assertEquals(dto2, dto);
        assertNotEquals(dtoNull, dto2);
        assertNotEquals(dto2, dtoNull);
    }
}
