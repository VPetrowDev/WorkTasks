package org.example;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UniqueEventsQueue<T> {
    private final Set<T> elements = new HashSet<>();
    private final Queue<T> queue = new LinkedList<T>();
    public static final int MAX_SIZE_QUEUE = 10;
    public int getSize(){
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    public boolean isFull(){
        return MAX_SIZE_QUEUE == getSize();
    }
    final Lock lock = new ReentrantLock();
    final Condition queueFullCondition   = lock.newCondition();
    final Condition queueEmptyCondition  = lock.newCondition();

    public boolean add(T t) throws InterruptedException {
        lock.lock();
        try {
            while (isFull()) {
                queueFullCondition.await();
            }
            if (elements.add(t)) {
                queue.add(t);
                queueEmptyCondition.signal();
            }

        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        finally {
            lock.unlock();
        }
            return true;
        }
    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                queueEmptyCondition.await();
            }
            T ret = queue.remove();

            if(ret != null){

            elements.remove(ret);
            queueFullCondition.signal();

            }
           return ret;

        }
        finally {
           lock.unlock();
        }

    }

//    @Override
//        public String toString(){
//        lock.lock();
//        try {
//            return queue.toString();
//        }
//        finally {
//            lock.unlock();
//        }
//        }
}

