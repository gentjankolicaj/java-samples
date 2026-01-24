package org.sample.pekko.sample4;

import java.util.HashMap;
import java.util.Map;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;
import org.sample.pekko.sample4.DeviceManager.DeviceRegistered;

/**
 *
 * @author gentjan kolicaj
 * @since 1/24/26 7:12 PM
 *
 */
public class DeviceGroup extends AbstractBehavior<DeviceGroup.Command> {

  private final String groupId;
  private final Map<String, ActorRef<Device.Command>> deviceActors = new HashMap<>();

  public DeviceGroup(ActorContext<DeviceGroup.Command> context, String groupId) {
    super(context);
    this.groupId = groupId;
    getContext().getLog().info("DeviceGroup {} started", groupId);
  }

  public static Behavior<DeviceGroup.Command> create(String groupId) {
    return Behaviors.setup(context -> new DeviceGroup(context, groupId));
  }

  @Override
  public Receive<DeviceGroup.Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(DeviceManager.RequestTrackDevice.class, this::onTrackDevice)
        .onMessage(DeviceManager.RequestDeviceList.class, msg -> msg.groupId().equals(groupId), this::onDeviceList)
        .onMessage(DeviceTerminated.class, this::onTerminated)
        .onSignal(PostStop.class, this::onPostStop)
        .build();
  }

  private Behavior<DeviceGroup.Command> onTrackDevice(DeviceManager.RequestTrackDevice trackMsg) {
    if (this.groupId.equals(trackMsg.groupId())) {
      ActorRef<Device.Command> deviceActor = deviceActors.get(trackMsg.deviceId());
      if (deviceActor != null) {
        trackMsg.replyTo().tell(new DeviceRegistered(deviceActor));
      } else {
        getContext().getLog().info("Creating device actor for {}", trackMsg.deviceId());
        deviceActor = getContext().spawn(Device.create(trackMsg.deviceId(), trackMsg.groupId()),
            String.format("device-%s-%s", trackMsg.deviceId(), trackMsg.groupId()));
        deviceActors.put(trackMsg.groupId(), deviceActor);
        trackMsg.replyTo().tell(new DeviceRegistered(deviceActor));
      }
    } else {
      getContext().getLog().warn("Ignoring TrackDevice request for {}. This actor is responsible for {}.", groupId, this.groupId);
    }
    return this;
  }

  private DeviceGroup onDeviceList(DeviceManager.RequestDeviceList msg) {
    msg.replyTo().tell(new DeviceManager.ReplyDeviceList(msg.requestId(), deviceActors.keySet()));
    return this;
  }

  private DeviceGroup onTerminated(DeviceTerminated t) {
    getContext().getLog().info("Device actor for {} has been terminated", t.deviceId);
    deviceActors.remove(t.deviceId);
    return this;
  }

  private Behavior<DeviceGroup.Command> onPostStop(PostStop signal) {
    getContext().getLog().info("DeviceGroup {} stopped with signal {}", groupId, signal);
    return this;
  }

  public interface Command {

  }

  public record DeviceTerminated(String deviceId, String groupId, ActorRef<Device.Command> device) implements Command {

  }

}
