//1) Розробити програму, яка за допомогою AtomicInteger  і метода compareAndSet:
//виконує наступні операції для одновимірного массиву. Для потоків використовувати  ExecutorService або parallelStream.
//2) Створити паралельні фкнкції для знаходження:
//- кількості елементів за умовою;
//- мінімального та максимального елементів з індексами;
//- контрольної суми із використанням XOR.

package lab3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

public class AtomicIntArray {

    private final int length;
    private int [] items;

    public AtomicIntArray(final int min, final int max) {

        this.length = this.getRandomInRange(min, max);
        this.generateItems();
    }

    public int getLength() {
        return length;
    }

    // Метод знаходження кількості за умовою
    public int getItemsCountByCondition(Function<Integer, Boolean> cond) {

        AtomicInteger count = new AtomicInteger(0);
        IntStream.of(items).parallel().forEach(item -> {
            if (cond.apply(item)) {
                // Атомарна операція збільшення
                count.incrementAndGet();
            }
        });
        return count.get();
    }

    // Метод знаходження індексу за максимальним
    public int getIndexOfMax() {

        AtomicInteger maxIndex = new AtomicInteger(0);
        IntStream.range(0, length).parallel().forEach(i -> {
            int prev, next;
            do {
                prev = maxIndex.get();
                if (items[prev] < items[i]) {
                    next = i;
                }
                else {
                    next = prev;
                }
            } while (!maxIndex.compareAndSet(prev, next));
        });
        return maxIndex.get();
    }

    // Метод знаходження індексу за мінімальним
    public int getIndexOfMin() {

        AtomicInteger minIndex = new AtomicInteger(0);
        IntStream.range(0, length).parallel().forEach(i -> {
            int prev, next;
            do {
                prev = minIndex.get();
                if (items[prev] > items[i]) {
                    next = i;
                }
                else {
                    next = prev;
                }
            } while (!minIndex.compareAndSet(prev, next));
        });
        return minIndex.get();
    }

    // Метод отримання контрольної суми за ксор
    public int getCheckSum() {

        AtomicInteger checkSum = new AtomicInteger(0);
        IntStream.of(items).parallel().forEach(item -> {
            int prev, next;
            do {
                prev = checkSum.get();
                next = prev ^ item;
            } while (!checkSum.compareAndSet(prev, next));
        });
        return checkSum.get();
    }

    public int getItem(final int i) {
        return items[i];
    }

    // Рандомізатор
    private void generateItems() {

        items = new int[this.length];
        for (int i = 0; i < this.length; ++i) {
            items[i] = getRandomInRange(-1000000, 1000000);
        }
    }

    // Рандомізатор за випадковими
    private int getRandomInRange(final int min, final int max) {
        return min + (int) (Math.random() * (max - min));
    }
}
