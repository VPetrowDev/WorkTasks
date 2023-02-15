package org.example;
public class Producer implements Runnable {
    private final KeyEvent keyEvent;

    public Producer(KeyEvent keyEvent, int producerId) {
        this.keyEvent = keyEvent;
    }

    @Override
    public void run() {
        int count = 0;
        while (count < 10) {

            Message message = new Message(count, "Message " + count);

            keyEvent.addEvent(message);

            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
