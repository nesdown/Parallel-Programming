package lab4.ProducerConsumer;

import java.util.ArrayDeque;
import java.util.Queue;

public class TaskQueue {

    private final Queue<Integer> queue;

    public TaskQueue() {
        this.queue = new ArrayDeque<>();
    }

    public void writeTask(int task) {
        queue.offer(task);
    }

    public int readTask() {
        int task = queue.poll();
        return task;
    }

}
