package com.api.springpoems;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.springpoems.dto.user.ChangeEmailData;
import com.api.springpoems.dto.user.RegisterUserData;
import com.api.springpoems.dto.user.ShowProfileData;
import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;
import com.api.springpoems.enums.Gender;
import com.api.springpoems.infra.exceptions.ValidationException;
import com.api.springpoems.infra.security.TokenService;
import com.api.springpoems.repositories.CommentRepository;
import com.api.springpoems.repositories.PoemRepository;
import com.api.springpoems.repositories.UserRepository;
import com.api.springpoems.services.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserTests {
    @InjectMocks
    private UserService userService;

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PoemRepository poemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Pageable mockPageable;

    @Test
    public void testRegisterUser() {
        RegisterUserData userData = new RegisterUserData("username", "email", "passW0RD!", "passW0RD!");
        assertEquals(userData.password(), userData.confirmPassword());
        User userMock = new User(userData);

        when(userRepository.findByUsername(userData.username())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(userMock);

        User registeredUser = userService.registerUser(userData);

        assertNotNull(registeredUser);
        assertEquals(userData.username(), registeredUser.getUsername());
    }

    @Test
    public void testRegisterUserUsernameExists() {
        RegisterUserData data = new RegisterUserData("username", "email", "passW0RD!", "passW0RD!");
        
        when(userRepository.findByUsername(data.username())).thenReturn(new User());
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(data);
        });
    }

    @Test
    public void testChangeEmail() {
        User userMock = new User();
        String newEmail = "newemail@example.com";
        ChangeEmailData data = new ChangeEmailData(newEmail);
        
        when(userRepository.save(userMock)).thenReturn(userMock);
        
        userService.changeEmail(data, userMock);
        
        assertEquals(newEmail, userMock.getEmail());
        verify(userRepository, times(1)).save(userMock);
    }

    @Test
    public void testGetProfileDataAuthenticatedUser() {
        User authenticatedUser = new User();
        authenticatedUser.setUsername("authenticatedUser");
        authenticatedUser.setEmail("authenticatedUser@example.com");
        
        ShowProfileData profileData = userService.getProfileData(null, authenticatedUser);
        
        assertNotNull(profileData);
        assertEquals(authenticatedUser.getUsername(), profileData.username());
        assertEquals(authenticatedUser.getEmail(), profileData.email());
    }

    @Test
    public void testGetProfileDataByUsername() {
        User userMock = new User();
        userMock.setUsername("username");

        Mockito.when(userRepository.findByUsernameAndActiveTrue(userMock.getUsername())).thenReturn(userMock);

        ShowProfileData profileData = userService.getProfileData("username", null);

        assertNotNull(profileData);
        assertEquals(userMock.getUsername(), profileData.username());
    }

    @Test
    public void testGetProfileDataByUsernameNotFound() {
        String username = "nonExistingUser";
        
        assertThrows(ValidationException.class, () -> {
            userService.getProfileData(username, null);
        });
    }

    @Test
    public void testGetProfileDataNoInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getProfileData(null, null);
        });
    }

    @Test
    public void testUserList() {
        User user = new User(
            1L, 
            "testUser", 
            "password", 
            "user@email",
            LocalDate.now(),
            Gender.M,
            "test",
            "user",
            "it's a test user",
            LocalDate.of(1990, 5, 15),
            true
        );

        Page<User> usersPage = new PageImpl<>(List.of(user));
        Mockito.when(userRepository.findAllByActiveTrue(mockPageable)).thenReturn(usersPage);
        
        Page<ShowProfileData> result = userService.userList(mockPageable);
        
        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(1, result.getContent().size());

        ShowProfileData userProfile = result.getContent().get(0);
        assertEquals("testUser", userProfile.username());
        assertEquals("user@email", userProfile.email());
        assertEquals(Gender.M, userProfile.gender());
        assertEquals("test", userProfile.firstName());
        assertEquals("user", userProfile.lastName());
        assertEquals("it's a test user", userProfile.description());
        assertEquals(LocalDate.of(1990, 5, 15), userProfile.birthday());
    }

    @Test
    public void testDeleteUser() {
        String username = "username";
        User userMock = new User(1L, username, "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);

        Mockito.lenient().when(userRepository.findByUsernameAndActiveTrue(username)).thenReturn(userMock);

        Poem poemMock = new Poem();
        poemMock.setId(1L);
        poemMock.setTitle("Test Poem");
        poemMock.setActive(true);
        poemMock.setAuthor(userMock);

        Mockito.when(poemRepository.findAllByAuthorAndActiveTrue(userMock)).thenReturn(List.of(poemMock));

        Comment commentMock = new Comment();
        commentMock.setId(1L);
        commentMock.setContent("Test Comment");
        commentMock.setActive(true);
        commentMock.setAuthor(userMock);

        Mockito.when(commentRepository.findAllByAuthorAndActiveTrue(userMock)).thenReturn(List.of(commentMock));
        
        userService.deleteUser(userMock);

        assertFalse(userMock.getActive());
        assertFalse(poemMock.getActive());
        assertFalse(commentMock.getActive());
        verify(userRepository, times(1)).save(userMock);
        verify(poemRepository, times(1)).saveAll(List.of(poemMock)); 
        verify(commentRepository, times(1)).saveAll(List.of(commentMock)); 
    }
}
