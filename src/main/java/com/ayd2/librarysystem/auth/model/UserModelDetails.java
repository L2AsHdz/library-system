package com.ayd2.librarysystem.auth.model;

import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserModelDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Rol rol;
    private final Boolean status;

    private Long academicNumber = 0L;
    private Long CareerId = 0L;
    private Boolean isSanctioned = false;

    public UserModelDetails(UserModel user) {
        username = user.getUsername();
        password = user.getPassword();
        rol = user.getUserRole();
        status = user.getStatus() == 1;

        if (user instanceof StudentModel studentModel) {
            academicNumber = studentModel.getAcademicNumber();
            CareerId = studentModel.getCareerModel().getId();
            isSanctioned = studentModel.getIsSanctioned();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}
