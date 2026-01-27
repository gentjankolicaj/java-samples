package org.sample.pekko.sample4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;
import org.sample.pekko.sample4.DeviceManager.Command;

/**
 *
 * @author gentjan kolicaj
 * @since 1/24/26 7:03 PM
 *
 */
public class DeviceManager extends AbstractBehavior<Command> {

  private final Map<String, ActorRef<DeviceGroup.Command>> groupIdToActor = new HashMap<>();

  private DeviceManager(ActorContext<Command> context) {
    super(context);
    context.getLog().info("DeviceManager started.");
  }

  public static Behavior<Command> create() {
    return Behaviors.setup(DeviceManager::new);
  }

  private DeviceManager onRequestTrackDevice(RequestTrackDevice message) {
    String groupId = message.groupId;
    ActorRef<DeviceGroup.Command> ref = groupIdToActor.get(groupId);
    if (ref != null) {
      ref.tell(message);
    } else {
      getContext().getLog().info("Creating device group actor for {}", groupId);
      ActorRef<DeviceGroup.Command> groupActor = getContext().spawn(DeviceGroup.create(groupId), "group-" + groupId);
      getContext().watchWith(groupActor, new DeviceGroupTerminated(groupId));
      groupActor.tell(message);
      groupIdToActor.put(groupId, groupActor);
    }
    return this;
  }

  private DeviceManager onRequestDeviceList(RequestDeviceList message) {
    ActorRef<DeviceGroup.Command> ref = groupIdToActor.get(message.groupId);
    if (ref != null) {
      ref.tell(message);
    } else {
      message.replyTo.tell(new ReplyDeviceList(message.requestId, Collections.emptySet()));
    }
    return this;
  }

  private DeviceManager onDeviceGroupTerminated(DeviceGroupTerminated message) {
    getContext().getLog().info("Device group actor for {} has been terminated", message.groupId);
    groupIdToActor.remove(message.groupId);
    return this;
  }

  public Receive<Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(RequestTrackDevice.class, this::onRequestTrackDevice)
        .onMessage(RequestDeviceList.class, this::onRequestDeviceList)
        .onMessage(DeviceGroupTerminated.class, this::onDeviceGroupTerminated)
        .onSignal(PostStop.class, signal -> onPostStop())
        .build();
  }

  private DeviceManager onPostStop() {
    getContext().getLog().info("DeviceManager stopped.");
    return this;
  }

  public interface Command {

  }

  public record RequestTrackDevice(String groupId, String deviceId, ActorRef<DeviceRegistered> replyTo)
      implements Command, DeviceGroup.Command {

  }

  public record DeviceRegistered(ActorRef<Device.Command> device) implements Command {

  }

  public record RequestDeviceList(long requestId, String groupId, ActorRef<ReplyDeviceList> replyTo)
      implements Command, DeviceGroup.Command {

  }

  public record ReplyDeviceList(long requestId, Set<String> ids) implements Command {

  }

  public record DeviceGroupTerminated(String groupId) implements Command {

  }

}
