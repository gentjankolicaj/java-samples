package org.sample.pekko.sample4;


import static org.assertj.core.api.Assertions.assertThat;

import org.apache.pekko.actor.testkit.typed.javadsl.ActorTestKit;
import org.apache.pekko.actor.testkit.typed.javadsl.TestProbe;
import org.apache.pekko.actor.typed.ActorRef;
import org.junit.jupiter.api.Test;
import org.sample.pekko.sample4.IOTDevice.Command;
import org.sample.pekko.sample4.IOTDevice.ReadTemperature;


/**
 *
 * @author gentjan kolicaj
 * @since 1/23/26 9:38 PM
 *
 */
class IOTDeviceTest {

  private final ActorTestKit testKit = ActorTestKit.create();

  @Test
  void testReplyWithEmpty() {
    //probe
    TestProbe<IOTDevice.RespondTemperature> probe = testKit.createTestProbe(IOTDevice.RespondTemperature.class);

    //create actor
    ActorRef<Command> deviceActor = testKit.spawn(IOTDevice.create("0", "0"));
    deviceActor.tell(new ReadTemperature(1L, probe.getRef()));

    //response
    IOTDevice.RespondTemperature response = probe.receiveMessage();
    assertThat(response.requestId()).isEqualTo(1L);
    assertThat(response.value()).isEmpty();

  }

}