package org.sample.projectreactor;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author gentjan kolicaj
 * @since 4/1/26 7:32 PM
 *
 */
@Slf4j
public class BackpressureExample {

  public static void main(String[] args) {
    //At any point in time from reactive stream (Mono / Flux) comes :
    //1. An item/items to be processed
    //2. A complete event
    //3. A failure event (something went wrong)

    // Item / items => are non-terminal events.
    // A complete event & failure event => are called terminal events.

    //Mono: After 0-1 item is emitted, next complete event is emitted.
    Mono<Integer> mono = Mono.just(-1);
    //1. First way of subscribing (with lambdas)
    mono.subscribe(event -> log.info("Lambda: Mono item emitted: {}", event),
        error -> log.error("Lambda: Mono failure-event:", error),
        () -> log.info("Lambda: Mono complete-event."));
    //2.Second way of subscribing (with custom subscriber instance)
    mono.subscribe(new MySubscriber<>());

    //Flux: After 0-n items are emitted, next complete-event is emitted.
    //1.First way of subscribing (with lambdas)
    Flux<Integer> flux = Flux.range(0, 10);
    flux.subscribe(event -> log.info("Lambda: Flux item emitted: {}", event),
        error -> log.info("Lambda: Flux failure-event:", error),
        () -> log.info("Lambda: Flux complete-event."));
    //2.Second way of subscribing (with custom subscriber instance)
    flux.subscribe(new MySubscriber<>());

    flux.blockLast(); //blocking undefinitely until upstream signals terminal event.
  }


  //All subscriptions have a way of telling how much data is ok receiving (Backpressure)
  //You need to ask with how many items you are ok receiving.
  static class MySubscriber<T> extends BaseSubscriber<T> {

    @Override
    public void hookOnSubscribe(@NonNull Subscription subscription) {
      log.info("MySubscriber: Subscription happened");

      //Request 1 item after subscription
      //Doesn't mean publisher is forced to give 1 item, It just signals
      request(1);
    }

    @Override
    public void hookOnNext(@NonNull T value) {
      log.info("MySubscriber: Value {} received", value);
      //After first item is received
      //Request another one when publisher is ready.
      request(1);
    }
  }

}
