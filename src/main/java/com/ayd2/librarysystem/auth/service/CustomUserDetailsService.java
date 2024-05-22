package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.auth.model.UserModelDetails;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.StudentRepository;
import com.ayd2.librarysystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                                .orElse(null));

        if (Objects.isNull(user)) {
            try{
                var academicNumber = Long.parseLong(usernameOrEmail);
                user = studentRepository.findByAcademicNumber(academicNumber)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
            catch (NumberFormatException e) {
                throw new UsernameNotFoundException("User not found");
            }
        }

        return new UserModelDetails(user);
    }
}
