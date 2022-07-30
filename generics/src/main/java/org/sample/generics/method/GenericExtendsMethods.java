package org.sample.generics.method;

import org.sample.generics.core.Animal;
import org.sample.generics.core.Car;

public class GenericExtendsMethods {


    public static <T extends Animal> T processAnimal0(T t){
        System.out.println("This static method has T extends Animal for (return & param) "+t+" , "+t.getClass());
        return t;
    }

    public static <T extends Car> T processCar0(T t){
        System.out.println("This static method has T extends Car for (return & param) "+t+" , "+t.getClass());
        return t;
    }

    public <T extends Animal> T processAnimal1(T t){
        System.out.println("This instance method has T extends Animal for (return & param) "+t+" , "+t.getClass());
        return t;
    }

    public  <T extends Car> T processCar1(T t){
        System.out.println("This instance method has T extends Car for (return & param) "+t+" , "+t.getClass());
        return t;
    }


}
