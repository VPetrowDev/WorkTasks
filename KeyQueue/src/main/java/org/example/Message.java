package org.example;

import java.util.Objects;

public class Message {
    private final int key;
    private final String message;
    private volatile boolean processing = false;
    public static final Message POISON_PILL = new Message(-1, "POISON_PILL");
    public Message(int key, String message) {
        this.key = key;
        this.message = message;
    }

    public synchronized void markProcessing(){
        this.processing = true;
    }
    public synchronized void markProcessed(){
        this.processing = false;
    }
    public synchronized boolean isProcessing(){
        return processing;
    }
    @Override
    public String toString() {
        return "Message{" +
                "id=" + key +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return key == message1.key && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, message);
    }
}
