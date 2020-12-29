package diningPhilosophers;

import java.util.Random;

//Класс "Тарелка"
public class Plate {
    private static final int MAX_EATS = 100;//Максимальное кол-во еды
    private int currentEats = MAX_EATS;//При инициализации текущее кол-во еды = максимальному
    public void eatFromPlate(int id) {//Кушать
        this.currentEats--;
        try {
            System.out.println("philosopher "+id+" is eating from the plate");
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
        }//Съели одну ложку и отдыхаем от 0 до 500 мс
    }
    public boolean isEmplty() {//Проверка на пустоуту тарелки
        if (currentEats == 0) {
            return true;
        } else
            return false;
    }
    public int getCurrentEats(){//Геттор для текущего к-ва еды
        return currentEats;
    }
}
