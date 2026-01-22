package org.sample.pekko.sample2;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

public class StartStopActor2 extends AbstractBehavior<String> {

  private StartStopActor2(ActorContext<String> context) {
    super(context);
    getContext().getLog().info("second started");
  }

  static Behavior<String> create() {
    return Behaviors.setup(StartStopActor2::new);
  }

  @Override
  public Receive<String> createReceive() {
    return newReceiveBuilder().onSignal(PostStop.class, signal -> onPostStop()).build();
  }

  private Behavior<String> onPostStop() {
    getContext().getLog().info("second stopped");
    return this;
  }
}