import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Random;

class ThreadCalc extends Thread{
  int vect[];
  int startIndex;
  int endIndex;
  int result;

  public ThreadCalc(int[] vect, int startIndex,
  int endIndex) {
    this.vect = vect;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  public int getResult() {
    return result;
  }

  @Override
  public void run(){
    result = Math.abs(vect[startIndex]);

    for (int i = startIndex; i < endIndex; i++){
      result = Math.max(result, Math.abs(vect[i]));
    }
  }
}


public class ThreadSample {
  public static int SIZE[] = {1000, 100000, 10000000};
  public static int NUMBER_JOBS[] = {2, 8, 32};
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static void main(String [] args)
  throws InterruptedException{

    for (int size = 0; size < SIZE.length; size++) {
      for (int jobs = 0; jobs < NUMBER_JOBS.length; jobs++) {

        System.out.println("Size: " + SIZE[size] + ", number of jobs: " + NUMBER_JOBS[jobs]);

        int vect[] = new int [SIZE[size]];
        Random random = new Random();
        for (int i = 0; i < SIZE[size]; i++){
          vect[i] = random.nextInt(SIZE[size]*10 + 1) - SIZE[size]*5;
        }


        long serialTimeStart = System.nanoTime();
        int serialResult = Math.abs(vect[0]);

        for (int i = 0; i < SIZE[size]; i++){
          serialResult = Math.max(serialResult, Math.abs(vect[i]));
        }
        
        //WE ARE HERE
        System.out.println("Serial result: " + serialResult);
        double serialTime = (double)(System.nanoTime() - serialTimeStart);
        System.out.println(String.format("Serial Time: %.3f ms ", serialTime/1000000));


        long parallelTimeStart = System.nanoTime();
        ThreadCalc TreadArrray[] = new ThreadCalc[NUMBER_JOBS[jobs]];

        for (int i = 0; i < NUMBER_JOBS[jobs]; i++){
          TreadArrray[i] = new ThreadCalc(vect,
          SIZE[size]/NUMBER_JOBS[jobs] * i,
          i == NUMBER_JOBS[jobs] - 1 ? SIZE[size] : SIZE[size]/NUMBER_JOBS[jobs] * (i + 1) );
          TreadArrray[i].start();
        }

        for (int i = 0; i < NUMBER_JOBS[jobs]; i++){
          TreadArrray[i].join();
        }

        //WE ARE HERE
        int parallelResult = TreadArrray[0].getResult();

        for (int i = 0; i < NUMBER_JOBS[jobs]; i++){
          parallelResult = Math.max(parallelResult, TreadArrray[i].getResult());
        }

        System.out.println("Parallel result: " + parallelResult);
        double parallelTime = (double)(System.nanoTime() - parallelTimeStart);
        System.out.println(String.format("Parallel time: %.3f ms ", parallelTime/1000000));


        if (serialResult == parallelResult) {
          System.out.println(ANSI_GREEN + "The results are the same" + ANSI_RESET);
        } else {
          System.out.println(ANSI_RED + "The results are not the same!" + ANSI_RESET);
        }

        System.out.println(String.format("Acceleration: %.3f", (double)serialTime/parallelTime));
        System.out.println(String.format("Efficiency: %.3f", (double)(serialTime/parallelTime)/NUMBER_JOBS[jobs]));

        System.out.println("____________________________________");
      }
    }
  }
}
