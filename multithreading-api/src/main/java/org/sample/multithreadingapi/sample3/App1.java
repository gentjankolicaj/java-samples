package org.sample.multithreadingapi.sample3;

public class App1 {

  private int count;

  public App1() {
    super();
  }

  public static void main(String[] args) {

    App1 app1 = new App1();
    app1.doWork();

  }


  public void doWork() {

    Thread t1 = new Thread(new Runnable() {

      public void run() {

        for (int i = 0; i < 100000; i++) {
          increment();
        }

      }
    });

    Thread t2 = new Thread(new Runnable() {

      public void run() {

        for (int i = 0; i < 100000; i++) {
          increment();
        }

      }
    });

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println(count);
  }


  public synchronized void increment() { //by using synchronized keyword i get intrinsic lock for count variable each time i call method increment
    count++;
  }

}
