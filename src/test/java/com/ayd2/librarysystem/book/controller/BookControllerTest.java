package com.ayd2.librarysystem.book.controller;

import com.ayd2.librarysystem.AbstractMvcTest;
import com.ayd2.librarysystem.book.model.BookModel;
import com.ayd2.librarysystem.book.model.dto.BookRequestDto;
import com.ayd2.librarysystem.book.model.dto.BookResponseDto;
import com.ayd2.librarysystem.book.service.BookService;
import com.ayd2.librarysystem.career.controller.CareerController;
import com.ayd2.librarysystem.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {BookController.class, GlobalExceptionHandler.class})
@ExtendWith(MockitoExtension.class)
class BookControllerTest extends AbstractMvcTest {

    @MockBean
    private BookService bookService;

    private BookModel testBook;
    private BookRequestDto testBookRequestDto;

    @BeforeEach
    public void setUp() {
        testBook = BookModel.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .publicationDate(LocalDate.parse("2021-01-01"))
                .publisher("Test Publisher")
                .stock(10L)
                .build();
    }

    @Test
    void getAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(testBook.toRecord()));

        ResultActions result = mockMvc.perform(get("/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String response = r.getResponse().getContentAsString();
                    assertThat(response).isEqualTo(objectMapper.writeValueAsString(List.of(testBook.toRecord())));
                });
    }

    @Test
    void getBookById() throws Exception {
        when(bookService.getBookById(testBook.getId())).thenReturn(testBook.toRecord());

        ResultActions result = mockMvc.perform(get("/v1/books/" + testBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String response = r.getResponse().getContentAsString();
                    assertThat(response).isEqualTo(objectMapper.writeValueAsString(testBook.toRecord()));
                });
    }

    @Test
    void createBook() throws Exception {
        createBookRequestDto();

        when(bookService.createBook(testBookRequestDto)).thenReturn(testBook.toRecord());

        ResultActions result = mockMvc.perform(post("/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookRequestDto))
                .with(csrf()));

        result.andExpect(status().isCreated())
                .andExpect((r) -> {
                    String response = r.getResponse().getContentAsString();
                    assertThat(response).isEqualTo(objectMapper.writeValueAsString(testBook.toRecord()));
                });
    }

    @Test
    void updateBook() throws Exception {
        createBookRequestDto();

        when(bookService.updateBook(testBook.getId(), testBookRequestDto)).thenReturn(testBook.toRecord());

        ResultActions result = mockMvc.perform(put("/v1/books/" + testBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookRequestDto))
                .with(csrf()));

        result.andExpect(status().isOk());
    }

    @Test
    void updateStock() throws Exception {
        testBook.setStock(15L);

        when(bookService.updateStock(testBook.getId(), 15L)).thenReturn(testBook.toRecord());

        ResultActions result = mockMvc.perform(put("/v1/books/stock/" + testBook.getId() + "/15")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));


        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String response = r.getResponse().getContentAsString();
                    assertThat(response).isEqualTo(objectMapper.writeValueAsString(testBook.toRecord()));
                });
    }

    private void createBookRequestDto() {
        testBookRequestDto = new BookRequestDto(
                testBook.getTitle(),
                testBook.getAuthor(),
                testBook.getPublicationDate(),
                testBook.getPublisher()
        );
    }
}