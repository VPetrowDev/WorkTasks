package org.example;
public class Consumer extends CommonLogic{
    public Consumer(UniqueEventsQueue<Person> queue, int count) {
        super(queue, count);
    }
    @Override
    protected void doWork(int i) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Consumer consumed: " + queue.toString());
        queue.get();
    }
}
