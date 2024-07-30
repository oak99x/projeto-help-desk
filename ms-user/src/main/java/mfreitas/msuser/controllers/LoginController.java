package mfreitas.msuser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mfreitas.msuser.dtos.UserLoginDTO;
import mfreitas.msuser.services.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService authService;

    @GetMapping("/{email}")
    public ResponseEntity<UserLoginDTO> getUserByEmail(@PathVariable String email) {
        UserLoginDTO user = authService.getUserByEmail(email);
        return ResponseEntity.ok().body(user);
    }
}
