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

import com.api.springpoems.dto.comment.ListCommentsData;
import com.api.springpoems.dto.comment.SendCommentData;
import com.api.springpoems.dto.comment.ShowCommentData;
import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;
import com.api.springpoems.infra.exceptions.ValidationException;
import com.api.springpoems.repositories.CommentRepository;
import com.api.springpoems.repositories.PoemRepository;
import com.api.springpoems.services.CommentService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CommentTests {
    @InjectMocks
    private CommentService service;

    @Mock
    private PoemRepository poemRepository;

    @Mock
    private CommentRepository commentRepository;

    @MockBean
    private Pageable mockPageable;

    @Test
    public void testGetComment() {
        User mockAuthor = new User(1L, "userTest", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);
        Poem mockPoem = new Poem(1L, "poemTitle", "poemContent", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);
        Comment mockComment = new Comment(1L, "content", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, mockPoem,true);
        
        Mockito.when(commentRepository.findByIdAndActiveTrue(mockComment.getId())).thenReturn(mockComment);

        ShowCommentData response = service.getComment(mockComment.getId());

        assertNotNull(response);
        assertEquals(mockComment.getContent(), response.content());

        Mockito.verify(commentRepository).findByIdAndActiveTrue(mockComment.getId());
    }

    @Test
    public void testGetUserComments() {
        User mockAuthor = new User(1L, "userTest", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);
        Poem mockPoem1 = new Poem(1L, "poemTitle1", "poemContent2", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);
        Poem mockPoem2 = new Poem(2L, "poemTitle1", "poemContent2", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);
        Comment mockComment1 = new Comment(1L, "content1", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, mockPoem1,true);
        Comment mockComment2 = new Comment(2L, "content2", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, mockPoem2,true);
        
        List<Comment> comments = List.of(mockComment1, mockComment2);

        Mockito.when(mockPageable.getPageSize()).thenReturn(10);
        Mockito.when(mockPageable.getSort()).thenReturn(Sort.by("lastUpdate").ascending());

        Mockito.when(
            commentRepository.findAllByAuthorAndActiveTrue(mockAuthor, mockPageable)
        ).thenReturn(new PageImpl<>(comments));

        Page<ListCommentsData> response = service.getUserComments(mockAuthor, mockPageable);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.hasContent(), "Response page should have content");
    }

    @Test
    public void testGetPoemComments() {
        User mockAuthor1 = new User(1L, "userTest1", "password", "email1@email.com", LocalDate.now(), null, null, null, null, null, true);
        User mockAuthor2 = new User(2L, "userTest2", "password", "email2@email.com", LocalDate.now(), null, null, null, null, null, true);
        Poem mockPoem = new Poem(1L, "poemTitle", "poemContent", LocalDateTime.now(), LocalDateTime.now(), mockAuthor1, true);
        Comment mockComment1 = new Comment(1L, "content1", LocalDateTime.now(), LocalDateTime.now(), mockAuthor1, mockPoem,true);
        Comment mockComment2 = new Comment(2L, "content2", LocalDateTime.now(), LocalDateTime.now(), mockAuthor2, mockPoem,true);
        
        List<Comment> comments = List.of(mockComment1, mockComment2);

        Mockito.when(mockPageable.getPageSize()).thenReturn(10);
        Mockito.when(mockPageable.getSort()).thenReturn(Sort.by("lastUpdate").ascending());

        Mockito.when(poemRepository.findByIdAndActiveTrue(mockPoem.getId())).thenReturn(mockPoem);
        Mockito.when(
            commentRepository.findAllByPoemAndActiveTrue(mockPoem, mockPageable)
        ).thenReturn(new PageImpl<>(comments));

        Page<ListCommentsData> response = service.getPoemComments(mockPoem.getId(), mockPageable);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.hasContent(), "Response page should have content");
    }

    @Test
    public void testCreateComment() {
        SendCommentData commentData = new SendCommentData( "content");
        User mockAuthor = new User();
        Poem mockPoem = new Poem(1L, "poemTitle", "poemContent", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);

        service.create(commentData, mockAuthor, mockPoem.getId());
        Mockito.verify(commentRepository).save(Mockito.any(Comment.class));
    }

    @Test
    public void testUpdateComment() {
        Long commentId = 1L;
        User mockAuthor = new User();
        Comment mockComment = new Comment();

        Mockito.when(commentRepository.findByIdAndAuthorAndActiveTrue(commentId, mockAuthor)).thenReturn(mockComment);

        SendCommentData updateData = new SendCommentData("new_content");

        service.update(mockAuthor, commentId, updateData);

        assertEquals(updateData.content(), mockComment.getContent());

        Mockito.verify(commentRepository).findByIdAndAuthorAndActiveTrue(commentId, mockAuthor);
        Mockito.verify(commentRepository).save(mockComment);
    }

    @Test
    public void testDeleteComment() {
        User mockAuthor = new User(1L, "userTest", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);
        Poem mockPoem = new Poem(1L, "poemTitle", "poemContent", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, true);
        Comment mockComment = new Comment(1L, "content", LocalDateTime.now(), LocalDateTime.now(), mockAuthor, mockPoem,true);

        Mockito.when(commentRepository.findByIdAndActiveTrue(mockComment.getId())).thenReturn(mockComment);

        service.delete(mockComment.getId(), mockAuthor);

        Mockito.verify(commentRepository).save(mockComment);

        assertFalse(mockComment.getActive());
    }

    @Test
    public void testDeleteCommentUnauthorizedUser() {
        User unauthorizedUser = new User(2L, "userTest", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);

        User authorizedUser = new User(1L, "userTest2", "password", "email@email.com", LocalDate.now(), null, null, null, null, null, true);
        Poem mockPoem = new Poem(1L, "poemTitle", "poemContent", LocalDateTime.now(), LocalDateTime.now(), authorizedUser, true); 

        Comment mockComment = new Comment(1L, "content", LocalDateTime.now(), LocalDateTime.now(), authorizedUser, mockPoem,true);

        Mockito.when(commentRepository.findByIdAndActiveTrue(1L)).thenReturn(mockComment);

        assertThrows(ValidationException.class, () -> service.delete(1L, unauthorizedUser));
    }
}
