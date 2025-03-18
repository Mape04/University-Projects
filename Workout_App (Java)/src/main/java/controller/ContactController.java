package controller;

import Domain.ContactMessage;
import Service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout_app")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/contact")
    public ResponseEntity<String> handleContactForm(@RequestBody ContactMessage contactMessage) {
        boolean isSent = contactService.sendContactMessage(contactMessage);

        if (isSent) {
            return ResponseEntity.ok("Message sent successfully!");
        } else {
            return ResponseEntity.status(500).body("Failed to send the message. Please try again.");
        }
    }
}
