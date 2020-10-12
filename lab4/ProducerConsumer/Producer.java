package lab4.ProducerConsumer;

import java.util.concurrent.Semaphore;

public class Producer extends Thread {

    private final Semaphore fillCount;
    private final Semaphore emptyCount;
    private final TaskQueue queue;

    public Producer(final Semaphore fillCount, final Semaphore emptyCount, final TaskQueue queue) {
        this.fillCount = fillCount;
        this.emptyCount = emptyCount;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1200);
                int task = (int) (Math.random() * 1000) + 1000;
                System.out.println("Generated task: " + task);
                emptyCount.acquire();
                queue.writeTask(task);
                System.out.println("Task was written: " + task);
                System.out.println("Places left in buffer: " + emptyCount.availablePermits());
                fillCount.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
