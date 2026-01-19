package org.sample.pekko.sample0;

import org.apache.pekko.actor.typed.ActorRef;

public record Greeted(String whom, ActorRef<Greet> from) {

}