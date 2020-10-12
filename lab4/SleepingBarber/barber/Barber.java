package lab4.SleepingBarber.barber;

import lab4.SleepingBarber.waiting.room.WaitingRoom;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barber extends Thread {

    private final ReentrantLock client;
    private final ReentrantLock seats;
    private final ReentrantLock service;

    final Condition clientReady;
    final Condition serviceFinished;

    private final WaitingRoom waitingRoom;

    public Barber(final ThreadGroup group, final String name,
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

        while (!isInterrupted()) {
            getClient();
            if (isInterrupted()) break;
            System.out.println("Cutting... " + getName());
            finishService();
        }
    }

    private void getClient() {

        seats.lock();
        if (waitingRoom.getNumberOfBusyPlaces() == 0) {
            System.out.println("Fell asleep " + getName());
            waitForClient();
            if (isInterrupted()) return;
        }
        takeClient();
    }

    private void waitForClient() {

        client.lock();
        seats.unlock();
        try {
            clientReady.await();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            client.unlock();
        }
    }

    private void takeClient() {

        lockSeatsIfNeeded();
        waitingRoom.releasePlace();
        seats.unlock();
        System.out.println("Took client " + getName());
    }

    private void lockSeatsIfNeeded() {
        if (!seats.isHeldByCurrentThread()) {
            seats.lock();
        }
    }

    private void finishService() {

        System.out.println("Cut " + getName());
        service.lock();
        try {
            serviceFinished.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.unlock();
        }
    }

}
