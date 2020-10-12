package lab4.WriterReader;

public class Reader extends Thread {

    private final Buffer buffer;

    public Reader(final Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            buffer.read(getName());
        }
    }
}
