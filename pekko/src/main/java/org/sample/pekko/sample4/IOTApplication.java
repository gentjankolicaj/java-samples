package org.sample.pekko.sample4;

import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.ActorSystem;

@Slf4j
public class IOTApplication {

  public static void main(String[] args){
    try(ActorSystem<String> actorSystem=ActorSystem.create(IOTSupervisor.create(),IOTSupervisor.name())){

      actorSystem.tell("hello");

      actorSystem.tell("world");

      log.info(">>> Press ENTER to finish <<<");
      System.in.read(); //blocking read
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
