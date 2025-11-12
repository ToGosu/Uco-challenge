package co.edu.uco.ucochallenge.secondary.adapters.notification;

import co.edu.uco.ucochallenge.config.NotificationApiProperties;
import com.notificationapi.NotificationApi;
import com.notificationapi.model.EmailOptions;
import com.notificationapi.model.NotificationRequest;
import com.notificationapi.model.SmsOptions;
import com.notificationapi.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationApiService {

    private final NotificationApi api;

    public NotificationApiService(NotificationApiProperties properties) {
        this.api = new NotificationApi(properties.getClientId(), properties.getClientSecret());
    }

    public boolean sendNotification(
            String notificationId,
            String userId,
            String email,
            String phone,
            Map<String, String> mergeTags
    ) {
        try {
            User user = new User(userId);
            if (email != null) {
                user.setEmail(email);
            }
            if (phone != null) {
                user.setNumber(phone);
            }

            NotificationRequest request = new NotificationRequest(notificationId, user);

            if (email != null) {
                EmailOptions emailOptions = new EmailOptions()
                        .setSubject(mergeTags.getOrDefault("subject", "Notificación UCO Challenge"))
                        .setHtml(mergeTags.getOrDefault("html",
                                "<p>Hola " + mergeTags.getOrDefault("name", "usuario") + "!</p>"));

                request.setEmail(emailOptions);
            }

            if (phone != null) {
                SmsOptions smsOptions = new SmsOptions()
                        .setMessage(mergeTags.getOrDefault("message",
                                "Hola " + mergeTags.getOrDefault("name", "usuario") + ", bienvenido a UCO Challenge"));
                request.setSms(smsOptions);
            }

            System.out.println("Enviando notificación a NotificationAPI...");
            String response = api.send(request);
            System.out.println("Notificación enviada. Response: " + response);

            return true;

        } catch (Exception e) {
            System.err.println("Error enviando notificación con NotificationAPI: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmail(String notificationId, String userId, String email, Map<String, String> variables) {
        return sendNotification(notificationId, userId, email, null, variables);
    }

    public boolean sendSms(String notificationId, String userId, String phone, Map<String, String> variables) {
        return sendNotification(notificationId, userId, null, phone, variables);
    }

    public boolean sendEmailAndSms(
            String notificationId,
            String userId,
            String email,
            String phone,
            Map<String, String> variables
    ) {
        return sendNotification(notificationId, userId, email, phone, variables);
    }
}
