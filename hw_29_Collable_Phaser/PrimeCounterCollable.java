package de.telran.hw_29_Collable_Phaser;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;



    public class PrimeCounterCollable {
        //  1 уровень сложности: 1. Напишите программу просчета количества всех
        //  простых в диапазоне до 1 000 000.
        //Просчет выполняйте в 4 потоках, которые создаются с помощью Callable.
        //Получите результаты подсчета из каждого потока и напечатайте общий результат.

        public static void main(String[] args) {
            int maxNum = 1_000_000;
            int numberOfThreads = 4;     // Количество потоков

            int range = maxNum / numberOfThreads;    // диапазон каждого потока

            FutureTask<Integer>[] tasks = new FutureTask[numberOfThreads];   // Создаем массив задач
            Thread[] threads = new Thread[numberOfThreads];  // Создаем массив потоков

            for (int i = 0; i < numberOfThreads; i++) {
                int start = i * range + 1;
                int end =(i + 1) * range;
                tasks[i] = new FutureTask<>(new PrimeTask(start, end));
                threads[i] = new Thread(tasks[i]);
                threads[i].start();  // Запуск потока
            }

            int sumCount = 0;
            // Получаем результаты из каждого потока
            for (int i = 0; i < numberOfThreads; i++) {
                try {
                    sumCount += tasks[i].get(); // Получаем результат выполнения задачи
                    System.out.println(" Количество простых чисел , посчитанных потоком номер_ "+ i+ " равно = "+ tasks[i].get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(" Итоговое количество простых чисел в диаппазоне 0..1_000_000 равно: " + sumCount);
        }
    }

    // Класс задачи для поиска простых чисел в указанном диапазоне
    class PrimeTask implements Callable<Integer> {
        private final int start;
        private final int end;

        public PrimeTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer call() {
            int primeCount = 0;
            for (int i = start; i <= end; i++) {
                if (isPrime(i)) {
                    primeCount++;
                }
            }
            return primeCount;
        }

        private boolean isPrime(int number) {
            if (number < 2) return false;
            for (int i = 2; i < number; i++) {
                if (number % i == 0) return false;
            }
            return true;
        }
    }

