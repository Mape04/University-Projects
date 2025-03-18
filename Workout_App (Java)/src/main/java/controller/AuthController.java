package controller;

import Domain.AppUser;
import Service.UserService;
import dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/workout_app/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody AppUser newUser) {
        String responseMessage = userService.signup(newUser);
        Map<String, String> response = new HashMap<>();
        response.put("message", responseMessage);
        if ("User registered successfully.".equals(responseMessage)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        // Attempt to log in the user
        String response = userService.login(loginRequest);

        if (response.startsWith("Login successful")) {
            // Retrieve the userId based on email
            String userId = userService.findByEmail(loginRequest.getEmail());

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "User not found."));
            }

            // Generate a token (you can replace this with a JWT implementation later)
            String token = UUID.randomUUID().toString();

            // Retrieve the admin status of the user
            AppUser user = userService.getUserById(Integer.parseInt(userId));
            Boolean isAdmin = (user != null) ? user.getAdmin() : false;

            // Store email in the session (if required, optional for token-based authentication)
            session.setAttribute("email", loginRequest.getEmail());

            // Prepare the structured JSON response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("userId", userId);
            responseBody.put("isAdmin", isAdmin);

            // Return the structured response
            return ResponseEntity.ok(responseBody);
        } else {
            // Return an error response if login failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", response));
        }
    }





    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return ResponseEntity.ok("Logout successful.");
    }
}
