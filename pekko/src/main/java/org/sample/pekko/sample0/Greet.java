package org.sample.pekko.sample0;

import org.apache.pekko.actor.typed.ActorRef;

public record Greet(String whom, ActorRef<Greeted> replyTo) {

}