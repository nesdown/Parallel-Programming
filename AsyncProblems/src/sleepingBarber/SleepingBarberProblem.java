package sleepingBarber;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SleepingBarberProblem extends Thread {

    public static final int CHAIRS = 5;//К-во стульев = 5

    public static int numberOfFreeSeats = CHAIRS;//В начале к-во свободных стульев = к-ву всех стульев


    public static CustomerPool customers = new CustomerPool(CHAIRS);//Создаем очередь клиентов

    public static Lock barber = new ReentrantLock();//"Лок" для парикмахера
    public static Condition barberAvailable = barber.newCondition();


    public static Lock accessSeats = new ReentrantLock();//"Лок" для досупа к местам


    class Customer extends Thread {

        int id;
        boolean notCut = true;


        public Customer(int i) {
            id = i;
        }

        public void run() {
            while (notCut) {
                accessSeats.lock(); // Пробуем получить доступ к стульям
                if (numberOfFreeSeats > 0) {//Если есть свободные...
                    System.out.println("Customer " + this.id + " just sat down.");
                    numberOfFreeSeats--; // Клиент сел -- минус стул
                    customers.releaseCustomer();

                    accessSeats.unlock(); // больше не блокируем доступ к местам
                    barber.lock();//блокируем поток парикмахера
                    try {
                        barberAvailable.await();
                    } catch (InterruptedException e) {
                    } finally {
                        barber.unlock();
                    }
                    notCut = false;
                    this.get_haircut(); // Парикмахер работает 3.5 сек
                } else { // Если мест нету, клиент уходит...
                    System.out.println("There are no free seats. Customer " + this.id + " has left the barbershop.");
                    accessSeats.unlock();
                    notCut = false;
                }
            }
        }

        // Стрижка, которая длится 3.5 секунд
        public void get_haircut() {
            System.out.println("Customer " + this.id + " is getting his hair cut");
            try {
                sleep(3500);
            } catch (InterruptedException ex) {
            }
        }

    }

    class Barber extends Thread {

        public Barber() {
        }

        public void run() {
            while (true) {
                try {
                    customers.acquireCustomer(); // клиент занимает место, если такого нет - парикмахер спит.

                    accessSeats.lock(); // здесь нужно изменить к-во свободных мест, так как один клиент пришел на стрижку
                    numberOfFreeSeats++;
                    barber.lock();

                    try {
                        // Сообщаем всем процессам, что парикмахер готов
                        barberAvailable.signal(); // Парикмахер готов работать
                    } finally {
                        barber.unlock();
                    }
                    // И что освободилось одно место
                    accessSeats.unlock();

                    this.cutHair(); // работает...
                } catch (InterruptedException ex) {
                }
            }
        }

        public void cutHair() {
            System.out.println("The barber is cutting hair");
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
            }
        }
    }


    public static void Demonstrate() {

        SleepingBarberProblem barberShop = new SleepingBarberProblem();
        barberShop.start();
    }

    public void run() {
        Barber barber = new Barber();
        barber.start();

        for (int i = 1; i < 16; i++) {//сегодня прийдет 16 клиентов
            Customer aCustomer = new Customer(i);
            aCustomer.start();
            try {
                sleep(2000);
            } catch (InterruptedException ex) {
            }
        }
    }
}