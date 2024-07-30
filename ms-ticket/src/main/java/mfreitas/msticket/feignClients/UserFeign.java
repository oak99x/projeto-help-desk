package mfreitas.msticket.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import mfreitas.msticket.configs.FeignConfig;
import mfreitas.msticket.models.User;
@FeignClient(name = "ms-user", path = "/api/user", configuration = FeignConfig.class)
public interface UserFeign {

    @GetMapping
    public ResponseEntity<User> getUserById();

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email);
}