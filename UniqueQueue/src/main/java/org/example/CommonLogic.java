package org.example;

public abstract class CommonLogic implements Runnable{
   private static final int DELAY = 1000;
    public final UniqueEventsQueue<Person> queue;
    private final int count;

    public CommonLogic(UniqueEventsQueue<Person> queue, int count ) {
        this.queue = queue;
        this.count = count;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < count; i++) {
                doWork(i);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void doWork(int i) throws InterruptedException;
}