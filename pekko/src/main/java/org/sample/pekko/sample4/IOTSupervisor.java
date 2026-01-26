package org.sample.pekko.sample4;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;
import org.sample.pekko.sample4.IOTSupervisor.SupervisorCommand;

/**
 * user guardian actor of our application
 */
public class IOTSupervisor extends AbstractBehavior<SupervisorCommand> {

  public static String ACTOR_NAME = "iot-supervisor";

  public IOTSupervisor(ActorContext<SupervisorCommand> context) {
    super(context);
    getContext().getLog().info("IOT application started.");
  }

  public static Behavior<SupervisorCommand> create() {
    return Behaviors.setup(IOTSupervisor::new);
  }

  public static String name() {
    return ACTOR_NAME;
  }

  @Override
  public Receive<SupervisorCommand> createReceive() {
    return newReceiveBuilder()
        .onMessage(CreateDeviceCommand.class, this::onCreateDevice)
        .onSignal(PostStop.class, this::onPostStop)
        .build();
  }

  private Behavior<SupervisorCommand> onCreateDevice(CreateDeviceCommand createDeviceCommand) {
    String deviceId = createDeviceCommand.deviceId();
    String groupId = createDeviceCommand.groupId();
    String actorName = String.format("iot-%s-%s", deviceId, groupId);
    getContext().spawn(Device.create(deviceId, groupId), actorName);
    return this;
  }

  private Behavior<SupervisorCommand> onPostStop(PostStop signal) {
    getContext().getLog().info("IOT application stopped.");
    return this;
  }

  //========================================================================================================================================
  //Supervisor message definition
  //========================================================================================================================================

  public interface SupervisorCommand {

  }

  public record CreateDeviceCommand(String deviceId, String groupId) implements SupervisorCommand {

  }
}
