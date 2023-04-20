package org.sample.heapleak.halo.ability;

public abstract class Ability implements Comparable<Ability> {

  protected int abilityValue;

  @Override
  public int compareTo(Ability ability) {
    return abilityValue - ability.abilityValue;
  }
}
