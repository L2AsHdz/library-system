package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.UserModel;
import io.jsonwebtoken.Claims;
import com.ayd2.librarysystem.user.model.enums.Rol;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private String jwtSigningKey = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
    private Integer tokenValidTime = 15;

    private UserModel user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", jwtSigningKey);
        ReflectionTestUtils.setField(jwtService, "tokenValidTime", tokenValidTime);

        user = UserModel.builder()
                .id(1L)
                .fullName("fullName")
                .username("username")
                .email("email")
                .userRole(Rol.LIBRARIAN)
                .password("password")
                .status((short)1)
                .build();

    }

    @Test
    public void testGenerateToken() throws Exception {
        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull();
        assertThat(jwtService.extractUserName(token)).isEqualTo(user.getUsername());
    }

    @Test
    public void testBuildClaimsStudent() throws Exception {
        StudentModel student = new StudentModel();
        student.setId(1L);
        student.setUsername("username");
        student.setFullName("fullName");
        student.setEmail("email");
        student.setPassword("password");
        student.setStatus((short)1);
        student.setUserRole(Rol.STUDENT);
        student.setAcademicNumber(23413414L);
        student.setCareerModel(CareerModel.builder().id(1L).name("career").build());
        student.setIsSanctioned(false);

        Map<String, Object> claims = jwtService.buildClaims(student);

        assertThat(claims).isNotNull();
        assertThat(claims.get("id")).isEqualTo(student.getId());
        assertThat(claims.get("user")).isEqualTo(student.getUsername());
        assertThat(claims.get("rol")).isEqualTo(Rol.STUDENT);
    }

    @Test
    public void testIsTokenValid() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(user.getUsername());

        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    public void testExtractUserName() throws Exception {
        String token = jwtService.generateToken(user);

        assertThat(jwtService.extractUserName(token)).isEqualTo(user.getUsername());
    }

    @Test
    public void testExtractClaim() throws Exception {
        String token = jwtService.generateToken(user);

        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertThat(expiration).isAfter(new Date());
    }

}