package org.sample.generics.extendz;

import org.sample.generics.core.Car;

//Every type must be child of Car class
public class CarPrinter<T extends Car>{

    public void print(T t){
        System.out.println(t);
    }
}
