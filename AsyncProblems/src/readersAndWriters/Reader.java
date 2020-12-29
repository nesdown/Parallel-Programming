package readersAndWriters;

public class Reader extends Thread
{
    private static int readers = 0; // к-во читателей

    private int number;
    private DataBase database;

    public Reader(DataBase database)
    {
        this.database = database;
        this.number = Reader.readers++;
    }

    public void run()
    {
        while (true)
        {
            final int DELAY = 5000;
            try
            {
                Thread.sleep((int) (Math.random() * DELAY));
            }
            catch (InterruptedException e) {}
            this.database.read(this.number);
        }
    }
}