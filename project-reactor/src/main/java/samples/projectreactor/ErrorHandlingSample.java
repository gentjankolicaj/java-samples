package samples.projectreactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author gentjan kolicaj
 * @since 4/3/26 12:14 PM
 *
 */
@Slf4j
public class ErrorHandlingSample {


  public static void main(String[] args) {
    //1.Error: is a terminal event like complete-event.
    //2.Reason 'Error' is a terminal event is because consumer stops asking for more.If consumer continued => error wouldn't be terminal.
    //2.OnError() method of subscriber called.
    //3.doOnError(): adds behavior triggered when the Flux/Mono completes with an error.It doesn't swallow error.
    //4.onErrorContinue: Lets upstream operators recover from error and continue.

    //Mono emitting error
    Mono.error(new RuntimeException("Mono RE"))
        .doOnError(err -> log.error("Mono doOnError(): {} ",
            err.getMessage())) //Adds behavior triggered when the Mono completes with an error, and doesn't stop event down the line.
        .subscribe();

    //Flux emitting error
    Flux.error(new RuntimeException("Flux RE"))
        .doOnError(err -> log.error("Flux doOnError(): {}",
            err.getMessage())) //Adds behavior triggered when the Flux completes with an error, and doesn't stop event down the line.
        .subscribe();

    //Continue consuming if error happens: Emit values, emit error, emit values
    Flux.range(0, 10)
        .map(i -> {
          if (i == 5) {
            throw new RuntimeException("Middle RE");
          }
          return i;
        })
        .onErrorContinue((err, item) -> log.error("onErrorContinue() :{}", err.getMessage()))
        .log()
        .subscribe();

    //When error is emitted, resume with another flux.
    Flux.error(new RuntimeException("Start RE"))
        .onErrorResume(err -> Flux.range(10, 20))
        .log()
        .subscribe();

  }

}
