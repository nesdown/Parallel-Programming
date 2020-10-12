package lab4.WriterReader;

public class Writer extends Thread {

    private final int messagesToWrite;
    private int writtenMessages;
    private final Buffer buffer;

    public Writer(final int messagesToWrite, final Buffer buffer) {
        this.messagesToWrite = messagesToWrite;
        this.buffer = buffer;
        this.writtenMessages = 0;
    }

    @Override
    public void run() {
        while (!isInterrupted() && writtenMessages < messagesToWrite) {
            writtenMessages++;
            buffer.write();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {}
        }
    }

    public boolean stopTask() {
        return writtenMessages == messagesToWrite;
    }

}
