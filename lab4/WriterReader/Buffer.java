package lab4.WriterReader;

public class Buffer {

    private int data = 0;
    private boolean write = false;

    private synchronized void tryRead() {
        while (write) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void read(String readerName) {
        tryRead();
        try {
            Thread.sleep((int) (Math.random() * 500) + 500);
        } catch (InterruptedException e) {}
        System.out.println(readerName + " read data: " + data);
    }

    public synchronized void write() {
        while (write) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        write = true;
        data = (int)(Math.random() * 1000) + 500;
        System.out.println("Wrote data: " + data);
        write = false;
        notify();

    }

}
