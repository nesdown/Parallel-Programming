package readersAndWriters;

public class Writer extends Thread {
    private static int writers = 0; // к-во писателей

    private int number;
    private DataBase database;

    public Writer(DataBase database) {
        this.database = database;
        this.number = Writer.writers++;
    }

    public void run() {
        while (true) {
            final int DELAY = 5000;
            try {
                Thread.sleep((int) (Math.random() * DELAY));
            } catch (InterruptedException e) {
            }
            this.database.write(this.number);
        }
    }
}