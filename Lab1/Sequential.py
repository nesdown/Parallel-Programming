'''
ГЛУШКО Б.С., 14.09.2020
Створити вектор з N >= 1000 елементами з випакдкових чисел.
Знайти норму.

ПОСЛІДОВНЕ ВИРІШЕННЯ
'''

import numpy as np 
import threading
import random
import time

class CalcThread (threading.Thread):
    result = 0

    def __init__(self, vect, start_index, end_index):
        threading.Thread.__init__(self)
        self.vect = vect
        self.start_index = start_index
        self.end_index = end_index

    def return_result(self):
        return self.result

    def run(self):
        result = abs(self.vect[self.start_index])

        for i in range(self.start_index, self.end_index):
            result = max(result, abs(self.vect[i]))

class ThreadSample(threading.Thread):
    SIZE = [1000, 100000, 1000000]
    JOBS_NUMBER = [2, 8, 32]

    def entry_point(self):
        for size in range(len(self.SIZE)):
            for jobs in range(len(self.JOBS_NUMBER)):
                print("CURRENT SIZE: " + str(self.SIZE[size]) + "; NUMBER OF JOBS: " + str(self.JOBS_NUMBER[jobs]))

                # vect_current = np.linspace(0, 0, self.SIZE[size], dtype=int)
                vect_current = []
                for i in range(self.SIZE[size]):
                    vect_current.append(0)
                for i in range(self.SIZE[size]):
                    vect_current[i] = random.randint(-100000, 100000)
                
                serial_time_start = time.time()
                serial_result = abs(vect_current[0])

                for i in range(self.SIZE[size]):
                    serial_result = max(serial_result, abs(vect_current[i]))

                print("THE SERIAL RESULT IS: " + str(serial_result))
                serial_time = time.time() - serial_time_start
                print("THE TIME FOR WORK IS: " + str(serial_time))

                parallel_start = time.time()
                thread_set = []
                for i in range(self.JOBS_NUMBER[jobs]):
                    thread_set.append(CalcThread(vect_current, int(self.SIZE[size]/self.JOBS_NUMBER[jobs] * i), int(self.SIZE[size]) if i == self.JOBS_NUMBER[jobs] - 1 else int(self.SIZE[size]/self.JOBS_NUMBER[jobs] * (i + 1))))
                for thread in thread_set:
                    thread.start()
                for thread in thread_set:
                    thread.join()
                
                parallel_result = thread_set[0].return_result()

                for i in range(self.JOBS_NUMBER[jobs]):
                    parallel_result = max(parallel_result, thread_set[i].return_result())
                
                print("Parallel Result: " + str(parallel_result))
                parallel_time = time.time() - parallel_start
                print("Parallel Time: " + str(parallel_time))

                print("ACCELERATION: " + str(float(serial_time)/float(parallel_time)))
                print("EFFICIENCY: " + str(float(serial_time)/float(parallel_time)/self.JOBS_NUMBER[jobs]))
                
ThreadSample().entry_point()