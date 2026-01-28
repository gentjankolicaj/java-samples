package org.sample.pekko.sample4;

import java.util.Optional;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;


public class Device extends AbstractBehavior<Device.Command> {


  private final String deviceId;
  private final String groupId;

  private Optional<Double> lastTemperatureRead = Optional.empty();

  public Device(ActorContext<Device.Command> actorContext, String deviceId, String groupId) {
    super(actorContext);
    this.deviceId = deviceId;
    this.groupId = groupId;
    getContext().getLog().info("Device actor {}-{} started.", groupId, deviceId);
  }

  public static Behavior<Device.Command> create(String deviceId, String groupId) {
    return Behaviors.setup(context -> new Device(context, deviceId, groupId));
  }

  @Override
  public Receive<Device.Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(RecordTemperature.class, this::onRecordTemperature)
        .onMessage(ReadTemperature.class, this::onReadTemperature)
        .onMessage(Passivate.class, m -> Behaviors.stopped())
        .onSignal(PostStop.class, signal -> onPostStop())
        .build();
  }

  private Behavior<Device.Command> onReadTemperature(ReadTemperature message) {
    message.replyTo().tell(new RespondTemperature(message.requestId, deviceId, lastTemperatureRead));
    return this;
  }

  private Behavior<Device.Command> onRecordTemperature(RecordTemperature message) {
    getContext().getLog().info("Recorded temperature reading {} with {}.", message.value, message.requestId);
    this.lastTemperatureRead = Optional.of(message.value());
    message.replyTo().tell(new RecordedTemperature(message.requestId));
    return this;
  }

  private Behavior<Device.Command> onPostStop() {
    getContext().getLog().info("Device actor {}-{} stopped.", groupId, deviceId);
    return this;
  }

  enum Passivate implements Command {
    INSTANCE
  }

  public interface Command {

  }

  public record ReadTemperature(long requestId, ActorRef<RespondTemperature> replyTo) implements Command {

  }

  public record RespondTemperature(long requestId, String deviceId, Optional<Double> value) implements Command {

  }

  public record RecordTemperature(long requestId, Double value, ActorRef<RecordedTemperature> replyTo) implements Command {

  }

  public record RecordedTemperature(long requestId) implements Command {

  }

}
