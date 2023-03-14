package org.sample.generics.wildcard;

import lombok.extern.slf4j.Slf4j;
import org.sample.generics.core.Animal;

import java.util.List;

@Slf4j
public class TypeWildcards {

    public static void print1(List<? super Animal> list){
        for(Object el:list){
            log.info("{}", el);
        }
    }

    public void print0(List<? extends Animal> list){
        for(Animal var:list){
            log.info("{}", var);
        }
    }
}
