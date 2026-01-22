package org.sample.pekko.sample1;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

public class RootPrintActorRefActor extends AbstractBehavior<PrintMessage> {

  public RootPrintActorRefActor(ActorContext<PrintMessage> context) {
    super(context);
  }

  public static Behavior<PrintMessage> create() {
    return Behaviors.setup(RootPrintActorRefActor::new);
  }

  @Override
  public Receive<PrintMessage> createReceive() {
    return newReceiveBuilder().onMessage(PrintMessage.class, this::onPrintRequest).build();
  }

  private Behavior<PrintMessage> onPrintRequest(PrintMessage printMessage) {
    ActorRef<PrintMessage> actorRef = getContext().spawn(PrintActorRefActor.create(),
        "print-actor");
    getContext().getLog().info("Root spanned actor:{}", actorRef);
    getContext().getLog().info("Sending print message...");
    actorRef.tell(new PrintMessage.Empty());
    return Behaviors.same();
  }

}
