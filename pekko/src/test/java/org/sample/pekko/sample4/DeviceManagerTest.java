package org.sample.pekko.sample4;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.pekko.actor.testkit.typed.javadsl.ActorTestKit;
import org.apache.pekko.actor.testkit.typed.javadsl.TestProbe;
import org.apache.pekko.actor.typed.ActorRef;
import org.junit.jupiter.api.Test;
import org.sample.pekko.sample4.DeviceManager.Command;
import org.sample.pekko.sample4.DeviceManager.DeviceRegistered;
import org.sample.pekko.sample4.DeviceManager.ReplyDeviceList;
import org.sample.pekko.sample4.DeviceManager.RequestDeviceList;
import org.sample.pekko.sample4.DeviceManager.RequestTrackDevice;

/**
 *
 * @author gentjan kolicaj
 * @since 1/26/26 6:22 PM
 *
 */
class DeviceManagerTest {

  ActorTestKit actorTestKit = ActorTestKit.create();

  @Test
  void onTrackDevice() {
    ActorRef<Command> deviceManager = actorTestKit.spawn(DeviceManager.create(), "device-manager");

    //Message probe
    TestProbe<DeviceManager.DeviceRegistered> probe = actorTestKit.createTestProbe(DeviceManager.DeviceRegistered.class);

    //send message
    //group actor is created
    deviceManager.tell(new RequestTrackDevice("0", "0", probe.getRef()));

    //read message sent
    DeviceRegistered deviceRegistered = probe.receiveMessage();
    assertThat(deviceRegistered).isNotNull();
    assertThat(deviceRegistered.device()).isNotNull();

    //send same message (with same group) & expect same actor
    deviceManager.tell(new RequestTrackDevice("0", "0", probe.getRef()));

    //group actor is received from hashmap
    DeviceRegistered deviceRegistered1 = probe.receiveMessage();
    assertThat(deviceRegistered1).isNotNull();
    assertThat(deviceRegistered1.device()).isEqualTo(deviceRegistered.device());
  }

  @Test
  void onRequestDeviceList() {
    //create actor
    ActorRef<Command> deviceManager = actorTestKit.spawn(DeviceManager.create(), "device-manager-requestdevicelist");

    //create message probe
    TestProbe<DeviceManager.ReplyDeviceList> probe = actorTestKit.createTestProbe(DeviceManager.ReplyDeviceList.class);

    //send message
    deviceManager.tell(new RequestDeviceList(0L, "0", probe.getRef()));

    //receive message with probe
    ReplyDeviceList replyDeviceList = probe.receiveMessage();
    assertThat(replyDeviceList).isNotNull();
    assertThat(replyDeviceList.ids()).isEmpty();

  }

}