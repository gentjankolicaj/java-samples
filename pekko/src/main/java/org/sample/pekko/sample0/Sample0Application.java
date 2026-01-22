package org.sample.pekko.sample0;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.ActorSystem;
import org.sample.pekko.sample0.GreeterGuardian.SayHello;

/**
 *
 * @author gentjan kolicaj
 * @since 1/19/26 5:05 PM
 *
 */
@Slf4j
public class Sample0Application {

  public static void main(String[] args) {
    //create actor system with guardian actor
    final ActorSystem<GreeterGuardian.SayHello> actorSystem = ActorSystem.create(
        GreeterGuardian.create(), "greeter-guardian");

    //greeter-guardian send message
    actorSystem.tell(new SayHello("greeter-bot", "hello bot-0"));

    try {
      log.info(">>> Press ENTER to exit <<<");
      System.in.read(); //block until input available.
    } catch (IOException e) {
      log.error("", e);
    } finally {
      actorSystem.terminate();
    }

  }

}
