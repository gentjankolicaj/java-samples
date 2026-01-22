package org.sample.pekko.sample1;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.ActorSystem;

/**
 *
 * @author gentjan kolicaj
 * @since 1/21/26 5:39 PM
 *
 */
@Slf4j
public class Sample1Application {

  public static void main(String[] args) {
    try (ActorSystem<PrintMessage> actorSystem = ActorSystem.create(RootPrintActorRefActor.create(),
        "print-system")) {

      //send message
      actorSystem.tell(new PrintMessage.Empty());

      log.info(">>> Press ENTER to exit <<<");
      System.in.read(); //block until input available.
    } catch (IOException | TimeoutException e) {
      log.error("", e);


    }

  }

}
