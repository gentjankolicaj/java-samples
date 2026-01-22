package org.sample.pekko.sample4;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.PostStop$;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

/**
 *  user guardian actor of our application
 */
public class IOTSupervisor extends AbstractBehavior<String> {
 public static String ACTOR_NAME="iot-supervisor";

  public IOTSupervisor( ActorContext<String> context) {
    super(context);
    getContext().getLog().info("IOT application started.");
  }

  @Override
  public Receive<String> createReceive() {
    return newReceiveBuilder()
        .onSignal(PostStop.class,this::onPostStop)
        .build();
  }

  private Behavior<String> onPostStop(PostStop signal){
    getContext().getLog().info("IOT application stopped");
    return this;
  }

  public static Behavior<String> create(){
    return Behaviors.setup(IOTSupervisor::new);
  }

  public static String name(){
    return ACTOR_NAME;
  }
}
