package com.api.springpoems;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.Sort;

import com.api.springpoems.dto.poem.ListAuthorPoemsData;
import com.api.springpoems.dto.poem.SendPoemData;
import com.api.springpoems.dto.poem.ShowPoemData;
import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;
import com.api.springpoems.infra.exceptions.ValidationException;
import com.api.springpoems.repositories.CommentRepository;
import com.api.springpoems.repositories.PoemRepository;
import com.api.springpoems.repositories.UserRepository;
import com.api.springpoems.services.PoemService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PoemTests {
    @InjectMocks
    private PoemService service;

    @Mock
    private PoemRepository poemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @MockBean
    private Pageable mockPageable;

    @Test
    public void testGetPoem() {
        Long id = 1L;
        User mockAuthor = new User();
        Poem mockPoem = new Poem(id, "title", "content", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);
        
        Mockito.when(poemRepository.findByIdAndActiveTrue(id)).thenReturn(mockPoem);

        ShowPoemData response = service.getPoem(id);

        assertNotNull(response);
        assertEquals(mockPoem.getTitle(), response.title());
        assertEquals(mockPoem.getContent(), response.content());

        Mockito.verify(poemRepository).findByIdAndActiveTrue(id);
    }

    @Test
    public void testListPoem() {
        User mockAuthor = new User(1L, "userTest", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);
        Poem mockPoem1 = new Poem(1L, "poemTitle1", "poemContent1", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);
        Poem mockPoem2 = new Poem(2L, "poemTitle2", "poemContent2", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);
        List<Poem> poems = List.of(mockPoem1, mockPoem2);

        Mockito.when(mockPageable.getPageSize()).thenReturn(10);
        Mockito.when(mockPageable.getSort()).thenReturn(Sort.by("title").ascending());

        Mockito.when(userRepository.findByUsernameAndActiveTrue(mockAuthor.getUsername())).thenReturn(mockAuthor);
        Mockito.when(
            poemRepository.findAllByAuthorAndActiveTrue(mockAuthor, mockPageable)
        ).thenReturn(new PageImpl<>(poems));


        Page<ListAuthorPoemsData> response = service.getAuthorPoems(mockAuthor.getUsername(), mockPageable);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.hasContent(), "Response page should have content");
    }

    @Test
    public void testCreatePoem() {
        SendPoemData poemData = new SendPoemData("title", "content");
        User mockAuthor = new User();

        service.create(poemData, mockAuthor);
        Mockito.verify(poemRepository).save(Mockito.any(Poem.class));
    }

    @Test
    public void testUpdatePoem() {
        Long poemId = 1L;
        User mockAuthor = new User();
        Poem mockPoem = new Poem();
        Mockito.when(poemRepository.findByIdAndAuthorAndActiveTrue(poemId, mockAuthor)).thenReturn(mockPoem);

        SendPoemData updateData = new SendPoemData("new_title", "new_content");

        service.update(mockAuthor, poemId, updateData);

        assertEquals(updateData.title(), mockPoem.getTitle());
        assertEquals(updateData.content(), mockPoem.getContent());

        Mockito.verify(poemRepository).findByIdAndAuthorAndActiveTrue(poemId, mockAuthor);
        Mockito.verify(poemRepository).save(mockPoem);
    }

    @Test
    public void testDeletePoem() {
        Long poemId = 1L;
        User mockAuthor = new User();
        Poem mockPoem = new Poem();
        List<Comment> mockComments = List.of(new Comment(), new Comment());
        for (Comment comment : mockComments) {
            comment.setPoem(mockPoem);
        }

        Mockito.when(poemRepository.findByIdAndAuthorAndActiveTrue(poemId, mockAuthor)).thenReturn(mockPoem);
        Mockito.when(commentRepository.findAllByPoemAndActiveTrue(mockPoem)).thenReturn(mockComments);

        service.delete(poemId, mockAuthor);

        Mockito.verify(poemRepository).findByIdAndAuthorAndActiveTrue(poemId, mockAuthor);
        Mockito.verify(poemRepository).save(mockPoem);
        Mockito.verify(commentRepository).findAllByPoemAndActiveTrue(mockPoem);
        Mockito.verify(commentRepository).saveAll(mockComments);

        assertFalse(mockPoem.getActive());
        for (Comment comment : mockComments) {
            assertFalse(comment.getActive());
        }
    }

    @Test
    public void testDeletePoemUnauthorizedUser() {
        User unauthorizedUser = new User(2L, "userTest", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);

        User authorizedUser = new User(1L, "userTest2", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);
        Poem mockPoem = new Poem(1L, "poemTitle", "poemContent", LocalDateTime.now(), LocalDateTime.now(), authorizedUser, true); 


        Mockito.when(poemRepository.findByIdAndActiveTrue(1L)).thenReturn(mockPoem);

        assertThrows(ValidationException.class, () -> service.delete(1L, unauthorizedUser));
    }
}

