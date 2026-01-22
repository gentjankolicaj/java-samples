package org.sample.pekko.sample3;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.ActorSystem;

@Slf4j
public class Sample3Application {

  public static void main(String[] args) {
    try (ActorSystem<String> actorSystem = ActorSystem.create(SupervisingActor.create(),
        "supervising-actor-sample3")) {

      //send message
      actorSystem.tell("failChild");

      log.info(">>> Press ENTER to exit <<<");
      System.in.read(); //block until input available.
    } catch (IOException | TimeoutException e) {
      log.error("", e);
    }
  }

}
