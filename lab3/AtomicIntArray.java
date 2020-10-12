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

    public int getItemsCountByCondition(Function<Integer, Boolean> cond) {

        AtomicInteger count = new AtomicInteger(0);
        IntStream.of(items).parallel().forEach(item -> {
            if (cond.apply(item)) {
                count.incrementAndGet();
            }
        });
        return count.get();
    }

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

    private void generateItems() {

        items = new int[this.length];
        for (int i = 0; i < this.length; ++i) {
            items[i] = getRandomInRange(-1000000, 1000000);
        }
    }

    private int getRandomInRange(final int min, final int max) {
        return min + (int) (Math.random() * (max - min));
    }
}
