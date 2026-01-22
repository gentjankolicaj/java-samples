package org.sample.pekko.sample2;

import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

@Slf4j
public class StartStopActor1 extends AbstractBehavior<String> {

  private StartStopActor1(ActorContext<String> context) {
    super(context);
    getContext().getLog().info("first started.");
    getContext().getLog().info("Spawning second...");
    context.spawn(StartStopActor2.create(), "second");
  }

  static Behavior<String> create() {
    return Behaviors.setup(StartStopActor1::new);
  }

  @Override
  public Receive<String> createReceive() {
    return newReceiveBuilder()
        .onMessageEquals("stop", Behaviors::stopped)
        .onSignal(PostStop.class, signal -> onPostStop())
        .build();
  }

  private Behavior<String> onPostStop() {
    getContext().getLog().info("first stopped");
    return this;
  }
}