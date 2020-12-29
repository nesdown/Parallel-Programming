/*
Имеются один или несколько производителей, генерирующих данные некоторого типа
записи, символы и т.п.) и помещающих их в буфер, а также единственный потребитель,
который извлекает помещенные в буфер элементы по одному. Требуется защитить систему от
перекрытия операций с буфером, т.е. обеспечить, чтобы одновременно получить доступ к буферу
мог только один процесс (производитель или потребитель).
 */

package producersAndConsumers;

import java.util.concurrent.Semaphore;

public class ProducerAndConsumerDemo {

    // Инициализируем потоки для производителей и потребителей
    private static Thread producerThread;
    private static Thread consumerThread;

    // Инициализируем также семафоры, у обоих потоков есть доступы к ресурсам. При этом, допустимое кол-во разрешений одно.
    private static Semaphore prodLock = new Semaphore(1);
    private static Semaphore consLock = new Semaphore(1);

    // счетчик выполнения, и максимальный размер буфера
    private static volatile int production = 0;
    private final static int BUFFER_SIZE = 10;

    public static void Demonstrate() throws InterruptedException {

        producerThread = new Thread(()->{
            while (true){

                try{
//                    Пока поток не получит разрешение, он блокируется
                    prodLock.acquire();

                    // После работы с ресурсом, освобождаем один из ресурсов
                    generateProduct(1000);

                    if(production < BUFFER_SIZE){
                        prodLock.release();
                    }
                    else
                        consLock.release();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        producerThread.setName("prod");

//        Идентичная реализация для потребителей
        consumerThread = new Thread(()->{

            while (true){
                try {

                    consLock.acquire();

                    // если есть что потреблять, то потребляем, если нет то освобождаем производителя
                    if(production != 0){
                        consumeProduct(250);
                        consLock.release();
                    }
                    else
                        prodLock.release();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        });
        consumerThread.setName("cons");

        producerThread.start();
        Thread.sleep(500);
        consumerThread.start();
    }

//    Операция имитирующая произведение
    private static void generateProduct(int generatingTime){
        try {
            System.out.println("Thread "+Thread.currentThread().getName() + " Thread start generating");
            Thread.sleep(generatingTime);
            production ++;
            System.out.println("Thread "+Thread.currentThread().getName() + " Thread generated | Production Count = " + production);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Операция имитирующая потребление
    private static void consumeProduct(int delay) throws InterruptedException {

        production--;
        System.out.println("Thread " + Thread.currentThread().getName() + " Thread consumed one item | Production Count = " + production);
        Thread.sleep(delay);
    }
}
