package org.example;
public class Main {
    public static boolean isRunning = true;

    public static void main(String[] args){
        MessageManager messageManager = new MessageManager();

        Thread producer1 = new Thread(new Producer(messageManager, 1));
        Thread producer2 = new Thread(new Producer(messageManager, 2));

        producer1.start();
        producer2.start();

        Thread consumer1 = new Thread(new Consumer(messageManager, 1));
        Thread consumer2 = new Thread(new Consumer(messageManager, 2));

        consumer1.start();
        consumer2.start();

    }
}
