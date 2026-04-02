package samples.projectreactor.pubsub;

import java.util.stream.IntStream;

/**
 *
 * @author gentjan kolicaj
 * @since 4/1/26 10:19 AM
 *
 */
public class PubSubSample {

  public static void main(String[] args) {
    //subscriber
    StringSubscriber a = new StringSubscriber();
    StringSubscriber b = new StringSubscriber();
    StringSubscriber c = new StringSubscriber();

    //publisher
    StringPublisher publisher = new StringPublisher();

    //add subscribers
    publisher.subscribe(a);
    publisher.subscribe(b);
    publisher.subscribe(c);

    //publish events.
    IntStream.range(0, 100)
        .mapToObj(i -> String.valueOf((char) i))
        .forEach(publisher::publish);
  }

}
