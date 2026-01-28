package org.sample.pekko.sample4;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;
import org.apache.pekko.actor.typed.javadsl.TimerScheduler;
import org.sample.pekko.sample4.DeviceManager.RespondAllTemperatures;
import org.sample.pekko.sample4.DeviceManager.Temperature;

/**
 *
 * @author gentjan kolicaj
 * @since 1/27/26 4:56 PM
 *
 */
public class DeviceGroupQuery extends AbstractBehavior<DeviceGroupQuery.Command> {

  private final long requestId;
  private final ActorRef<DeviceManager.RespondAllTemperatures> requester;
  private final Map<String, DeviceManager.TemperatureReading> repliesSoFar = new HashMap<>();
  private final Set<String> stillWaiting;

  public DeviceGroupQuery(Map<String, ActorRef<Device.Command>> deviceActors, long requestId,
      ActorRef<DeviceManager.RespondAllTemperatures> requester, Duration timeout,
      ActorContext<DeviceGroupQuery.Command> context,
      TimerScheduler<DeviceGroupQuery.Command> timers) {
    super(context);
    this.requestId = requestId;
    this.requester = requester;

    //timers
    timers.startSingleTimer("", CollectionTimeout.INSTANCE, timeout);

    //convert Device.RespondTemperature of device actor to DeviceGroupQuery message
    ActorRef<Device.RespondTemperature> respondTemperatureAdapter =
        context.messageAdapter(Device.RespondTemperature.class, WrappedRespondTemperature::new);

    deviceActors.forEach((key, value) -> {
      context.watchWith(value, new DeviceTerminated(key));
      value.tell(new Device.ReadTemperature(0L, respondTemperatureAdapter));
    });

    stillWaiting = new HashSet<>(deviceActors.keySet());
  }

  public static Behavior<Command> create(
      Map<String, ActorRef<Device.Command>> deviceIdToActor,
      long requestId,
      ActorRef<DeviceManager.RespondAllTemperatures> requester,
      Duration timeout) {
    return Behaviors.setup(
        context ->
            Behaviors.withTimers(
                timers ->
                    new DeviceGroupQuery(
                        deviceIdToActor, requestId, requester, timeout, context, timers)));
  }

  @Override
  public Receive<Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(WrappedRespondTemperature.class, this::onWrappedRespondTemperature)
        .onMessage(DeviceTerminated.class, this::onDeviceTerminated)
        .onMessage(CollectionTimeout.class, this::onCollectionTimeout)
        .build();
  }

  private Behavior<Command> onCollectionTimeout(CollectionTimeout timeout) {
    for (String deviceId : stillWaiting) {
      repliesSoFar.put(deviceId, DeviceManager.DeviceTimedOut.INSTANCE);
    }
    stillWaiting.clear();
    return respondWhenAllCollected();
  }

  private Behavior<Command> onDeviceTerminated(DeviceTerminated terminated) {
    if (stillWaiting.contains(terminated.deviceId)) {
      repliesSoFar.put(terminated.deviceId, DeviceManager.DeviceNotAvailable.INSTANCE);
      stillWaiting.remove(terminated.deviceId);
    }
    return respondWhenAllCollected();
  }

  private Behavior<Command> onWrappedRespondTemperature(WrappedRespondTemperature message) {
    DeviceManager.TemperatureReading reading = message.response.value()
        .map(value -> (DeviceManager.TemperatureReading) new Temperature(value))
        .orElse(DeviceManager.TemperatureNotAvailable.INSTANCE);

    String deviceId = message.response.deviceId();
    repliesSoFar.put(deviceId, reading);
    stillWaiting.remove(deviceId);
    return respondWhenAllCollected();
  }

  private Behavior<Command> respondWhenAllCollected() {
    if (stillWaiting.isEmpty()) {
      requester.tell(new RespondAllTemperatures(requestId, repliesSoFar));
      return Behaviors.stopped();
    } else {
      return this;
    }
  }

  public enum CollectionTimeout implements Command {
    INSTANCE
  }

  public interface Command {

  }

  public record WrappedRespondTemperature(Device.RespondTemperature response) implements Command {

  }

  public record DeviceTerminated(String deviceId) implements Command {

  }

}
