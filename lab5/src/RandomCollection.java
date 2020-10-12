import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


// Клас що описує колекцію наповнену випадковими значеннями
public class RandomCollection {
    //Список значень цілого типу
    private List<Integer> list;

    //Конструктор, що створює об'єкт за параметром
    public RandomCollection(List<Integer> list){
        this.list = list;//назначить ссылку
        sort();//отсортировать
    }

    //Конструктор, що створює об'єкт та ініціалізує список цілих чисел за параметрами розміру, мінімального та максимального значень.
    public RandomCollection(int capacity, int minVal, int maxVal){

        Random random = new Random();
        list = new ArrayList<Integer>();

        for(int i = 0; i < capacity; i++){
            getList().add(random.nextInt(maxVal - minVal) + minVal);
        }
    }

    //Доступ до списку для читання
    public List<Integer> getList() {
        return list;
    }

    //Доступ до списку для запису
    public void setList(List<Integer> list){
        this.list = list;
    }

    //Отримання середнього значення послідовності
    private double getAverageValue(){
        double average = 0;
        if (list.size() > 0)
        {
            double sum = 0;
            for (int j = 0; j < list.size(); j++) {
                sum += list.get(j);
            }
            average = sum / list.size();
        }

        return average;
    }

    // Видалити всі значення менші від середнього
    public void removeAllLessThanAverage(){
        double average = getAverageValue();
        List<Integer> newList = new ArrayList<>();

        for (int val: list) {
            if(val > average)
                newList.add(val);
        }

        list = newList;
    }

    // Видалити всі значення більші від середнього
    public void removeAllBiggerThanAverage(){
        double average = getAverageValue();
        List<Integer> newList = new ArrayList<>();

        for (int val: list) {
            if(val < average)
                newList.add(val);
        }

        list = newList;
    }

    //Сортування
    public void sort(){
        Collections.sort(list);
    }

    //Статичний метод об'єднання двух об'єктів одного класа в один з виключенням повторів
    public static RandomCollection joinTwoCollections(RandomCollection c1, RandomCollection c2){
        List<Integer> newList = new ArrayList<Integer>(c1.getList());



        for (int val : c2.getList()) {
            if(!newList.contains(val))
                newList.add(val);
        }

        return new RandomCollection(newList);
    }
}
