package org.example;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args){
        BlockingQueue<Message> myQueue = new LinkedBlockingQueue<>();
        Set<Message> mySet = ConcurrentHashMap.newKeySet();

        KeyEvent keyEvent = new KeyEvent(myQueue,mySet);

        Thread producer1 = new Thread(new Producer(keyEvent, 1));
        Thread producer2 = new Thread(new Producer(keyEvent, 2));

        producer1.start();
        producer2.start();

        Thread consumer1 = new Thread(new Consumer(keyEvent, 1));
        Thread consumer2 = new Thread(new Consumer(keyEvent, 2));

        consumer1.start();
        consumer2.start();

    }
}
