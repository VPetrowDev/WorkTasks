package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class MessageManager {
    private final ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Message>> queueMap;
    private final ConcurrentHashMap<Integer, ReentrantLock> lockMap;

    public MessageManager() {
        queueMap = new ConcurrentHashMap<>();
        lockMap = new ConcurrentHashMap<>();
    }

    public void addMessage(int key, Message message) {

        if (queueMap.containsKey(key)) {

            queueMap.putIfAbsent(key, new ConcurrentLinkedQueue<>());
            lockMap.putIfAbsent(key, new ReentrantLock());

        }

        ConcurrentLinkedQueue<Message> queue = queueMap.get(key);
        ReentrantLock lock = lockMap.get(key);

        try {
            lock.lock();
            queue.add(message);

        } finally {
            lock.unlock();
        }
    }

    public Message getMessage(int key) {
        if (!queueMap.containsKey(key)) {
            return null;
        }

        ConcurrentLinkedQueue<Message> queue = queueMap.get(key);
        ReentrantLock lock = lockMap.get(key);

        try {
            lock.lock();
           return queue.remove();

        } finally {
            lock.unlock();
        }

    }
    @Override
    public String toString() {
        return "MessageManager{" +
                "queueMap=" + queueMap +
                ", lockMap=" + lockMap +
                '}';
    }


}
