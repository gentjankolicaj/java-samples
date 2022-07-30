package org.sample.heap_leak.halo.ability;

public abstract class Ability implements Comparable<Ability>{
     protected int abilityValue;
    @Override
    public int compareTo(Ability ability) {
        return abilityValue-ability.abilityValue;
    }
}
