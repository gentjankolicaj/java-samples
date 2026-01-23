package org.sample.pekko.sample4;

import java.util.Optional;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;


public class IOTDevice extends AbstractBehavior<IOTDevice.Command> {


  private final String deviceId;
  private final String groupId;

  private Optional<Double> lastTemperatureRead = Optional.empty();

  public IOTDevice(ActorContext<IOTDevice.Command> actorContext, String deviceId, String groupId) {
    super(actorContext);
    this.deviceId = deviceId;
    this.groupId = groupId;
    getContext().getLog().info("IOT device started.");
  }

  public static Behavior<IOTDevice.Command> create(String deviceId, String groupId) {
    return Behaviors.setup(context -> new IOTDevice(context, deviceId, groupId));
  }

  @Override
  public Receive<IOTDevice.Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(WriteTemperature.class, this::onWriteTemperature)
        .onMessage(ReadTemperature.class, this::onReadTemperature)
        .onSignal(PostStop.class, signal -> onPostStop())
        .build();
  }

  private Behavior<IOTDevice.Command> onReadTemperature(ReadTemperature message) {
    message.actorRef().tell(new RespondTemperature(message.requestId, lastTemperatureRead));
    return this;
  }

  private Behavior<IOTDevice.Command> onWriteTemperature(WriteTemperature message) {
    this.lastTemperatureRead = Optional.of(message.value());
    return this;
  }

  private Behavior<IOTDevice.Command> onPostStop() {
    getContext().getLog().info("Device actor {}-{} stopped", groupId, deviceId);
    return this;
  }

  public interface Command {

  }

  public record ReadTemperature(long requestId, ActorRef<RespondTemperature> actorRef) implements Command {

  }

  public record RespondTemperature(long requestId, Optional<Double> value) {

  }

  public record WriteTemperature(Double value) implements Command {

  }

}
