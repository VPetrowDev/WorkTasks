package org.example;
public class Producer extends CommonLogic{
    public Producer(UniqueEventsQueue<Person> queue, int count) {
        super(queue, count);
    }
    @Override
    protected void doWork(int i) throws InterruptedException {
        Person person = new Person(i, String.format("%s%s", i, i));
        queue.add(person);
        System.out.println("Added person: " + queue.toString());
        Thread.sleep(1000);
    }
}


