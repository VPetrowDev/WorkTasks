package org.example;

public class Consumer implements Runnable {
    private final KeyEvent keyEvent;
    private final int consumerId;

    public Consumer(KeyEvent keyEvent, int consumerId) {
        this.keyEvent = keyEvent;
        this.consumerId = consumerId;
    }

    @Override
    public void run() {
        while (true) {

            Message message = keyEvent.myQueue.peek();
            keyEvent.removeEvent(message);

            if (message != null) {
                System.out.println("Consumer " + consumerId + " got message: " + message.getMessage());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

