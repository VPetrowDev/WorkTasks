import org.example.Consumer;
import org.example.Person;
import org.example.Producer;
import org.example.UniqueEventsQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repeat.Repeat;

import java.util.concurrent.*;

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
    @Repeat(times = 10, threads = 10)
    public void testAddWaitsForSpace() throws InterruptedException {

        UniqueEventsQueue<Person> queue = new UniqueEventsQueue<>();

        for (int i = 0; i < 10; i++) {
            queue.add(new Person(i, "Person " + i));
        }

        CountDownLatch latch = new CountDownLatch(1);

        Thread addThread = new Thread(() -> {
            try {

                latch.countDown();
                queue.add(new Person(11, "Person 11"));

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        addThread.start();

        latch.await(1, TimeUnit.SECONDS);

        assertEquals(Thread.State.WAITING,addThread.getState());

        assertEquals(queue.getSize() , 10);
    }

    @Test
    @Repeat(times = 10, threads = 10)
    public void testGetWaitsForElement() throws InterruptedException {
        UniqueEventsQueue<Integer> queue = new UniqueEventsQueue<>();

        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            try {
                latch.countDown(); // count down the latch

                queue.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        latch.await(1, TimeUnit.SECONDS);

        assertEquals(Thread.State.WAITING, thread.getState());

        assertEquals(0,queue.getSize());
    }
}
