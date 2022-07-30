package org.sample.heap_leak.halo;

import org.sample.heap_leak.halo.ability.Ability;

import java.util.List;
import java.util.Set;

public class Human {
    protected Set<? extends Ability> abilities;


    public Set<? extends Ability> getAbilities(){
        return abilities;
    }
}
