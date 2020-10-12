package lab4.ProducerConsumer;

import java.util.concurrent.Semaphore;

public class Consumer extends Thread {

    private final Semaphore fillCount;
    private final Semaphore emptyCount;
    private final TaskQueue queue;

    public Consumer(final Semaphore fillCount, final Semaphore emptyCount, final TaskQueue queue) {
        this.fillCount = fillCount;
        this.emptyCount = emptyCount;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Reading task from buffer.");
                fillCount.acquire();
                int task = queue.readTask();
                System.out.println("Task was read: " + task);
                emptyCount.release();
                Thread.sleep(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
