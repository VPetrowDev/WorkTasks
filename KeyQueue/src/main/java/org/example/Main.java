package org.example;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static void main(String[] args){
         ConcurrentLinkedQueue<Message> myQueue = new ConcurrentLinkedQueue<>();
         Set<Message> mySet = ConcurrentHashMap.newKeySet();

         KeyEvent keyEvent = new KeyEvent(myQueue,mySet);

        Thread producer1 = new Thread(new Producer(keyEvent, 1));
        Thread producer2 = new Thread(new Producer(keyEvent,  2));

        producer1.start();
        producer2.start();

        Thread consumer1 = new Thread(new Consumer(keyEvent, 1));
        Thread consumer2 = new Thread(new Consumer(keyEvent, 2));

        consumer1.start();
        consumer2.start();

    }
}
