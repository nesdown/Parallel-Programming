package diningPhilosophers;

import java.util.concurrent.locks.ReentrantLock;

//Класс "Стол"
public class Table {
    private final int NUM_OF_PEOPLE;//К-во философов
    private static Plate plate = new Plate();//Создадим объект "Тарелка"

    private Fork[] forks;//Массив вилок
    public Table(int numOfPeople) {
        NUM_OF_PEOPLE = numOfPeople;
        this.forks = new Fork[NUM_OF_PEOPLE];//Инициализируем массив вилок кол-вом равным кол-ву философов
        for (int i = 0; i < NUM_OF_PEOPLE; i++) {
            forks[i] = new Fork(new ReentrantLock());//Вызываем конструктор, передаем новый объект ReentrantLock() для блокировки потока
        }
    }
    public boolean getRightFork(final int id) {//Взять правую вилку
        if (id == NUM_OF_PEOPLE - 1 && forks[0] != null) {
            System.out.println("Right fork taken by philosopher " + id);
            return forks[0].getFork();
        } else {
            if (forks[id + 1] != null) {
                System.out.println("Right fork taken by philosopher " + id);
                return forks[id + 1].getFork();
            }
            return false;
        }
    }
    public boolean getLeftFork(final int id) {//взять левую вилку
        if (forks[id] != null) {
            System.out.println("Left fork taken by philosopher " + id);
            return forks[id].getFork();
        } else
            return false;
    }
    public void unlockLeftFork(int id) {//Разблокировать если положили левую вилку
        System.out.println("philosopher " + id + " is keeping the left fork");
        forks[id].unlockFork();
    }
    public void unlockRightFork(int id) {//Разблокировать если положили праую вилку
        System.out.println("philosopher " + id + " is keeping the right fork");
        if (id == NUM_OF_PEOPLE - 1) {
            forks[0].unlockFork();
            return;
        }
        forks[id + 1].unlockFork();
    }
    public Plate getPlate() {//геттор для тарелки
        return plate;
    }
}
