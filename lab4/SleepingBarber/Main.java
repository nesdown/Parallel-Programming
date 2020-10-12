package lab4.SleepingBarber;

import lab4.SleepingBarber.barber.Barber;
import lab4.SleepingBarber.client.Client;
import lab4.SleepingBarber.waiting.room.WaitingRoom;
import lab4.SleepingBarber.waiting.room.impl.DefaultWaitingRoom;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final int BARBERS_QUANTITY = 1;
    private static final int CLIENTS_QUANTITY = 5;
    private static final int QUANTITY_OF_SEATS = 4;

    public static void main(String[] args) {

        final ThreadGroup barbersThreadGroup = new ThreadGroup("Barber");
        final ThreadGroup clientsThreadGroup = new ThreadGroup("Client");

        final ReentrantLock client = new ReentrantLock();
        final ReentrantLock seats = new ReentrantLock();
        final ReentrantLock service = new ReentrantLock();

        final DefaultWaitingRoom waitingRoom = new DefaultWaitingRoom(QUANTITY_OF_SEATS);

        final Condition clientReady = client.newCondition();
        final Condition serviceFinished = service.newCondition();

        final List<Thread> barbers = createBarbers(barbersThreadGroup, client, seats, service, clientReady, serviceFinished, waitingRoom);
        final List<Thread> clients = createClients(clientsThreadGroup, client, seats, service, clientReady, serviceFinished, waitingRoom);

        barbers.forEach(Thread::start);
        clients.forEach(Thread::start);

        stopSolution(clients, barbersThreadGroup);

        System.out.println("Max queue size: " + waitingRoom.getMax());
    }

    private static List<Thread> createBarbers(final ThreadGroup group, final ReentrantLock client, final ReentrantLock seats,
                                       final ReentrantLock service, final Condition clientReady, final Condition serviceFinished,
                                       final WaitingRoom waitingRoom) {

        return IntStream.range(0, BARBERS_QUANTITY)
                .mapToObj(index -> new Barber(group, "barber#" + index,
                        client, seats, service, clientReady, serviceFinished, waitingRoom))
                .collect(Collectors.toList());
    }

    private static List<Thread> createClients(final ThreadGroup group, final ReentrantLock client, final ReentrantLock seats,
                                       final ReentrantLock service, final Condition clientReady, final Condition serviceFinished,
                                       final WaitingRoom waitingRoom) {

        return IntStream.range(0, CLIENTS_QUANTITY)
                .mapToObj(index -> new Client(group, "Client#" + index,
                        client, seats, service, clientReady, serviceFinished, waitingRoom))
                .collect(Collectors.toList());
    }

    private static void stopSolution(final List<Thread> clients, final ThreadGroup barbersThreadGroup) {

        waitForClients(clients);
        barbersThreadGroup.interrupt();
    }

    private static void waitForClients(final List<Thread> clients) {

        for (final Thread client : clients) {
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
