package org.sample.pekko.sample4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.pekko.actor.testkit.typed.javadsl.ActorTestKit;
import org.apache.pekko.actor.testkit.typed.javadsl.TestProbe;
import org.apache.pekko.actor.typed.ActorRef;
import org.junit.jupiter.api.Test;
import org.sample.pekko.sample4.Device.Command;
import org.sample.pekko.sample4.DeviceManager.DeviceNotAvailable;
import org.sample.pekko.sample4.DeviceManager.DeviceTimedOut;
import org.sample.pekko.sample4.DeviceManager.RespondAllTemperatures;
import org.sample.pekko.sample4.DeviceManager.Temperature;
import org.sample.pekko.sample4.DeviceManager.TemperatureNotAvailable;
import org.sample.pekko.sample4.DeviceManager.TemperatureReading;

/**
 *
 * @author gentjan kolicaj
 * @since 1/27/26 6:12 PM
 *
 */
public class DeviceGroupQueryTest {

  ActorTestKit testKit = ActorTestKit.create();

  @Test
  public void testReturnTemperatureValueForWorkingDevices() {
    TestProbe<RespondAllTemperatures> requester = testKit.createTestProbe(RespondAllTemperatures.class);
    TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
    TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);

    Map<String, ActorRef<Command>> deviceIdToActor = new HashMap<>();
    deviceIdToActor.put("device1", device1.getRef());
    deviceIdToActor.put("device2", device2.getRef());

    ActorRef<DeviceGroupQuery.Command> queryActor =
        testKit.spawn(DeviceGroupQuery.create(deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));

    device1.expectMessageClass(Device.ReadTemperature.class);
    device2.expectMessageClass(Device.ReadTemperature.class);

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device2", Optional.of(2.0))));

    RespondAllTemperatures response = requester.receiveMessage();
    assertEquals(1L, response.requestId());

    Map<String, TemperatureReading> expectedTemperatures = new HashMap<>();
    expectedTemperatures.put("device1", new Temperature(1.0));
    expectedTemperatures.put("device2", new Temperature(2.0));

    assertEquals(expectedTemperatures, response.temperatures());
  }

  @Test
  public void testReturnTemperatureNotAvailableForDevicesWithNoReadings() {
    TestProbe<RespondAllTemperatures> requester =
        testKit.createTestProbe(RespondAllTemperatures.class);
    TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
    TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);

    Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
    deviceIdToActor.put("device1", device1.getRef());
    deviceIdToActor.put("device2", device2.getRef());

    ActorRef<DeviceGroupQuery.Command> queryActor =
        testKit.spawn(
            DeviceGroupQuery.create(
                deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));

    assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId());
    assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId());

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device1", Optional.empty())));

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device2", Optional.of(2.0))));

    RespondAllTemperatures response = requester.receiveMessage();
    assertEquals(1L, response.requestId());

    Map<String, TemperatureReading> expectedTemperatures = new HashMap<>();
    expectedTemperatures.put("device1", TemperatureNotAvailable.INSTANCE);
    expectedTemperatures.put("device2", new Temperature(2.0));

    assertEquals(expectedTemperatures, response.temperatures());
  }

  @Test
  public void testReturnDeviceNotAvailableIfDeviceStopsBeforeAnswering() {
    TestProbe<RespondAllTemperatures> requester =
        testKit.createTestProbe(RespondAllTemperatures.class);
    TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
    TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);

    Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
    deviceIdToActor.put("device1", device1.getRef());
    deviceIdToActor.put("device2", device2.getRef());

    ActorRef<DeviceGroupQuery.Command> queryActor =
        testKit.spawn(
            DeviceGroupQuery.create(
                deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));

    assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId());
    assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId());

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));

    device2.stop();

    RespondAllTemperatures response = requester.receiveMessage();
    assertEquals(1L, response.requestId());

    Map<String, TemperatureReading> expectedTemperatures = new HashMap<>();
    expectedTemperatures.put("device1", new Temperature(1.0));
    expectedTemperatures.put("device2", DeviceNotAvailable.INSTANCE);

    assertEquals(expectedTemperatures, response.temperatures());
  }

  @Test
  public void testReturnTemperatureReadingEvenIfDeviceStopsAfterAnswering() {
    TestProbe<RespondAllTemperatures> requester =
        testKit.createTestProbe(RespondAllTemperatures.class);
    TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
    TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);

    Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
    deviceIdToActor.put("device1", device1.getRef());
    deviceIdToActor.put("device2", device2.getRef());

    ActorRef<DeviceGroupQuery.Command> queryActor =
        testKit.spawn(
            DeviceGroupQuery.create(
                deviceIdToActor, 1L, requester.getRef(), Duration.ofSeconds(3)));

    assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId());
    assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId());

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device2", Optional.of(2.0))));

    device2.stop();

    RespondAllTemperatures response = requester.receiveMessage();
    assertEquals(1L, response.requestId());

    Map<String, TemperatureReading> expectedTemperatures = new HashMap<>();
    expectedTemperatures.put("device1", new Temperature(1.0));
    expectedTemperatures.put("device2", new Temperature(2.0));

    assertEquals(expectedTemperatures, response.temperatures());
  }

  @Test
  public void testReturnDeviceTimedOutIfDeviceDoesNotAnswerInTime() {
    TestProbe<RespondAllTemperatures> requester =
        testKit.createTestProbe(RespondAllTemperatures.class);
    TestProbe<Device.Command> device1 = testKit.createTestProbe(Device.Command.class);
    TestProbe<Device.Command> device2 = testKit.createTestProbe(Device.Command.class);

    Map<String, ActorRef<Device.Command>> deviceIdToActor = new HashMap<>();
    deviceIdToActor.put("device1", device1.getRef());
    deviceIdToActor.put("device2", device2.getRef());

    ActorRef<DeviceGroupQuery.Command> queryActor =
        testKit.spawn(
            DeviceGroupQuery.create(
                deviceIdToActor, 1L, requester.getRef(), Duration.ofMillis(200)));

    assertEquals(0L, device1.expectMessageClass(Device.ReadTemperature.class).requestId());
    assertEquals(0L, device2.expectMessageClass(Device.ReadTemperature.class).requestId());

    queryActor.tell(
        new DeviceGroupQuery.WrappedRespondTemperature(
            new Device.RespondTemperature(0L, "device1", Optional.of(1.0))));

    // no reply from device2

    RespondAllTemperatures response = requester.receiveMessage();
    assertEquals(1L, response.requestId());

    Map<String, TemperatureReading> expectedTemperatures = new HashMap<>();
    expectedTemperatures.put("device1", new Temperature(1.0));
    expectedTemperatures.put("device2", DeviceTimedOut.INSTANCE);

    assertEquals(expectedTemperatures, response.temperatures());
  }

}
