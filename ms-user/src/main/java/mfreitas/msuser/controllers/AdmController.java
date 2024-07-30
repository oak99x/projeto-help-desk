package mfreitas.msuser.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.dtos.UserFilter;
import mfreitas.msuser.services.AdmService;

@RestController
@RequestMapping("/api/admin")
public class AdmController {

    @Autowired
    private AdmService admService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = admService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/name/{name}")
    public ResponseEntity<List<UserDTO>> getUsersByName(@PathVariable String name) {
        List<UserDTO> users = admService.getUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<List<UserDTO>> getUsersByEmail(@PathVariable String email) {
        List<UserDTO> users = admService.getUsersByEmail(email);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/role/{role}")
    public List<UserDTO> getUsersByRole(@PathVariable String role) {
        return admService.getUsersByRole(role);
    }

    @PostMapping("/add-user")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDto) {
        UserDTO newUser = admService.addUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/promoteToAdmin/{email}")
    public ResponseEntity<UserDTO> promoteToAdmin(@PathVariable String email) throws Exception {
        UserDTO user = admService.promoteToAdmin(email);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/deleteRoleAdmin/{email}")
    public ResponseEntity<UserDTO> deleteRoleAdmin(@PathVariable String email) throws Exception {
        UserDTO user = admService.deleteRoleAdmin(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserDTO>> filterTickets(HttpServletRequest request,
            // @RequestParam(required = false) boolean all,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String userEmail) {
        UserFilter userFilter = new UserFilter(area, role, userEmail);
        List<UserDTO> userList = admService.filterUsers(userFilter);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

}


