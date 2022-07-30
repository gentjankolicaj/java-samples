package org.sample.generics.type;

//In java type parameters are defined <T>
public class TypePrinter<T> {


    public void print(T t){
        System.out.println(t);
    }
}
