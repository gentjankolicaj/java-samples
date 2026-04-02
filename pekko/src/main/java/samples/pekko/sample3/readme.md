# pekko sample 3

- Parents and children are connected throughout their lifecycles.
- Whenever an actor fails the failure information is propagated to the supervision strategy which
  then decides how to handle the exception caused by the actor.
- The supervision strategy is typically defined by the parent actor when it spawns a child actor. In
  this way, parents act as supervisors for their children.
- The default supervisor strategy is to stop the child. If you don’t define the strategy all
  failures result in a stop.
- Let’s observe a restart supervision strategy in a simple experiment.
- Add the following classes to your project, just as you did with the previous ones: