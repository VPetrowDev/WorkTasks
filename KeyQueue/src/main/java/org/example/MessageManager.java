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

    public void addMessage(Message message) {
        int id = message.getId();
        if (!queueMap.containsKey(id)) {

            queueMap.put(id, new ConcurrentLinkedQueue<>());
            lockMap.put(id, new ReentrantLock());

        }

        ConcurrentLinkedQueue<Message> queue = queueMap.get(id);
        ReentrantLock lock = lockMap.get(id);
        lock.lock();

        try {
            queue.add(message);

        } finally {
            lock.unlock();
        }
    }

    public Message getMessage(int id) {
        if (!queueMap.containsKey(id)) {
            return null;
        }

        ConcurrentLinkedQueue<Message> queue = queueMap.get(id);
        ReentrantLock lock = lockMap.get(id);
        lock.lock();

        try {
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
