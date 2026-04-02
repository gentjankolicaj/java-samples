package samples.projectreactor;

import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author gentjan kolicaj
 * @since 4/2/26 12:31 PM
 *
 */
@Slf4j
public class BlockingSample {


  public static void main(String[] args) {
    //Block non-empty mono -> returns value
    Integer zero = Mono.just(0).block();

    //Block empty mono
    Object obj1 = Mono.empty().block();

    //Non-responsive mono with block duration
    try {
      //I declare a mono to emit an element after a delay of 5s
      //And I declare the block to wait for 2s => will be thrown an exception [java.lang.IllegalStateException: Timeout on blocking read for 2000000000 NANOSECONDS].
      Integer one = Mono.just(1).delayElement(Duration.ofSeconds(5)).block(Duration.ofSeconds(2));
    } catch (Exception e) {
      log.error("Mono.delayElement(Duration.ofSeconds(5)).block(Duration.ofSeconds(2)): ", e);
    }

    //Block non-empty flux -> returns values
    List<Integer> list = Flux.range(0, 100).toStream().toList();

    //Block empty flux
    Object obj2 = Flux.empty().blockLast();

    //Non-responsive flux with block duration
    try {
      //I declare flux to emit 0-100 elements after a delay of 5 seconds
      //And I declare the block to wait for 2s => will be thrown an exception [java.lang.IllegalStateException: Timeout on blocking read for 2000000000 NANOSECONDS].
      Integer last = Flux.range(0, 100).delayElements(Duration.ofSeconds(5)).blockLast(Duration.ofSeconds(2));
    } catch (Exception e) {
      log.error("Flux.delayElements(Duration.ofSeconds(5)).blockLast(Duration.ofSeconds(2)): ", e);
    }

  }

}
