package org.sample.generics.wildcard;

import org.sample.generics.core.Animal;


import java.util.List;

public class TypeWildcards {

    public void print0(List<? extends Animal> list){
        for(Animal var:list){
            System.out.println(var);
        }
    }

    public static void print1(List<? super Animal> list){
        for(Object el:list){
            System.out.println(el);
        }
    }
}
