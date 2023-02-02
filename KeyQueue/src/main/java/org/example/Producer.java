package org.example;
public class Producer implements Runnable {
    private final MessageManager messageManager;
    private final int id;

    public Producer(MessageManager messageManager, int id) {
        this.messageManager = messageManager;
        this.id = id;
    }

    @Override
    public void run() {
        int count = 0;
        while (count < 10) {
            Message message = new Message(id, "Message " + count);

            messageManager.addMessage(message);

            System.out.println("Producer " + id + " added message " + count);


            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
