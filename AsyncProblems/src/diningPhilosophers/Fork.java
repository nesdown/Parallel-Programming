package diningPhilosophers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

//Класс "Вилка"
public class Fork {
    private Lock lock;//объект для блокировки
    Fork(Lock lock) {
        this.lock = lock;
    }
    public boolean getFork() {//Взять вилку и вернуть значение true, если вилка уже используется -- вернуть false
        boolean isTaken = false;
        try {
            isTaken = this.lock.tryLock(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
        }
        return isTaken;
    }
    public void unlockFork(){//Разблокировать. Используется в случае, когда философ закончил есть
        this.lock.unlock();
    }

}
