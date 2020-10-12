package lab4.WriterReader;

public class Main {

    public static void main(String[] args) {

        Buffer buffer = new Buffer();

        Reader[] readers = new Reader[5];
        for (int i = 0; i < 5; ++i) {
            readers[i] = new Reader(buffer);
        }
        Writer writer = new Writer(10, buffer);

        writer.start();
        for (int i = 0; i < 5; ++i) {
            readers[i].start();
        }

        while (true) {
            if (writer.stopTask()) {
                writer.interrupt();
                for (int i = 0; i < 5; ++i) {
                    readers[i].interrupt();
                }
                break;
            }
        }

    }
}
