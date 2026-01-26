package org.sample.pekko.sample4;

import lombok.extern.slf4j.Slf4j;
import org.apache.pekko.actor.typed.ActorSystem;
import org.sample.pekko.sample4.IOTSupervisor.CreateDeviceCommand;
import org.sample.pekko.sample4.IOTSupervisor.SupervisorCommand;

@Slf4j
public class IOTApplication {

  public static void main(String[] args) {
    try (ActorSystem<SupervisorCommand> actorSystem = ActorSystem.create(IOTSupervisor.create(), IOTSupervisor.name())) {

      actorSystem.tell(new CreateDeviceCommand("0", "0"));
      actorSystem.tell(new CreateDeviceCommand("1", "0"));
      actorSystem.tell(new CreateDeviceCommand("2", "0"));
      actorSystem.tell(new CreateDeviceCommand("3", "0"));

      log.info(">>> Press ENTER to finish <<<");
      System.in.read(); //blocking read
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
