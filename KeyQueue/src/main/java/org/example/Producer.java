package org.example;
public class Producer implements Runnable {
    private final KeyEvent keyEvent;

    public Producer(KeyEvent keyEvent, int producerId) {
        this.keyEvent = keyEvent;
    }

    @Override
    public void run() {
        try {

            generateMessages();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void generateMessages() throws InterruptedException {

        for(int i = 0; i < 10; i++){

            Message message = new Message(i, "Message " + i);
            keyEvent.addEvent(message);

        }
    }
}
