package com.makar;

interface INotifier {
    void connect();

    void send(String message);

    void disconnect();
}

class EmailNotifier implements INotifier {
    public void connect() {
        System.out.println("Connecting to email server...");
    }

    public void send(String message) {
        if (message.length() > 160) {
            throw new IllegalArgumentException("Message too long!");
        }
        System.out.println("Sending EMAIL: " + message);
    }

    public void disconnect() {
        System.out.println("Disconnecting from email server...");
    }
}

class WebhookNotifier implements INotifier {
    public void connect() {
        throw new UnsupportedOperationException("WebhookNotifier does not support connect()");
    }

    public void send(String message) {
        System.out.println("Sending webhook with message: " + message);
    }

    public void disconnect() {
        throw new UnsupportedOperationException("WebhookNotifier does not support disconnect()");
    }
}

class NotificationSender {
    public void sendNotification(INotifier notifier, String message) {
        if (notifier instanceof EmailNotifier) {
            notifier.connect();
            notifier.send(message);
            notifier.disconnect();
        } else if (notifier instanceof WebhookNotifier) {
            notifier.send(message);
        } else {
            System.out.println("Unsupported notification type");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        NotificationSender sender = new NotificationSender();
        sender.sendNotification(new EmailNotifier(), "Welcome!");
        sender.sendNotification(new WebhookNotifier(), "Webhook message here.");
    }
}
