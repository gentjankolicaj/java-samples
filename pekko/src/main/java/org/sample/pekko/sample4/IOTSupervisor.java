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
        .onMessage(CreateIOTDeviceCommand.class, this::onCreateIOTDevice)
        .onSignal(PostStop.class, this::onPostStop)
        .build();
  }

  private Behavior<SupervisorCommand> onCreateIOTDevice(CreateIOTDeviceCommand createIOTDeviceCommand) {
    String deviceId = createIOTDeviceCommand.deviceId();
    String groupId = createIOTDeviceCommand.groupId();
    String actorName = String.format("iot-%s-%s", deviceId, groupId);
    getContext().spawn(IOTDevice.create(deviceId, groupId), actorName);
    return this;
  }

  private Behavior<SupervisorCommand> onPostStop(PostStop signal) {
    getContext().getLog().info("IOT application stopped");
    return this;
  }

  //========================================================================================================================================
  //Superviso message definition
  //========================================================================================================================================

  public interface SupervisorCommand {

  }

  public record CreateIOTDeviceCommand(String deviceId, String groupId) implements SupervisorCommand {

  }
}
