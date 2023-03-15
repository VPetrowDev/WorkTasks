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

        try{

            while (true) {

            Message message = null;

            message = keyEvent.myQueue.peek();

            if (keyEvent.mySet.size() == 1 && keyEvent.mySet.contains(Message.POISON_PILL)) {

                break;

            } else if(message != null && message.isProcessing()){

                continue;
            } else if(message != null){

                message.markProcessing();
            }

            if (message != null) {
                keyEvent.removeEvent(message);
                System.out.println("Consumer " + consumerId + " removed event " + message.getMessage());
                message.markProcessed();
            }


            }
        } catch (InterruptedException e) {
                throw new RuntimeException(e);

        }
    }
}

