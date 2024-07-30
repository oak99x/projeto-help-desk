package mfreitas.msticket.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mfreitas.msticket.models.Email;

@FeignClient(name = "ms-email", path = "/api/email")
public interface EmailFeign {

    @PostMapping("/sendEmail")
    public ResponseEntity<Email> sendEmail(@RequestBody Email emailDTO);
    
}
