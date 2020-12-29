package readersAndWriters;

import java.util.concurrent.Semaphore;

public class WritersWithPriority implements  Runnable {

    private volatile int readCount;
    private volatile int writeCount;
    private Semaphore readLock;
    private Semaphore writeLock;
    private Semaphore tryReadLock;
    private Semaphore res;

    public WritersWithPriority(int readCount, int writeCount, Semaphore readLock, Semaphore writeLock,
                                Semaphore tryReadLock, Semaphore res) {
        this.readCount = readCount;
        this.writeCount = writeCount;
        this.readLock = readLock;
        this.writeLock = writeLock;
        this.tryReadLock = tryReadLock;
        this.res = res;
    }

    @Override
    public void run() {

        try {
            writeLock.acquire();
            writeCount ++;
            if(writeCount == 1)
                tryReadLock.acquire();
            writeLock.release();

            res.acquire();
            System.out.println("Thread "+Thread.currentThread().getName() + " is WRITING");
            Thread.sleep(2500);
            System.out.println("Thread "+Thread.currentThread().getName() + " has finished WRITING");
            res.release();

            writeLock.acquire();
            writeCount --;
            if(writeCount == 0)
                tryReadLock.release();
            writeLock.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
