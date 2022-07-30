package org.sample.heap_leak;

import org.sample.heap_leak.halo.Spartan;
import org.sample.heap_leak.halo.SpartanThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class Main {
    static final Random random=new Random();
    static final long maxIndex=10000000L;
    static long index=0;

    static final List<Spartan> STATIC_SPARTANS=new ArrayList<>();
     static final List<Class<? extends Spartan>> STATIC_SPARTANS_CLS=new ArrayList<>();

     final List<Spartan> spartans=new ArrayList<>();
     final List<Class<? extends Spartan>> spartansCls=new ArrayList<>();

    public static void main( String[] args ) {
       Thread halo0=new SpartanThread("halo_0");
       Thread halo1=new SpartanThread("halo_1");

       //Start threads
        startThreads(halo0,halo1);


       //Create main object
        //Populate with spartans
        Main main=new Main();

        trainSpartans(main);

    }

    static void trainSpartans(Main main){
            while(index<maxIndex){
                try{
                    //Allocate spartan objects
                    Spartan vannak134=new Spartan(index,"vannak","134","Lieutenant","UNSC");
                    Spartan riz028=new Spartan(index+1,"riz","028","Lieutenant","UNSC");

                    //Add references to static collection
                    STATIC_SPARTANS.add(vannak134);
                    STATIC_SPARTANS_CLS.add(vannak134.getClass());

                    //Add references to instance collection
                    main.spartans.add(riz028);
                    main.spartansCls.add(riz028.getClass());

                     //Thread sleep of main[] randomly
                     int value= random.nextInt(100);
                     if(value==10){
                         Thread.sleep(3);
                     }
                }catch (Exception e){

                }finally {
                    index+=2;
                }

            }
    }

    static void startThreads(Thread...args){
        for(Thread var:args)
            var.start();
    }


}
