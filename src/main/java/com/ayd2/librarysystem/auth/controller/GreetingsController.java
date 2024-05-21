package com.ayd2.librarysystem.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/greetings")
@RequiredArgsConstructor
public class GreetingsController {

    @GetMapping("welcome")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Welcome to Library System");
    }

    @GetMapping("hello-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> helloAdmin(){
        return ResponseEntity.ok("Hello Admin");
    }


    @GetMapping("hello-student")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<String> helloStudent(){
        return ResponseEntity.ok("Hello Student");
    }

    @GetMapping("hello-librarian")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<String> helloLibrarian(){
        return ResponseEntity.ok("Hello Librarian");
    }

}
