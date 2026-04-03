package samples.projectreactor;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
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
    Flux.range(10, 20).map(e -> e * 3.14).log();

    //take: intermediate
    Flux.range(0, 10).map(e -> e * 10).take(5).subscribe(e -> log.info("take:{}", e));

    //defaultIfEmpty: intermediate
    Flux.empty().log().defaultIfEmpty(-1).log().subscribe();

    //flatMap: intermediate operator => new flux returned
    Flux.range(3, 30)
        .flatMap(i -> getUserFlux().filter(u -> u.getId() == i))
        .filter(u -> u.getName() != null)
        .log()
        .subscribe();

    //distinct: intermediate
    Flux.just(4, 4, 4, 4, 4, 4, 4, 5, 6, 7, 7, 7, 7, 8, 9, 0).distinct().log().subscribe(); //.subscribe() triggers running.

    //distinctUntilChanged: intermediate, filter out subsequent repetitions
    Flux.just(4, 4, 4, 3, 4, 4, 4, 4, 5, 6, 7, 7, 2, 7, 7, 8, 9, 0).distinctUntilChanged().log()
        .subscribe(); //.subscribe() triggers running.

    //NOTE: subscribe() method can't be chained,it returns Disposable.

  }

  static Flux<User> getUserFlux() {
    return Flux.just(new User(0, "0", "0"), new User(1, "1", "1"), new User(2, "2", "2"),
        new User(3, "3", "3"), new User(4, "4", "4"), new User(5, "5", "5"));
  }

  @ToString
  @Getter
  @AllArgsConstructor
  static class User {

    private int id;
    private String name;
    private String surname;
  }

}
