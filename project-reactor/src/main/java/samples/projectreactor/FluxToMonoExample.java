package samples.projectreactor;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author gentjan kolicaj
 * @since 4/3/26 1:19 PM
 *
 */
public class FluxToMonoExample {


  public static void main(String[] args) {
    //Conversion: Flux->Mono & Mono->Flux
    //Buffer operator:  used to gather incoming items from a stream into batches (usually lists) before passing them on
    //How buffer operator works:
    //Gather incoming items into batch, once a specific batch "boundary" is met, it emits that entire collection as a single item and starts a new one.

    //Size of number bigger than 10
    Mono<Long> sizeBiggerThan10 = Flux.range(5, 15)
        .filter(i -> i > 10)
        .count();
    sizeBiggerThan10.log().subscribe();

    //List of elements from flux
    Mono<List<Integer>> listMono = Flux.range(20, 30)
        .collectList();
    listMono.log().subscribe();

    //Buffer operator: gather all int bigger than 9 and buffers of 2
    Flux.range(5, 11)
        .filter(i -> i > 9)
        .buffer(2) //Return a Flux of list with 2 items
        .map(l -> l.get(0) + l.get(1)) //Sum of 2 elements on list
        .log()
        .subscribe();

  }

}
