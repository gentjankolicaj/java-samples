package org.sample.pekko.sample4;


import static org.assertj.core.api.Assertions.assertThat;

import org.apache.pekko.actor.testkit.typed.javadsl.ActorTestKit;
import org.apache.pekko.actor.testkit.typed.javadsl.TestProbe;
import org.apache.pekko.actor.typed.ActorRef;
import org.junit.jupiter.api.Test;
import org.sample.pekko.sample4.Device.Command;
import org.sample.pekko.sample4.Device.ReadTemperature;
import org.sample.pekko.sample4.Device.RecordTemperature;


/**
 *
 * @author gentjan kolicaj
 * @since 1/23/26 9:38 PM
 *
 */
class DeviceTest {

  private final ActorTestKit testKit = ActorTestKit.create();

  @Test
  void onReadTemperature() {
    //probe
    TestProbe<Device.RespondTemperature> probe = testKit.createTestProbe(Device.RespondTemperature.class);

    //create actor
    ActorRef<Command> deviceActor = testKit.spawn(Device.create("0", "0"));
    deviceActor.tell(new ReadTemperature(1L, probe.getRef()));

    //response
    Device.RespondTemperature response = probe.receiveMessage();
    assertThat(response.requestId()).isEqualTo(1L);
    assertThat(response.value()).isEmpty();

  }

  @Test
  void onRecordTemperature() {
    //create actor
    ActorRef<Command> deviceActor = testKit.spawn(Device.create("0", "0"));

    //read probe
    TestProbe<Device.RespondTemperature> readProbe = testKit.createTestProbe(Device.RespondTemperature.class);

    //send read temperature request
    deviceActor.tell(new ReadTemperature(1L, readProbe.getRef()));

    //read temperature response
    Device.RespondTemperature response0 = readProbe.receiveMessage();
    assertThat(response0.requestId()).isEqualTo(1L);
    assertThat(response0.value()).isEmpty();

    //record probe
    TestProbe<Device.RecordedTemperature> recordProbe = testKit.createTestProbe(Device.RecordedTemperature.class);

    //send record temperature message
    deviceActor.tell(new RecordTemperature(101L, 3.14, recordProbe.getRef()));

    //record temperature response
    Device.RecordedTemperature recordResponse = recordProbe.receiveMessage();
    assertThat(recordResponse.requestId()).isEqualTo(101L);

    //send read temperature request
    deviceActor.tell(new ReadTemperature(2L, readProbe.getRef()));

    //read temperature response
    Device.RespondTemperature response1 = readProbe.receiveMessage();
    assertThat(response1.requestId()).isEqualTo(2L);
    assertThat(response1.value()).hasValue(3.14);

  }

}