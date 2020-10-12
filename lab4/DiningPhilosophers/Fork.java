package lab4.DiningPhilosophers;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {

    private final String name;
    private final Lock lock;

    public Fork(final String name) {
        this.name = name;
        lock = new ReentrantLock();
    }

    public String getName() {
        return name;
    }

    public boolean pickUp() {
        return lock.tryLock();
    }

    public void putDown() {
        lock.unlock();
    }
}
