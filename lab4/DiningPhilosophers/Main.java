package lab4.DiningPhilosophers;

public class Main {

    public static void main(String[] args) {
        Fork[] forks = new Fork[5];
        for (int i = 0; i < 5; ++i) {
            forks[i] = new Fork("Fork " + (i + 1));
        }
        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; ++i) {
            philosophers[i] = new Philosopher("Philosopher " + (i + 1), i, forks);
        }

        for (int i = 0; i < 5; ++i) {
            philosophers[i].start();
        }

    }
}
