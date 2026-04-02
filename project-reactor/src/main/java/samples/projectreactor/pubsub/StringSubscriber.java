package samples.projectreactor.pubsub;

import java.util.concurrent.Flow;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author gentjan kolicaj
 * @since 3/31/26 8:58 AM
 *
 */
@Slf4j
public class StringSubscriber implements Flow.Subscriber<String> {

  @Override
  public void onSubscribe(Flow.Subscription subscription) {

  }

  @Override
  public void onNext(String s) {
    log.info("Next : {}", s);
  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onComplete() {
    log.info("Completed :{}", this);
  }
}
