package org.example;
public class Consumer implements Runnable {
    private final MessageManager messageManager;
    private final int id;

    public Consumer(MessageManager messageManager, int id) {
        this.messageManager = messageManager;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            Message message = messageManager.getMessage(id);
            if (message != null) {
                System.out.println("Consumer " + id + " got message: " + message.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

