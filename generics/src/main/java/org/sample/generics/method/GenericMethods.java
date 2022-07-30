package org.sample.generics.method;

public class GenericMethods {

    /**
     *  Generic static methods
     */
    public static <T> void print0a(T... t){
        System.out.println("This static method has generic param : "+t+" "+t.getClass());
    }

    public static <T> T[] print0b(T... t){
        System.out.println("This static method has generic param & return : "+t+" "+t.getClass());
        return t;
    }


    /**
     * Generic instance methods
     *
     */
    public <T> void print1a(T t){
        System.out.println("This instance method has generic param : "+t+" , "+t.getClass());
    }

    public <T> T print1b(T t){
        System.out.println("This instance method has generic param & return :  "+t+" , "+t.getClass()   );
        return t;
    }
}
