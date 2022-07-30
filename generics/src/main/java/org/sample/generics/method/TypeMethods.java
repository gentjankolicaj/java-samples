package org.sample.generics.method;

public class TypeMethods<T>{

    T t;

    public void print0(T t){
        System.out.println(t);
    }


    public T print1(T t){
        return t;
    }
}
