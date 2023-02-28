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

            if (keyEvent.mySet.size() == 1 && keyEvent.mySet.contains(Message.POISON_PILL)) {
                break;
            }

            Message message = null;
            try {
                message = keyEvent.myQueue.peek();

                if(message != null) {
                    keyEvent.removeEvent(message);
                    System.out.println("Consumer " + consumerId + " removed event " + message.getMessage());
                }


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

