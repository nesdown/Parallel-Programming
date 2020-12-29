package sleepingBarber;
/*
У парикмахера есть одно рабочее место и приемная с несколькими стульями.
Когда парикмахер заканчивает подстригать клиента, он отпускает клиента и затем идет в приёмную,
чтобы посмотреть, есть ли там ожидающие клиенты. Если они есть, он приглашает одного из них и стрижет его.
Если ждущих клиентов нет, он возвращается к своему креслу и спит в нем.
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CustomerPool {

    private final Lock lock = new ReentrantLock();
    private final Condition poolAvailable = lock.newCondition();
    private int num_customers;
    private final int max_num_customers;


    public CustomerPool(int num_customer_pools) {
        this.max_num_customers = num_customer_pools;
        this.num_customers = 0;
    }

    public void acquireCustomer() throws InterruptedException {//клиент занимает место, если мест нету -- ждет
        // заняли
        lock.lock();
        try {
            while (num_customers <= 0)
                poolAvailable.await();
            --num_customers;
        } finally {
            // освободили
            lock.unlock();
        }
    }

    public void releaseCustomer() {//клиент освобождает место
        lock.lock();
        try{
            if(num_customers >= max_num_customers)
                return;
            ++num_customers;
            // сигнализируем, что поток может продолжить работу
            poolAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public int getNumOfCustomers() {
        return num_customers;
    }
}