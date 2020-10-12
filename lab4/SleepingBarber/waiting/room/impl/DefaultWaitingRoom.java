package lab4.SleepingBarber.waiting.room.impl;

import lab4.SleepingBarber.waiting.room.WaitingRoom;

public class DefaultWaitingRoom implements WaitingRoom {
    private final int maxQuantityOfClients;
    private int quantityOfClients;
    private int max;

    public DefaultWaitingRoom(final int maxQuantityOfClients) {

        this.maxQuantityOfClients = maxQuantityOfClients;
        this.quantityOfClients = 0;
        this.max = 0;
    }

    @Override
    public int getCapacity() {

        return maxQuantityOfClients;
    }

    @Override
    public int getNumberOfBusyPlaces() {

        return quantityOfClients;
    }

    @Override
    public void takePlace() {

        quantityOfClients++;

        if (quantityOfClients > max) {
            max = quantityOfClients;
        }
    }

    @Override
    public void releasePlace() {

        quantityOfClients--;
    }

    public int getMax() {
        return max;
    }
}
