package mfreitas.msuser.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import mfreitas.msuser.dtos.UpdateUserDTO;
import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUserById(HttpServletRequest request) {
        UUID userId = UUID.fromString(request.getHeader("X-User-Id"));
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(HttpServletRequest request, @RequestBody UpdateUserDTO userDTO) {  
        UUID userId = UUID.fromString(request.getHeader("X-User-Id"));
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok().body(updatedUser);
    }
}
