package org.sample.pekko.sample4;

import java.util.Optional;
import org.apache.pekko.actor.typed.ActorRef;

@SuppressWarnings("unused")
public class Device {

  public interface Command{

  }

  public record ReadTemperature(ActorRef<String> actorRef) implements Command {

  }

  public record RespondTemperature(Optional<Double> value) {

  }

}
