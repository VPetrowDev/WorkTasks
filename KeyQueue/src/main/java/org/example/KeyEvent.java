 package org.example;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

 public class KeyEvent {
     final BlockingQueue<Message> myQueue;
     private final Set<Message> mySet;
     final BlockingQueue<Message> tempQueue = new LinkedBlockingQueue<>();
     final Lock lock = new ReentrantLock();
     AtomicInteger numOfActiveConsumers = new AtomicInteger(0);

     public KeyEvent(BlockingQueue<Message> myQueue, Set<Message> mySet) {
         this.myQueue = myQueue;
         this.mySet = mySet;
     }

     public boolean checkSet(Message message){
         lock.lock();
         try{
             if(message == null){
                 return false;
             }
            return mySet.add(message);
         }finally {
             lock.unlock();
         }
     }

     public void doneAddingEvents() throws InterruptedException {
        int numOfPoisonPills = numOfActiveConsumers.get();

        for(int i = 0; i < numOfPoisonPills; i++){

         myQueue.put(Message.POISON_PILL);

        }
     }

     public void checkIfEventHasBeenProcessed() throws InterruptedException {
         lock.lock();
         try {

             Message messageFromTheTempQueue = tempQueue.peek();

             boolean ifPass = checkSet(messageFromTheTempQueue);
             if (ifPass) {

                 messageFromTheTempQueue = tempQueue.take();
                 myQueue.add(messageFromTheTempQueue);

                 if(tempQueue.size() == 0){
                     doneAddingEvents();
                 }
                 System.out.println("Deleted from the tempQueue: " + messageFromTheTempQueue);

             }
         } finally {
             lock.unlock();
         }
     }

     public void addEvent(Message event) throws InterruptedException {
         lock.lock();
         try {

             boolean added = checkSet(event);

             if (added) {

                 myQueue.put(event);
                 System.out.println("Added successfully to the queue! " + event.getMessage());

             } else {
                 System.out.println("Oops we have the same event in the set already. Wait in the tempQueue! " + event.getMessage());

                 tempQueue.put(event);
             }

      //checkIfEventHasBeenProcessed();
         }finally {
             lock.unlock();
         }
     }

     public void removeEvent (Message event) throws InterruptedException {
         lock.lock();
         try {

             myQueue.remove(event);
             mySet.remove(event);
             checkIfEventHasBeenProcessed();

         }finally {
             lock.unlock();
         }
     }
     public void incrementAndGetNumConsumers() {
         numOfActiveConsumers.incrementAndGet();
     }

     public void decrementAndGetNumConsumers() {
         numOfActiveConsumers.decrementAndGet();
     }
 }