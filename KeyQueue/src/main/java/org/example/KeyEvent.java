package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyEvent {
    public ConcurrentLinkedQueue<Message> myQueue;
    public Set<Message> mySet;

    public KeyEvent(ConcurrentLinkedQueue<Message> myQueue, Set<Message> mySet) {
        this.myQueue = myQueue;
        this.mySet = mySet;
    }

    public void addEvent(Message event){
        Queue<Message> tempQueue = new LinkedList<>();
        if(mySet.add(event)){

            myQueue.add(event);
            System.out.println("Added successfully to the queue!" + event.getMessage());

        }else {
            System.out.println("Oops we have the same event in the set already. Wait in the array!" + event.getMessage());
            tempQueue.add(event);
        }

        if(!myQueue.contains(event)){

            System.out.println("Cheking if this event has been processed. If yes, add it to the queue!");

            Message duplicateMessage = tempQueue.poll();
            myQueue.add(duplicateMessage);
            tempQueue.remove(duplicateMessage);
        }
    }
    public Message removeEvent(Message event){
        System.out.println("Do you consume?");
        return myQueue.poll();
    }
}
