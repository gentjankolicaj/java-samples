package org.sample.jetty_12;

import java.util.concurrent.TimeUnit;

public record TimeoutProperties(int duration, TimeUnit timeUnit) {

}