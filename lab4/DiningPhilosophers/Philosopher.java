package lab4.DiningPhilosophers;

public class Philosopher extends Thread {

    private final Fork[] forks;
    private final int left;
    private final int right;

    public Philosopher(String name, final int id, final Fork[] forks) {
        super(name);
        this.forks = forks;
        this.left = id;
        this.right = (id + 1) % forks.length;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (forks[left].pickUp()) {
                if (forks[right].pickUp()) {
                    try {
                        System.out.println(getName() + " eating...");
                        Thread.sleep(1000);
                        System.out.println(getName() + " thinking...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    forks[right].putDown();
                }
                forks[left].putDown();
            }
        }
    }
}
