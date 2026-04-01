package org.sample.projectreactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author gentjan kolicaj
 * @since 4/1/26 7:06 PM
 *
 */
@Slf4j
public class EventTypesSample {

  public static void main(String[] args) {
    //At any point in time from reactive stream (Mono / Flux) comes :
    //1. An item/items to be processed
    //2. A complete event
    //3. A failure event (something went wrong)

    // Item / items => are non-terminal events.
    // A complete event & failure event => are called terminal events.

    //Mono: After 0-1 item is emitted, next complete event is emitted.
    Mono<Integer> mono = Mono.just(-1);
    mono.subscribe(event -> log.info("Mono item emitted: {}", event),
        error -> log.error("Mono failure-event:", error),
        () -> log.info("Mono complete-event."));

    //Flux: After 0-n items are emitted, next complete-event is emitted.
    Flux<Integer> flux = Flux.range(0, 10);
    flux.subscribe(event -> log.info("Flux item emitted: {}", event),
        error -> log.info("Flux failure-event:", error),
        () -> log.info("Flux complete-event."));

    flux.blockLast(); //blocking undefinitely until upstream signals terminal event.
  }

}
