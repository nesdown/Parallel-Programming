package lab3;

import java.util.stream.IntStream;

public class Main {

    private static final int EXECUTIONS = 5;
    private static final int MIN_LENGTH = 1000;
    private static final int MAX_LENGTH = 100000;

    public static void main(String[] args) {

        IntStream.range(0, EXECUTIONS).forEach(i -> {

            System.out.println("#" + (i + 1));
            AtomicIntArray arr = new AtomicIntArray(MIN_LENGTH, MAX_LENGTH);
            System.out.println("Generated array of " + arr.getLength() + " items.");

            System.out.println("Count of items bigger than 500: " + arr.getItemsCountByCondition(item -> item > 500));
            System.out.println("Count of items that div on 3: " + arr.getItemsCountByCondition(item -> item % 3 == 0));

            int min = arr.getIndexOfMin();
            int max = arr.getIndexOfMax();
            System.out.println("Min value of array: arr[" + min + "]=" + arr.getItem(min));
            System.out.println("Max value of array: arr[" + max + "]=" + arr.getItem(max));

            System.out.println("Array checksum: " + arr.getCheckSum());
            System.out.println();
        });
    }
}
