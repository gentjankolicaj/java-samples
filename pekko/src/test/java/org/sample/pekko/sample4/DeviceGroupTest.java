package org.sample.pekko.sample4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.pekko.actor.testkit.typed.javadsl.ActorTestKit;
import org.apache.pekko.actor.testkit.typed.javadsl.TestProbe;
import org.apache.pekko.actor.typed.ActorRef;
import org.junit.jupiter.api.Test;
import org.sample.pekko.sample4.DeviceGroup.Command;
import org.sample.pekko.sample4.DeviceManager.DeviceRegistered;
import org.sample.pekko.sample4.DeviceManager.ReplyDeviceList;
import org.sample.pekko.sample4.DeviceManager.RequestDeviceList;
import org.sample.pekko.sample4.DeviceManager.RequestTrackDevice;

/**
 *
 * @author gentjan kolicaj
 * @since 1/24/26 7:50 PM
 *
 */
class DeviceGroupTest {

  ActorTestKit testKit = ActorTestKit.create();

  @Test
  public void testReplyToRegistrationRequests() {
    TestProbe<DeviceRegistered> probe = testKit.createTestProbe(DeviceRegistered.class);
    ActorRef<Command> groupActor = testKit.spawn(DeviceGroup.create("group"));

    groupActor.tell(new RequestTrackDevice("group", "device", probe.getRef()));
    DeviceRegistered registered1 = probe.receiveMessage();

    // another deviceId
    groupActor.tell(new RequestTrackDevice("group", "device3", probe.getRef()));
    DeviceRegistered registered2 = probe.receiveMessage();
    assertNotEquals(registered1.device(), registered2.device());

    // Check that the device actors are working
    TestProbe<Device.RecordedTemperature> recordProbe = testKit.createTestProbe(Device.RecordedTemperature.class);
    registered1.device().tell(new Device.RecordTemperature(0L, 1.0, recordProbe.getRef()));
    assertEquals(0L, recordProbe.receiveMessage().requestId());
    registered2.device().tell(new Device.RecordTemperature(1L, 2.0, recordProbe.getRef()));
    assertEquals(1L, recordProbe.receiveMessage().requestId());
  }

  @Test
  public void testIgnoreWrongRegistrationRequests() {
    TestProbe<DeviceRegistered> probe = testKit.createTestProbe(DeviceRegistered.class);
    ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));
    groupActor.tell(new RequestTrackDevice("wrongGroup", "device1", probe.getRef()));
    probe.expectNoMessage();
  }

  @Test
  public void testReturnSameActorForSameDeviceId() {
    TestProbe<DeviceRegistered> probe = testKit.createTestProbe(DeviceRegistered.class);
    ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));

    groupActor.tell(new RequestTrackDevice("group", "device", probe.getRef()));
    DeviceRegistered registered1 = probe.receiveMessage();

    // registering same again should be idempotent
    groupActor.tell(new RequestTrackDevice("group", "device", probe.getRef()));
    DeviceRegistered registered2 = probe.receiveMessage();
    assertEquals(registered1.device(), registered2.device());
  }

  @Test
  public void testListActiveDevices() {
    TestProbe<DeviceRegistered> registeredProbe = testKit.createTestProbe(DeviceRegistered.class);
    ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));

    groupActor.tell(new RequestTrackDevice("group", "device1", registeredProbe.getRef()));
    registeredProbe.receiveMessage();

    groupActor.tell(new RequestTrackDevice("group", "device2", registeredProbe.getRef()));
    registeredProbe.receiveMessage();

    TestProbe<ReplyDeviceList> deviceListProbe = testKit.createTestProbe(ReplyDeviceList.class);

    groupActor.tell(new RequestDeviceList(0L, "group", deviceListProbe.getRef()));
    ReplyDeviceList reply = deviceListProbe.receiveMessage();
    assertEquals(0L, reply.requestId());
    assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.ids());
  }

  @Test
  public void testListActiveDevicesAfterOneShutsDown() {
    TestProbe<DeviceRegistered> registeredProbe = testKit.createTestProbe(DeviceRegistered.class);
    ActorRef<DeviceGroup.Command> groupActor = testKit.spawn(DeviceGroup.create("group"));

    groupActor.tell(new RequestTrackDevice("group", "device1", registeredProbe.getRef()));
    DeviceRegistered registered1 = registeredProbe.receiveMessage();

    groupActor.tell(new RequestTrackDevice("group", "device2", registeredProbe.getRef()));
    DeviceRegistered registered2 = registeredProbe.receiveMessage();

    ActorRef<Device.Command> toShutDown = registered1.device();

    TestProbe<ReplyDeviceList> deviceListProbe = testKit.createTestProbe(ReplyDeviceList.class);

    groupActor.tell(new RequestDeviceList(0L, "group", deviceListProbe.getRef()));
    ReplyDeviceList reply = deviceListProbe.receiveMessage();
    assertEquals(0L, reply.requestId());
    assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.ids());

    toShutDown.tell(Device.Passivate.INSTANCE);
    registeredProbe.expectTerminated(toShutDown, registeredProbe.getRemainingOrDefault());

    // using awaitAssert to retry because it might take longer for the groupActor
    // to see the Terminated, that order is undefined
    registeredProbe.awaitAssert(
        () -> {
          groupActor.tell(new RequestDeviceList(1L, "group", deviceListProbe.getRef()));
          ReplyDeviceList r = deviceListProbe.receiveMessage();
          assertEquals(1L, r.requestId());
          assertEquals(Stream.of("device2").collect(Collectors.toSet()), r.ids());
          return null;
        });
  }

}