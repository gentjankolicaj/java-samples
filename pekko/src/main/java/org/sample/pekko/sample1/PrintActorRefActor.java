package org.sample.pekko.sample1;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

public class PrintActorRefActor extends AbstractBehavior<PrintMessage> {

  public PrintActorRefActor(ActorContext<PrintMessage> context) {
    super(context);
  }

  public static Behavior<PrintMessage> create() {
    return Behaviors.setup(PrintActorRefActor::new);
  }

  @Override
  public Receive<PrintMessage> createReceive() {
    return newReceiveBuilder().onMessage(PrintMessage.class, this::onPrintRequest).build();
  }

  private Behavior<PrintMessage> onPrintRequest(PrintMessage printMessage) {
    ActorRef<String> actorRef = getContext().spawn(Behaviors.empty(), "empty-actor");
    getContext().getLog().info("spanned actor:{}", actorRef);
    return this;
  }

}
