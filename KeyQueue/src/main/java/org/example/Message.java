package org.example;

public class Message {
    private final int id;
    private final String message;

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
