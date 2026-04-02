package samples.projectreactor;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author gentjan kolicaj
 * @since 4/2/26 12:52 PM
 *
 */
@Slf4j
public class MonoOperatorsSample {

  public static void main(String[] args) {
    //There are 2 types of operators: intermediate & terminal

    //filter: intermediate
    Mono.just(1).filter(Objects::nonNull).subscribe(e -> log.info("filter:{}", e));

    //map: intermediate
    Mono.just(2).map(e -> e * 10).subscribe(e -> log.info("map:{}", e));

    //log: intermediate
    Mono.just(3).map(e -> e * 3.14).log();

    //defaultIfEmpty: intermediate
    Mono.empty().defaultIfEmpty(-1).subscribe(e -> log.info("defaultIfEmpty:{}", e));

  }

}
