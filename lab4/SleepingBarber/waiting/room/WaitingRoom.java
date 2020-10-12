package lab4.SleepingBarber.waiting.room;

public interface WaitingRoom {

    int getCapacity();

    int getNumberOfBusyPlaces();

    void takePlace();

    void releasePlace();
}
