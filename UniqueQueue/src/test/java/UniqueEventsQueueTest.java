import org.example.Consumer;
import org.example.Person;
import org.example.Producer;
import org.example.UniqueEventsQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UniqueEventsQueueTest<T> {

    @Test
    public void getSize(){
        UniqueEventsQueue<Person> queue = new UniqueEventsQueue<>();
        assertEquals(0, queue.getSize());
    }
    @Test
    public void isFull() throws InterruptedException {
        UniqueEventsQueue<Integer> queue = new UniqueEventsQueue<>();
        Assertions.assertFalse(queue.isFull());

        for(int i = 0; i < UniqueEventsQueue.MAX_SIZE_QUEUE; i++){
            queue.add(i);
        }
        assertTrue(queue.isFull());
    }
    @Test
    public void testAdd() throws InterruptedException {
        UniqueEventsQueue<Person> queue = new UniqueEventsQueue<>();

        Person person1 = new Person(1, "Person 1");

        assertTrue (queue.add(person1));
        assertEquals(1,queue.getSize());

        Person person2 = new Person(2, "Person 2");
        assertTrue (queue.add(person2));
        assertEquals(2,queue.getSize());

    }
        @Test
        public void testGet () throws InterruptedException {
            UniqueEventsQueue<Person> queue = new UniqueEventsQueue<>();

            Person person1 = new Person(1, "Person 1");
            Person person2 = new Person(2, "Person 2");
            queue.add(person1);
            queue.add(person2);

            assertEquals(queue.get(), person1);
            assertEquals(queue.getSize(), 1);

            assertEquals( queue.get(), person2);
            assertEquals(queue.getSize(), 0);

        }
    @Test
    public void testLockConditions() throws InterruptedException {
        UniqueEventsQueue<Person> queue = new UniqueEventsQueue<>();

        Producer producer = new Producer(queue, 10);
        Consumer consumer = new Consumer(queue, 10);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        assert queue.getSize() == 0;
    }

    @Test
    public void testInterruptWaitingThreadForGetMethod() throws InterruptedException {
        UniqueEventsQueue<Integer> queue = new UniqueEventsQueue<>();
        Thread thread = new Thread(() -> {
            try {
                queue.get();
            } catch (InterruptedException e) {
                assertThat(e, instanceOf(InterruptedException.class));
                assertEquals("Thread interrupted", e.getMessage());
            }
        });
        thread.start();
        thread.interrupt();
        thread.join();
    }
    @Test
    public void testInterruptWaitingThreadForAddMethod() throws InterruptedException {
        UniqueEventsQueue<Person> queue = new UniqueEventsQueue<>();
        for (int i = 0; i < 10; i++) {
            queue.add(new Person(i, "Person " + i));
        }


        Thread addThread = new Thread(() -> {
            try {
                queue.add(new Person(11, "Person 11"));
            } catch (InterruptedException e) {
                assertThat(e, instanceOf(InterruptedException.class));
                assertEquals("Thread interrupted", e.getMessage());
            }
        });

        addThread.start();
        addThread.interrupt();
        addThread.join();

        assertEquals(queue.getSize() , 10);
    }
}
