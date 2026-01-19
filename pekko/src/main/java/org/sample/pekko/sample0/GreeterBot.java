package org.sample.pekko.sample0;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

/**
 *
 * @author gentjan kolicaj
 * @since 1/19/26 5:33 PM
 *
 */
public class GreeterBot extends AbstractBehavior<Greeted> {

  private final int maxGreet;
  private int greetCounter;

  public GreeterBot(ActorContext<Greeted> context, int maxGreet) {
    super(context);
    this.maxGreet = maxGreet;
  }

  public static Behavior<Greeted> create(int maxGreet) {
    return Behaviors.setup(context -> new GreeterBot(context, maxGreet));
  }

  @Override
  public Receive<Greeted> createReceive() {
    return newReceiveBuilder().onMessage(Greeted.class, this::onGreeted).build();
  }

  private Behavior<Greeted> onGreeted(Greeted message) {
    greetCounter++;
    getContext().getLog().info("Greeting {} for {} from {}", greetCounter, message.whom(), message.from());
    if (greetCounter == maxGreet) {
      return Behaviors.stopped();
    } else {
      message.from().tell(new Greet("Greeting " + greetCounter, getContext().getSelf()));
      return this;
    }
  }

}
