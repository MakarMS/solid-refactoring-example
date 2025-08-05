package com.makar;

class SendingException extends Exception {
    public SendingException(String message) {
        super(message);
    }
}

interface INotifier {
    void send(String message) throws SendingException;
}

interface IEmailConnection {
    void connect();

    void send(String message) throws IllegalArgumentException;

    void disconnect();
}

class SMTPConnection implements IEmailConnection {
    public void connect() {
        System.out.println("Connecting to email server...");
    }

    public void send(String message) {
        if (message.length() > 1000) {
            throw new IllegalArgumentException("Message too large");
        }
        System.out.println("Sending EMAIL: " + message);
    }

    public void disconnect() {
        System.out.println("Disconnecting from email server...");
    }
}

class EmailNotifier implements INotifier {

    private final IEmailConnection connection;

    public EmailNotifier(IEmailConnection connection) {
        this.connection = connection;
    }

    public void send(String message) throws SendingException {
        this.connection.connect();
        try {
            this.connection.send(message);
        } catch (IllegalArgumentException e) {
            throw new SendingException("Message not sent, because: " + e.getMessage());
        } finally {
            this.connection.disconnect();
        }
    }
}

class WebhookNotifier implements INotifier {
    public void send(String message) {
        System.out.println("Sending webhook with message: " + message);
    }
}

class NotificationSender {
    public void sendNotification(INotifier notifier, String message) throws SendingException {
        notifier.send(message);
    }
}

public class Main {
    public static void main(String[] args) {
        NotificationSender sender = new NotificationSender();

        try {
            sender.sendNotification(new EmailNotifier(new SMTPConnection()), "Welcome!");
            sender.sendNotification(new WebhookNotifier(), "Webhook message here.");
            sender.sendNotification(new EmailNotifier(new SMTPConnection()), "A".repeat(1001));
        } catch (SendingException e) {
            System.out.println(e.getMessage());
        }
    }
}
