package com.denisborovkov.models;

public class Notification {

        private String username;
        private String message;

        public Notification() {}

        public Notification(String username, String message) {
            this.username = username;
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Notification[" +
                    "username=" + username + ", " +
                    "message=" + message + ']';
        }
}
