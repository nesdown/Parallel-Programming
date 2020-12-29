/*
Пять безмолвных философов сидят вокруг круглого стола, перед каждым философом стоит тарелка спагетти.
Вилки лежат на столе между каждой парой ближайших философов.

Каждый философ может либо есть, либо размышлять. Приём пищи не ограничен количеством оставшихся спагетти
— подразумевается бесконечный запас.
Тем не менее, философ может есть только тогда, когда держит две вилки — взятую справа и слева
(альтернативная формулировка проблемы подразумевает миски с рисом и палочки для еды вместо тарелок
со спагетти и вилок).

Каждый философ может взять ближайшую вилку (если она доступна) или положить — если он уже держит её.
Взятие каждой вилки и возвращение её на стол являются раздельными действиями, которые должны выполняться
одно за другим.
 */

package diningPhilosophers;

import java.util.Random;

public class Philosopher implements Runnable {
    private final int id;// id философа будет совпадать с id левой вилки
    private Table table;// стол
    private static Object lock = new Object();// блокировка для создания набора ждущих философов
    public Philosopher(int id, Table tb) {
        this.id = id;
        this.table = tb;
    }
    public int getId() {
        return id;
    }

    @Override
    public void run() {
        synchronized (lock) {//создание набора ждущих философов
            try {
                System.out.println("philosopher " + id + " is getting ready");
                lock.wait();//при инициалзации все философы будут в режиме ожидания
            } catch (InterruptedException e) {
            }
        }
        // условие того, что вилки надо держать две
        boolean isEatsRemaining = true;//логично что в начале всегда еда будет
        while (isEatsRemaining) {//Пытаемся есть пока тарелка не пуста
            if (this.table.getRightFork(id)) {
                if (this.table.getLeftFork(id)) {
                    isEatsRemaining = this.acquirePlateAndEat();
                    this.think();
                    this.table.unlockLeftFork(id);//после того как поели бросаем левую вилку
                }
                this.table.unlockRightFork(id);
            }
        }
        System.out.println("philosopher " + id + " is stopping");
        System.out.println("Current eats= "
                + table.getPlate().getCurrentEats());
    }
    public void think() {//Философ думает
        System.out.println("philosopher " + id + " is thinking");
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
        }
    }
    public boolean acquirePlateAndEat() {//Занять тарелку и кушать
        Plate plate = table.getPlate();
        synchronized (plate) {
            if (plate.isEmplty()) {
                return false;//Если пусто в тарелке - возвратим false
            }
            plate.eatFromPlate(id);
            return true;
        }
    }

    public static void Demonstrate() {//демонстрация работы алгоритма
        Table table = new Table(10);//Пусть кол-во философов и вилок будет 10
        Philosopher[] pQueue = new Philosopher[10];
        for (int id = 0; id < 10; id++) {
            Philosopher p = new Philosopher(id, table);
            pQueue[id] = p;
            Thread t = new Thread(p);
            t.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        synchronized (lock) {
            lock.notifyAll();//не забываем что в начале все потоки "ждут", по этому "пробуждаем" их и наблюдаем за работой алгоритма
        }
    }
}
