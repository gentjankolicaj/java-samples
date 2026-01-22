package org.sample.pekko.sample2;

import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

@Slf4j
public class ParentActor extends AbstractBehavior<String> {

  private final ActorRef<String> child;

  public ParentActor(ActorContext<String> context) {
    super(context);
    getContext().getLog().info("Spawning first...");
    this.child = context.spawn(StartStopActor1.create(), "child");
  }

  public static Behavior<String> create() {
    return Behaviors.setup(ParentActor::new);
  }

  @Override
  public Receive<String> createReceive() {
    return newReceiveBuilder()
        .onMessage(String.class, this::onMessage)
        .build();
  }


  private Behavior<String> onMessage(String message) {
    if (message.equalsIgnoreCase("stop")) {
      child.tell(message);
      return Behaviors.stopped();
    } else {
      return Behaviors.same();
    }
  }
}
