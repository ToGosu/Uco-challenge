package co.edu.uco.ucochallenge.secondary.adapters.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * DTO para requests a NotificationAPI
 */
public class NotificationApiRequest {

    @JsonProperty("notificationId")
    private String notificationId;

    @JsonProperty("user")
    private UserInfo user;

    @JsonProperty("mergeTags")
    private Map<String, String> mergeTags;

    public NotificationApiRequest() {
    }

    public NotificationApiRequest(String notificationId, UserInfo user, Map<String, String> mergeTags) {
        this.notificationId = notificationId;
        this.user = user;
        this.mergeTags = mergeTags;
    }

    // Getters y Setters
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Map<String, String> getMergeTags() {
        return mergeTags;
    }

    public void setMergeTags(Map<String, String> mergeTags) {
        this.mergeTags = mergeTags;
    }

    public static class UserInfo {
        @JsonProperty("id")
        private String id;

        @JsonProperty("email")
        private String email;

        @JsonProperty("number")
        private String number;

        public UserInfo() {
        }

        public UserInfo(String id, String email, String number) {
            this.id = id;
            this.email = email;
            this.number = number;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
