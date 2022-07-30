package org.sample.heap_leak.halo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sample.heap_leak.halo.ability.Ability;
import org.sample.heap_leak.halo.ability.MentalAbility;
import org.sample.heap_leak.halo.ability.PhysicalAbility;
import org.sample.heap_leak.halo.lab.Augmentation;
import org.sample.heap_leak.halo.lab.procedure.BioChemical;
import org.sample.heap_leak.halo.lab.procedure.BioMechanical;
import org.sample.heap_leak.halo.lab.procedure.Procedure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Spartan extends Human implements Augmentation {
    private Long counter;
    private String name;
    private String tag;
    private String rank;
    private String affiliation;
    private List<Procedure> procedures;


    //INSTANCE BLOCK => is executed before constructor
    {
        //Add augmentation procedures
        if(procedures==null) {
            procedures = new ArrayList<>();
            procedures.add(new BioChemical());
            procedures.add(new BioMechanical());
        }

        if(abilities==null){
            abilities=new TreeSet<>();
            abilities.add(new MentalAbility());
            abilities.add(new PhysicalAbility());
        }


    }



    @Override
    public List<Procedure> getProcedures() {
        return procedures;
    }

    @Override
    public Set<Ability> getAbilities() {
        return super.getAbilities();
    }
}

