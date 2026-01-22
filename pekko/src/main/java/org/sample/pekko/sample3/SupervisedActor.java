package org.sample.pekko.sample3;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.PostStop;
import org.apache.pekko.actor.typed.PreRestart;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

public class SupervisedActor extends AbstractBehavior<String> {

  static Behavior<String> create() {
    return Behaviors.setup(SupervisedActor::new);
  }

  private SupervisedActor(ActorContext<String> context) {
    super(context);
    getContext().getLog().info("Supervised actor started.");
  }

  @Override
  public Receive<String> createReceive() {
    return newReceiveBuilder()
        .onMessageEquals("fail", this::fail)
        .onSignal(PreRestart.class, signal -> preRestart())
        .onSignal(PostStop.class, signal -> postStop())
        .build();
  }

  private Behavior<String> fail() {
    getContext().getLog().info("Supervised actor fails now.");
    throw new RuntimeException("Supervised actor: I failed!");
  }

  private Behavior<String> preRestart() {
    getContext().getLog().info("Supervised actor will be restarted...");
    return this;
  }

  private Behavior<String> postStop() {
    getContext().getLog().info("Supervised actor stopped.");
    return this;
  }

}