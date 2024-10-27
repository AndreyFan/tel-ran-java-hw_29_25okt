package de.telran.hw_29_Collable_Phaser;

import java.util.concurrent.Phaser;

public class MainBookPhaser {
    // 2*. Вы пишете книгу и вы автор. Есть 2 человека, которые выступают у вас соавторами.
    // На этапе написания книги, они пишут разные главы, помогая таким образом
    // вам полностью ее закончить.
    // После того как вы и все соавторы закончили свою работу, вы отправляете книгу
    // на рецензирование.
    // В группу рецензентов входит 3 человека, каждый из которых является специалистом в своей области
    // и проверяет Вашу книгу на правильность отражения фактов в вашем произведении.
    // После того. как все рецензии получены, Вы отправляете книгу в издательский дом.
    // В редакции главный редактор и руководитель издательства читают ваше творение и утверждают
    // его в печать.
    // Вы относите утвержденные рукописи печатникам, они печатают книгу, переплетчики делают ей переплет
    // а служба доставки развозят книги по магазинам.
    // Поздравляю Вас, вы полностью прошли все фазы становления известного писателя, т.к. Ваша книга
    // стала бестселлером! )
    // Создайте приложение, которое сымитирует работу процесса создания книги, с учетом что каждый человек,
    // который встречается в описанной схеме будет представлен отдельным потоком.
    // Какой синхронизатор с библиотеки concurrent мог бы быть Вам полезен при выполнении данной задачи?
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1); // регистрируем главный поток
        new Thread(new Co_Autor(phaser, "Первый соавтор")).start();
        new Thread(new Co_Autor(phaser, "Второй соавтор")).start();

        System.out.println(" Я пишу свою часть книги");
        phaser.arriveAndAwaitAdvance();  // ожидаю соавторов

        System.out.println();
        System.out.println(" Книга написана -->  отправляю книгу на рецензирование");
        System.out.println();
        new Thread(new Reviewer(phaser, "Первый рецензент")).start();
        new Thread(new Reviewer(phaser, "Второй рецензент")).start();
        new Thread(new Reviewer(phaser, "Третий рецензент")).start();

        phaser.arriveAndAwaitAdvance();// ожидаю рецензентов

        System.out.println();
        System.out.println(" Книга прошла рецензирование --> отправляю книгу в издательский дом ");
        System.out.println();
        new Thread(new Editor(phaser, "Главный редактор ")).start();
        new Thread(new Editor(phaser, "Руководитель ")).start();
        phaser.arriveAndAwaitAdvance(); // ожидаю редактирование

        System.out.println();
        System.out.println("отношу утвержденные рукописи печатникам");
        System.out.println();
        new Thread(new Typographer(phaser, "Печатники ")).start();
        System.out.println("Ожидаю печать книги");
        phaser.arriveAndAwaitAdvance();  // Ожидаю печать книги

        System.out.println();
        System.out.println(" Печатники отдают книгу переплётчикам ");
        new Thread(new Bookbinders(phaser, "Переплётчики ")).start();
        System.out.println("Ожидаю переплёт книги");
        phaser.arriveAndAwaitAdvance();  // Ожидаю переплёт книги

        System.out.println();
        System.out.println(" *** Служба доставки развозит книгу по магазинам --> становлюсь известным писателем *** ");
        phaser.arriveAndDeregister();
        System.out.println(" Принимаю поздравления ");

    }
}

class Co_Autor implements Runnable {
    Phaser phaser;
    String name;

    public Co_Autor(Phaser phaser, String name) {

        this.phaser = phaser;
        this.name = name;

        phaser.register(); // поток регистрируется в фазере
    }

    @Override
    public void run() {
        System.out.println(name + " начал работать");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(name + " написал свою часть книги");
        phaser.arriveAndDeregister(); // завершаем участие потока в фазере

    }
}

class Reviewer implements Runnable {
    Phaser phaser;
    String name;

    public Reviewer(Phaser phaser, String name) {

        this.phaser = phaser;
        this.name = name;

        phaser.register(); // поток регистрируется в фазере
    }

    @Override
    public void run() {
        System.out.println(name + " начал работать");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(name + " написал рецензию на часть книги");
        phaser.arriveAndDeregister(); // завершаем участие потока в фазере

    }
}

class Editor implements Runnable {
    Phaser phaser;
    String name;

    public Editor(Phaser phaser, String name) {

        this.phaser = phaser;
        this.name = name;

        phaser.register(); // поток регистрируется в фазере
    }

    @Override
    public void run() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(name + " издательства читает книгу и утверждает её в печать");
        phaser.arriveAndDeregister(); // завершаем участие потока в фазере

    }
}

class Typographer implements Runnable {
    Phaser phaser;
    String name;

    public Typographer(Phaser phaser, String name) {

        this.phaser = phaser;
        this.name = name;

        phaser.register(); // поток регистрируется в фазере
    }

    @Override
    public void run() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(name + " печатают книгу");
        phaser.arriveAndDeregister(); // завершаем участие потока в фазере

    }
}

class Bookbinders implements Runnable {
    Phaser phaser;
    String name;

    public Bookbinders(Phaser phaser, String name) {

        this.phaser = phaser;
        this.name = name;

        phaser.register(); // поток регистрируется в фазере
    }

    @Override
    public void run() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(name + " делают переплёт книги");
        phaser.arriveAndDeregister(); // завершаем участие потока в фазере

    }
}