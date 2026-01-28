package org.sample.pekko.sample0;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

/**
 *
 * @author gentjan kolicaj
 * @since 1/19/26 5:18 PM
 *
 */
public class Greeter extends AbstractBehavior<Greet> {


  public Greeter(ActorContext<Greet> context) {
    super(context);
  }

  public static Behavior<Greet> create() {
    return Behaviors.setup(Greeter::new);
  }

  @Override
  public Receive<Greet> createReceive() {
    return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
  }

  private Behavior<Greet> onGreet(Greet greet) {
    getContext().getLog().info("whom '{}' | replyTo: '{}'", greet.whom(), greet.replyTo());

    //get actor ref on message and resend message to him.
    //greet.replyTo() returns replyTo of greeterBot.
    greet.replyTo().tell(new Greeted(greet.whom(), getContext().getSelf()));

    return this;
  }

}
