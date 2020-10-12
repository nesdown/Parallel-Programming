package lab4.WriterReader;

public class ReaderCounter {

    private int activeReaders;

    public ReaderCounter() {
        this.activeReaders = 0;
    }

    public int inc() {
        return ++activeReaders;
    }

    public int dec() {
        return --activeReaders;
    }
}
