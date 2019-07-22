package cs446.budgetMe.Email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {

    public EmailService() { }

    EmailConfiguration emailConfiguration = new EmailConfiguration();

    public JavaMailSender emailSender = emailConfiguration.getJavaMailSender();

    public void sendRegistrationEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);

        String subject = "BudgetMe Registration Successfully";
        message.setSubject(subject);

        String text = "Thank you for your registration of BudgetMe. \nYour account has now been activated.";
        message.setText(text);
        emailSender.send(message);
    }
}
