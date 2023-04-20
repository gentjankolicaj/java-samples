package org.sample.heapleak.halo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import lombok.Data;
import org.sample.heapleak.halo.ability.Ability;
import org.sample.heapleak.halo.ability.MentalAbility;
import org.sample.heapleak.halo.ability.PhysicalAbility;
import org.sample.heapleak.halo.lab.Augmentation;
import org.sample.heapleak.halo.lab.procedure.BioChemical;
import org.sample.heapleak.halo.lab.procedure.BioMechanical;
import org.sample.heapleak.halo.lab.procedure.Procedure;


@Data
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
    if (procedures == null) {
      procedures = new ArrayList<>();
      procedures.add(new BioChemical());
      procedures.add(new BioMechanical());
    }

    if (abilities == null) {
      abilities = new TreeSet<>();
      abilities.add(new MentalAbility());
      abilities.add(new PhysicalAbility());
    }


  }


  public Spartan(Long counter, String name, String tag, String rank, String affiliation) {
    this.counter = counter;
    this.name = name;
    this.tag = tag;
    this.rank = rank;
    this.affiliation = affiliation;
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

