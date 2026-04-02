package samples.projectreactor.flux;

import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author gentjan kolicaj
 * @since 4/1/26 1:24 PM
 *
 */
@Slf4j
public class FluxSample {


  public static void main(String[] args) {

    //1.Mono represents a deferred result that emits 0 - 1 item at completion.
    //2.Flux represents a deferred stream of data that can emit 0 - n item.
    // In the context of reactive programming, a deferred result means that the actual computation or data retrieval doesn't happen when you define the code,
    // but rather at a later time—specifically when someone asks for it.
    intsFlux();


  }


  static void intsFlux() {
    //Declare reactive stream
    Flux<Integer> flux = Flux.range(0, 100);

    //transform from flux to stream, this is a blocking operation.
    Stream<Integer> stream = flux.toStream(); //blocking call
    log.info("intsFlux elements: {}", stream.toList());
  }

}
