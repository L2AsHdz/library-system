package com.ayd2.librarysystem.user.controller;

import com.ayd2.librarysystem.AbstractMvcTest;
import com.ayd2.librarysystem.exception.GlobalExceptionHandler;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.dto.UserRequestUpdateDto;
import com.ayd2.librarysystem.user.model.dto.UserUpdatePasswordDto;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {UserController.class, GlobalExceptionHandler.class})
@ExtendWith(MockitoExtension.class)
public class UserControllerTest extends AbstractMvcTest {

    @MockBean
    private UserService userService;

    private UserModel testUser;
    private UserRequestUpdateDto testUserRequestUpdateDto;
    private UserUpdatePasswordDto testUserUpdatePasswordDto;

    @BeforeEach
    public void setUp() {
        testUser = UserModel.builder()
                .id(1L)
                .fullName("Test User")
                .username("testuser")
                .password("Prueba_1234")
                .userRole(Rol.LIBRARIAN)
                .email("testuser@email.com")
                .status((short)1)
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(testUser.toRecord()));

        ResultActions result = mockMvc.perform(get("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String json = r.getResponse().getContentAsString();
                    assertThat(json).isEqualTo(objectMapper.writeValueAsString(List.of(testUser.toRecord())));
                });
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getUserById() throws Exception {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser.toRecord());

        ResultActions result = mockMvc.perform(get("/v1/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String json = r.getResponse().getContentAsString();
                    assertThat(json).isEqualTo(objectMapper.writeValueAsString(testUser.toRecord()));
                });
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void updateUser() throws Exception {
        testUserRequestUpdateDto = new UserRequestUpdateDto(
                testUser.getFullName(),
                testUser.getUsername(),
                testUser.getEmail(),
                testUser.getBirthDate(),
                testUser.getUserRole().name()
        );

        when(userService.updateUser(testUser.getId(), testUserRequestUpdateDto)).thenReturn(testUser.toRecord());

        ResultActions result = mockMvc.perform(put("/v1/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserRequestUpdateDto))
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void updatePassword() throws Exception {
        var newPassword = "newPassword_123";
        testUser.setPassword(newPassword);
        testUserUpdatePasswordDto = new UserUpdatePasswordDto(newPassword);

        when(userService.updatePassword(testUser.getId(), testUserUpdatePasswordDto)).thenReturn(testUser.toRecord());

        ResultActions result = mockMvc.perform(put("/v1/users/update-password/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserUpdatePasswordDto))
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String json = r.getResponse().getContentAsString();
                    assertThat(json).isEqualTo(objectMapper.writeValueAsString(testUser.toRecord()));
                });
    }
}