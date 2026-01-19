package org.sample.pekko.sample0;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

/**
 *
 * @author gentjan kolicaj
 * @since 1/19/26 5:54 PM
 *
 */
public class GreeterGuardian extends AbstractBehavior<GreeterGuardian.SayHello> {

  private final ActorRef<Greet> greeter;

  public GreeterGuardian(ActorContext<SayHello> context) {
    super(context);

    //create greeter actor
    greeter = context.spawn(Greeter.create(), "greeter");

  }

  public static Behavior<GreeterGuardian.SayHello> create() {
    return Behaviors.setup(GreeterGuardian::new);
  }

  //GreeterRoot:
  // When it receives message:
  // 1.It creates GreeterBot actor
  // 2.Tells Greeter (actor) to greet GreeterBot (actor)
  @Override
  public Receive<SayHello> createReceive() {
    return newReceiveBuilder().onMessage(SayHello.class, this::onSayHello).build();
  }

  private Behavior<SayHello> onSayHello(SayHello sayHello) {
    //spawn greeterBot actor
    ActorRef<Greeted> greeterBot = getContext().spawn(GreeterBot.create(7), sayHello.name);

    //message from greeter actor to greeterBot
    greeter.tell(new Greet(sayHello.message, greeterBot));

    return this;
  }

  public record SayHello(String name, String message) {

  }

}
