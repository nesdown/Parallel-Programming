// Глушко Богдан, ІП-71, 6 варіант

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {

        //Створимо перший об'єкт CompletableFuture за допомогою статичного метода supplyAsync,
        //в якому параметром передамо анонімний метод, що запуститься в новому потоці.
        //Тут створимо об'єкт RandomCollection, а з нього видалимо усі значення нижче середного.
        CompletableFuture<RandomCollection> completableFuture = CompletableFuture.supplyAsync(() -> {

            RandomCollection rc1 = new RandomCollection(10, -20, 300);
            rc1.removeAllLessThanAverage();

            return rc1;
        });

        //Головний потік - тут створимо другий об'єкт і видалимо з нього всі значення більше середнього.
        //Ця задача паралельна створенню першого об'єкта
        RandomCollection rc2 = new RandomCollection(15, -400, 600);
        rc2.removeAllBiggerThanAverage();

        //Створимо ще один CompletableFuture за допомогою thenApply викликаного з першого об'єкта
        //CompletableFuture, в цей метод передамо "лямбду" параметром якої буде результат роботи першого об'єкта.
        //ТТаким чином: якщо робота з другим RandomCollection виконається швидше, то виконання лямбди не почнеться доки
        //не завершиться робота з першим RandomCollection. Задача "лямбди" злити обидва об'екти RandomCollection згідно завдання.
        CompletableFuture<RandomCollection> future = completableFuture.thenApply((s) -> {
            return RandomCollection.joinTwoCollections(s, rc2);
        });

        try {
            //отримання результуючої колекції з асинхронного потоку
            RandomCollection res = future.get();

            //виведення результатів у консоль
            for(int el : res.getList())
                System.out.print(el + ", ");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
