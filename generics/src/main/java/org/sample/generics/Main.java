package org.sample.generics;

import org.sample.generics.core.*;
import org.sample.generics.extendz.AnimalPrinter;
import org.sample.generics.extendz.CarPrinter;
import org.sample.generics.method.GenericExtendsMethods;
import org.sample.generics.method.GenericMethods;
import org.sample.generics.method.TypeMethods;
import org.sample.generics.type.TypePrinter;
import org.sample.generics.wildcard.TypeWildcards;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main( String[] args ) {
        //Type parameters
        callTypes();

        callMethods();

        callTypeExtends();

          callWildcards();
    }


    static void callTypes(){
        //Type parameters
        TypePrinter<String> stringPrinter=new TypePrinter<>();
        TypePrinter<Integer> integerPrinter=new TypePrinter<>();

        //Call methods
        stringPrinter.print("Hello generics world.");
        integerPrinter.print(Integer.valueOf(1111));
    }

    static void callMethods(){
        TypeMethods<Double> doubleTypeMethods =new TypeMethods<>();

        doubleTypeMethods.print0(3.142324234);
        double value=doubleTypeMethods.print1(3.14);
        System.out.println(value);


        //========================================================
        //Generic methods
        //========================================================
        //Generic static methods
        GenericMethods.print0a(value);
        GenericMethods.print0a(value);

        //Generic instance methods
        GenericMethods genericMethods=new GenericMethods();
        genericMethods.print1b(value);
        genericMethods.print1a(value);



        //Generic static extends methods
        GenericExtendsMethods.processAnimal0(new Dog());
        GenericExtendsMethods.processCar0(new BMW());

        //Generic instance methods
        GenericExtendsMethods genericExtendsMethods=new GenericExtendsMethods();
        genericExtendsMethods.processAnimal1(new Cat());
        genericExtendsMethods.processCar1(new Tesla());



    }

    static void callTypeExtends(){
        //Car printer
        CarPrinter carPrinter=new CarPrinter<>();
        carPrinter.print(new Tesla());
        carPrinter.print(new BMW());

        //Animal printer
        AnimalPrinter animalPrinter=new AnimalPrinter<>();
        animalPrinter.print(new Cat());
        animalPrinter.print(new Dog());
    }

    static void callWildcards(){
        //Lower-bounded wildcard ? super Animal
        TypeWildcards.print1(new ArrayList<Animal>());

        //Upper-bounded wildcard ? extends Animal
        TypeWildcards typeWildcards=new TypeWildcards();
        typeWildcards.print0(new ArrayList<Cat>());
    }
}
