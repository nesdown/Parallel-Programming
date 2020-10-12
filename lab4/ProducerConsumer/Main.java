package lab4.ProducerConsumer;

import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {

        Semaphore fillCount = new Semaphore(0);
        Semaphore emptyCount = new Semaphore(10);
        TaskQueue queue = new TaskQueue();

        Producer producer = new Producer(fillCount, emptyCount, queue);
        Consumer consumer = new Consumer(fillCount, emptyCount, queue);

        producer.start();
        consumer.start();
    }
}
