package readersAndWriters;

/*
	Есть область памяти, допускающая чтение и запись. Несколько потоков имеют к ней доступ,
	при этом одновременно могут читать сколько угодно потоков, но писать — только один.
	Как обеспечить такой режим доступа?
 */

/*
	С приоритетом: Как только появился хоть один писатель, никого больше не пускать.
	Все остальные могут простаивать.
 */

public class DataBase
{
    private int readers; // сейчас читают...

    public DataBase()
    {
        this.readers = 0;
    }

    public void read(int number)
    {
        synchronized(this)//блокируем доступ к "readers" для остальных потоков
        {
            this.readers++;// изменяем
            System.out.println("Reader " + number + " starts reading.");
        }

//        Имитируем чтение
        final int DELAY = 5000;
        try
        {
            Thread.sleep((int) (Math.random() * DELAY));
        }
        catch (InterruptedException e) {}

        synchronized(this)
        {
            System.out.println("Reader " + number + " stops reading.");
            this.readers--;
            if (this.readers == 0)
            {
                this.notifyAll();
            }
        }
    }

    public synchronized void write(int number)
    {
        while (this.readers != 0)
        {
            try
            {
                this.wait();//Если кто-то читает -- ждем
            }
            catch (InterruptedException e) {}
        }
        System.out.println("Writer " + number + " starts writing.");//Когда читателей нету - начинаем писать

        final int DELAY = 5000;
        try
        {
            Thread.sleep((int) (Math.random() * DELAY));
        }
        catch (InterruptedException e) {}

        System.out.println("Writer " + number + " stops writing.");
        // По завершению, возобновляем работу всех потоков, которым вызывали wait.
        this.notifyAll();
    }
}