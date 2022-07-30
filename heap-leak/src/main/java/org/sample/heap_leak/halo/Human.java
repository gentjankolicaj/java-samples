package org.sample.heap_leak.halo;

import org.sample.heap_leak.halo.ability.Ability;

import java.util.List;
import java.util.Set;

public class Human {
    protected Set<Ability> abilities;


    public Set<Ability> getAbilities(){
        return abilities;
    }
}
