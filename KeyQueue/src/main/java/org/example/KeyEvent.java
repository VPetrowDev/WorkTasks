package org.example;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

 public class KeyEvent {
     BlockingQueue<Message> myQueue;
     final Set<Message> mySet;
     final BlockingQueue<Message> tempQueue = new LinkedBlockingQueue<>();
     final Lock lock = new ReentrantLock();

     final Condition emptyQueue = lock.newCondition();
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
         lock.lock();
         try {
             mySet.add(Message.POISON_PILL);
         }
         finally {
             lock.unlock();
         }
     }


     public void checkIfEventHasBeenProcessed() throws InterruptedException {
         lock.lock();
         try {

             Message messageFromTheTempQueue = tempQueue.peek();

             if (messageFromTheTempQueue == null) {
                 return;
             }

             boolean ifPass = checkSet(messageFromTheTempQueue);
             if (ifPass) {

                 messageFromTheTempQueue = tempQueue.take();
                 mySet.add(messageFromTheTempQueue);
                 myQueue.add(messageFromTheTempQueue);


                 System.out.println("Deleted from the tempQueue and added from the tempQueue to mySet and myQueue: " + messageFromTheTempQueue);

             }
             if(tempQueue.size() == 0){
                 doneAddingEvents();
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

                emptyQueue.signal();

             } else {
                 System.out.println("Oops we have the same event in the set already. Wait in the tempQueue! " + event.getMessage());
                 tempQueue.put(event);

             }

         }finally {
             lock.unlock();
         }
     }

     public void removeEvent (Message event) throws InterruptedException {
         lock.lock();
         try {
            while(mySet.isEmpty()){
                emptyQueue.await();
            }
                    if (mySet.remove(event)) {

                        myQueue.remove(event);
                        System.out.println("Removed from the set and the queue " + event.getMessage());

                    }

            // checkIfEventHasBeenProcessed();

         }finally {
             lock.unlock();
         }
     }

 }