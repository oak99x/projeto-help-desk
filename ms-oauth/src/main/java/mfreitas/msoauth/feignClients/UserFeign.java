package mfreitas.msoauth.feignClients;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import mfreitas.msoauth.models.User;

@FeignClient(name = "ms-user", path = "/api/login")
public interface UserFeign {

    @GetMapping("/{email}")
    ResponseEntity<User> getUserByEmail(@PathVariable String email);
    
}
