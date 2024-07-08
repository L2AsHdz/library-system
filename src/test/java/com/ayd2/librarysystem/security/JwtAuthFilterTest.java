package com.ayd2.librarysystem.security;

import com.ayd2.librarysystem.auth.model.UserModelDetails;
import com.ayd2.librarysystem.auth.service.CustomUserDetailsService;
import com.ayd2.librarysystem.auth.service.JwtService;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private UserModelDetails userDetails;

    @BeforeEach
    void setUp() {
        var user = UserModel.builder()
                .id(1L)
                .username("username")
                .password("password")
                .userRole(Rol.ADMIN)
                .status((short) 1)
                .build();

        userDetails = new UserModelDetails(user);
    }

    @Test
    public void itShouldDoFilterInternal() throws ServletException, IOException, IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserName("token")).thenReturn("username");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(request, never()).setAttribute(any(), any());
        verify(response, never()).sendError(anyInt(), anyString());
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    public void itShouldDoFilterInternal_WithEmptyAuthorizationHeader() throws ServletException, IOException {
        // Mocking an empty authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Invoking the doFilterInternal method
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifying that no authentication-related methods were called
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).extractUserName(anyString());
        verify(jwtService, never()).isTokenValid(anyString(), any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void itShouldDoFilterInternal_WithInvalidToken() throws ServletException, IOException {
        // Mocking an invalid token
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.extractUserName("invalidToken")).thenReturn(null);

        // Invoking the doFilterInternal method
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifying that no authentication-related methods were called
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtService, times(1)).extractUserName("invalidToken");
        verify(jwtService, never()).isTokenValid(anyString(), any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void itShouldDoFilterInternal_WithExpiredToken() throws ServletException, IOException {
        // Mocking an expired token
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredToken");
        when(jwtService.extractUserName("expiredToken")).thenReturn("username");
        when(userService.loadUserByUsername("username")).thenReturn(userDetails);
        when(jwtService.isTokenValid("expiredToken", userDetails)).thenReturn(false);

        // Invoking the doFilterInternal method
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifying that authentication-related methods were called appropriately
        verify(userService, times(1)).loadUserByUsername("username");
        verify(jwtService, times(1)).isTokenValid("expiredToken", userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    public void itShouldDoFilterInternal_WithNullAuthentication() throws ServletException, IOException {
        // Mocking a valid token, but no authentication context set
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUserName("validToken")).thenReturn("username");
        when(userService.loadUserByUsername("username")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(true);
        SecurityContextHolder.clearContext();

        // Invoking the doFilterInternal method
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifying that authentication-related methods were called appropriately
        verify(userService, times(1)).loadUserByUsername("username");
        verify(jwtService, times(1)).isTokenValid("validToken", userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }

}