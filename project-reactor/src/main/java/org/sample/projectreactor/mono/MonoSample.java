package org.sample.projectreactor.mono;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author gentjan kolicaj
 * @since 4/1/26 1:36 PM
 *
 */
@Slf4j
public class MonoSample {


  public static void main(String[] args) {

    //1.Mono represents a deferred result that emits 0 - 1 item at completion.
    //2.Flux represents a deferred stream of data that can emit 0 - n item.
    // In the context of reactive programming, a deferred result means that the actual computation or data retrieval doesn't happen when you define the code,
    // but rather at a later time—specifically when someone asks for it.
    emptyAndValueMono();

    //Print value from mono, non-blocking
    Mono.just(101).subscribe(value -> log.info("subscribe() mono value: {}", value));

    //Declare mono with value, and get it => subscribe to mono and block indefinitely until event emitted.
    Integer _202 = Mono.just(202).block();
    log.info("block() mono value: {}", _202);
  }

  static void emptyAndValueMono() {
    log.info("Empty mono: {}", Mono.empty());
    log.info("Value mono: {}", Mono.just(3.14f));
  }

}
