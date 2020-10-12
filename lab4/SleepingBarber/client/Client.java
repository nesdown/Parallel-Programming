package lab4.SleepingBarber.client;

import lab4.SleepingBarber.waiting.room.WaitingRoom;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Client extends Thread {

    private final ReentrantLock client;
    private final ReentrantLock seats;
    private final ReentrantLock service;

    final Condition clientReady;
    final Condition serviceFinished;

    private final WaitingRoom waitingRoom;

    public Client(final ThreadGroup group, final String name,
                  final ReentrantLock client, final ReentrantLock seats, final ReentrantLock service,
                  final Condition clientReady, final Condition serviceFinished,
                  final WaitingRoom waitingRoom) {

        super(group, name);

        this.client = client;
        this.seats = seats;
        this.service = service;

        this.clientReady = clientReady;
        this.serviceFinished = serviceFinished;

        this.waitingRoom = waitingRoom;
    }

    @Override
    public void run() {

        System.out.println("Entered " + getName());
        if (takePlace()) {
            service.lock();
            wakeUpBarber();
            waitService();
        }
        System.out.println("Left " + getName());
    }

    private boolean takePlace() {

        boolean result = Boolean.FALSE;
        seats.lock();
        if (waitingRoom.getNumberOfBusyPlaces() < waitingRoom.getCapacity()) {
            System.out.println("Took place " + getName());
            waitingRoom.takePlace();
            result = Boolean.TRUE;
        }
        seats.unlock();
        return result;
    }

    private void wakeUpBarber() {

        System.out.println("Waked up " + getName());
        client.lock();
        try {
            clientReady.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.unlock();
        }
    }

    private void waitService() {

        try {
            serviceFinished.await();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            service.unlock();
        }
        System.out.println("Serviced " + getName());
    }

}
