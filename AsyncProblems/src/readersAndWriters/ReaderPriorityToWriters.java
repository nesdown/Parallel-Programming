package readersAndWriters;

import java.util.concurrent.Semaphore;

public class ReaderPriorityToWriters implements Runnable {
    private volatile int readCount;
    private volatile int writeCount;
    private Semaphore readLock;
    private Semaphore writeLock;
    private Semaphore tryReadLock;
    private Semaphore res;

    public ReaderPriorityToWriters(int readCount, int writeCount, Semaphore readLock, Semaphore writeLock,
                                   Semaphore tryReadLock, Semaphore res){
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

            // Получаем разрешение у семафора
            tryReadLock.acquire();
            readLock.acquire();
            readCount ++;
            if(readCount == 1)
                res.acquire();
            readLock.release();
            tryReadLock.release();

            System.out.println("Thread "+Thread.currentThread().getName() + " is READING");
            Thread.sleep(1500);
            System.out.println("Thread "+Thread.currentThread().getName() + " has FINISHED READING");

            readLock.acquire();
            readCount --;
            if(readCount == 0)
                res.release();
            readLock.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
