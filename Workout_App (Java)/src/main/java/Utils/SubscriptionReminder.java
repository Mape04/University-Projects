package Utils;

import Domain.AppUser;
import Service.EmailManager;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class SubscriptionReminder {

    @Autowired
    private UserService userService; // Service to fetch users
    @Autowired
    private EmailManager emailManager; // Email sending service

    @Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
    public void sendReminders() {
        System.out.println("Checking for expiring subscriptions...");

        // Get the current date and the date 5 days from now
        LocalDate today = LocalDate.now();
        LocalDate thresholdDate = today.plusDays(5);

        // Fetch all users
        Collection<AppUser> users = userService.getAllUsers();

        for (AppUser user : users) {
            if (user.getSubExpirationDate() != null) {
                LocalDate subExpirationDate = user.getSubExpirationDate().toLocalDate();

                // Check if the subscription expires in less than 5 days
                if (!subExpirationDate.isBefore(today) && subExpirationDate.isBefore(thresholdDate)) {
                    String subject = "Subscription Expiration Reminder";
                    String text = String.format(
                            "Hello %s,\n\nYour subscription is expiring on %s. Please renew it to continue enjoying our services.\n\nBest regards,\nEQUINOX Team",
                            user.getUsername(), subExpirationDate);

                    // Send email
                    emailManager.sendEmail(user.getEmail(), subject, text);
                }
            }
        }
    }
}
