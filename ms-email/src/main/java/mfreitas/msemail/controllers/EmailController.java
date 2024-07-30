package mfreitas.msemail.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

import jakarta.validation.Valid;
import mfreitas.msemail.models.Email;
import mfreitas.msemail.services.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid Email email) throws MailjetException, MailjetSocketTimeoutException {
        emailService.sendEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
