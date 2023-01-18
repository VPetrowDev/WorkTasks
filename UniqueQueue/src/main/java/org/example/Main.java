package org.example;
public class Main {
    public static void main(String[] args) throws InterruptedException {

        UniqueEventsQueue<Person> uniqueEventsQueue = new UniqueEventsQueue<>();

        final int REPETITIONS = 5;

        Producer producer = new Producer(uniqueEventsQueue, REPETITIONS);
        Consumer consumer = new Consumer(uniqueEventsQueue, REPETITIONS);


        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

    }
}

