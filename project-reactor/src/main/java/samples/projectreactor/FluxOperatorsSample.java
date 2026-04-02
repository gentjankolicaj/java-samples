package samples.projectreactor;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author gentjan kolicaj
 * @since 4/2/26 12:52 PM
 *
 */
@Slf4j
public class FluxOperatorsSample {

  public static void main(String[] args) {
    //There are 2 types of operators: intermediate & terminal

    //filter: intermediate
    Flux.range(0, 10).filter(Objects::nonNull).subscribe(e -> log.info("filter:{}", e));

    //map: intermediate
    Flux.range(0, 10).map(e -> e * 10).subscribe(e -> log.info("map:{}", e));

    //log: intermediate
    Flux.range(0, 10).map(e -> e * 3.14).log();

    //take: intermediate
    Flux.range(0, 10).map(e -> e * 10).take(5).subscribe(e -> log.info("take:{}", e));

    //defaultIfEmpty: intermediate
    Flux.empty().log().defaultIfEmpty(-1).log().blockLast();
  }

}
