package org.sample.generics.extendz;

import org.sample.generics.core.Animal;

public class AnimalPrinter<T extends Animal>{

    public void print(T t){
        System.out.println(t);
    }
}
