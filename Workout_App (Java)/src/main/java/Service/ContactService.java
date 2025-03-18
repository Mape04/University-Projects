package Service;

import Domain.ContactMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendContactMessage(ContactMessage contactMessage) {
        try {
            // Create a new email message
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setFrom(contactMessage.getEmail()); // Sender's email
            emailMessage.setTo("equniox446@gmail.com"); // Recipient's email
            emailMessage.setSubject("Contact Message from " + contactMessage.getName() + " email: " +contactMessage.getEmail());
            emailMessage.setText(contactMessage.getMessage());

            // Send the email
            mailSender.send(emailMessage);

            // Return true if email is sent successfully
            return true;
        } catch (Exception e) {
            // Log the error if sending the email fails
            e.printStackTrace();
            return false;
        }
    }
}
