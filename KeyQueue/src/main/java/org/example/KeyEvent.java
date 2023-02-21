package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyEvent {
    ConcurrentLinkedQueue<Message> myQueue;
    private final Set<Message> mySet;
    private final Queue<Message> tempQueue = new ConcurrentLinkedQueue<>();

    public KeyEvent(ConcurrentLinkedQueue<Message> myQueue, Set<Message> mySet) {
        this.myQueue = myQueue;
        this.mySet = mySet;
    }


    public void addEvent(Message event) {
        boolean added;
        added = mySet.add(event);

        if (added) {
            myQueue.add(event);
            System.out.println("Added successfully to the queue! " + event.getMessage());
        } else {
            System.out.println("Oops we have the same event in the set already. Wait in the queue! " + event.getMessage());
            tempQueue.add(event);
        }

        Message duplicateMessage = null;
            if (!myQueue.contains(event)) {

                System.out.println("Checking if this event has been processed. If yes, add it to the queue!");
                duplicateMessage = tempQueue.poll();
                mySet.add(duplicateMessage);

            }

        if (duplicateMessage != null) {
            myQueue.add(duplicateMessage);
            tempQueue.remove(duplicateMessage);
        }
    }

    public Message removeEvent(Message event) {
            if (tempQueue.contains(event)) {
                // The event is a duplicate, so don't remove it from the queue
                System.out.println("Event is a duplicate, so not removing from the queue: " + event.getMessage());
                return null;
            } else {
                // The event is not a duplicate, so remove it from the queue and the set
                mySet.remove(event);
                System.out.println("Removed event from the set: " + event.getMessage());
                return myQueue.poll();
            }
    }

}
