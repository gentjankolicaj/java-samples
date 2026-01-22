package org.sample.pekko.sample3;

import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.SupervisorStrategy;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

@Slf4j
public class SupervisingActor extends AbstractBehavior<String> {

  private final ActorRef<String> child;

  private SupervisingActor(ActorContext<String> context) {
    super(context);
    child = context.spawn(Behaviors.supervise(SupervisedActor.create())
        .onFailure(SupervisorStrategy.restart()), "supervised-actor");
  }

  static Behavior<String> create() {
    return Behaviors.setup(SupervisingActor::new);
  }

  @Override
  public Receive<String> createReceive() {
    return newReceiveBuilder().onMessageEquals("failChild", this::onFailChild).build();
  }

  private Behavior<String> onFailChild() {
    child.tell("fail");
    getContext().getLog().info("Supervising actor sent 'fail' message.");
    return this;
  }

}
