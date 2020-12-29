package readersAndWriters;

import java.util.concurrent.Semaphore;

public class ReadersAndWritersDemo {

    // non-prioritizied
    static Semaphore readLock = new Semaphore(1);
    static Semaphore writeLock = new Semaphore(1);
    volatile static int readCount = 0;

    volatile static int writeCount = 0;
    static Semaphore tryReadLock = new Semaphore(1);
    static Semaphore res = new Semaphore(1);

    public static void Demonstrate() throws Exception{
        final int READERS = 4;
        final int WRITERS = 2;
        DataBase database = new DataBase();
        for (int i = 0; i < READERS; i++)
        {
            new Reader(database).start();
        }
        for (int i = 0; i < WRITERS; i++)
        {
            new Writer(database).start();
        }
    }

    public static void DemonstrateWritersPriority() throws Exception{

        ReaderPriorityToWriters read = new ReaderPriorityToWriters(readCount, writeCount, readLock, writeLock, tryReadLock, res);
        WritersWithPriority write = new WritersWithPriority(readCount, writeCount, readLock, writeLock, tryReadLock, res);

        Thread t1 = new Thread(read);
        t1.setName("T1");
        Thread t2 = new Thread(read);
        t2.setName("T2");
        Thread t3 = new Thread(write);
        t3.setName("T3");
        Thread t4 = new Thread(read);
        t4.setName("T4");
        t1.start();
        t3.start();
        t2.start();
        t4.start();
    }
}
