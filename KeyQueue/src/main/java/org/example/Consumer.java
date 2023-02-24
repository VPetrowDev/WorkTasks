package org.example;

public class Consumer implements Runnable {
    private final KeyEvent keyEvent;
    private final int consumerId;


    public Consumer(KeyEvent keyEvent, int consumerId) {
        this.keyEvent = keyEvent;
        this.consumerId = consumerId;
        keyEvent.incrementAndGetNumConsumers();
    }

    @Override
    public void run() {
        while (true) {
            Message message = null;
            try {
                message = keyEvent.myQueue.take();
                if (message == Message.POISON_PILL) {

                    System.out.println("Consumer " + consumerId + " terminating...");
                    keyEvent.decrementAndGetNumConsumers();
                    return;

                }
                keyEvent.removeEvent(message);
                System.out.println("Consumer " + consumerId + " removed event " + message.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

