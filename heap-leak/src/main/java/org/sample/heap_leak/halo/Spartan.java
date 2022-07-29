package org.sample.heap_leak.halo;

import org.sample.heap_leak.halo.ability.Ability;
import org.sample.heap_leak.halo.augmentation.Augmentation;
import org.sample.heap_leak.halo.augmentation.Procedure;

import java.util.List;

public class Spartan extends Human implements Augmentation {


    @Override
    public List<? extends Procedure> getProcedures() {
        return null;
    }

    @Override
    public List<? extends Ability> getAbilities() {
        return super.getAbilities();
    }
}

