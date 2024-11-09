# websocket

### What is it ?

A maven module that contains samples related to java websocket api & tomcat.

### Samples

It contains implementation samples at:

| topic                                                                                   | source code                                                                       | test code                                                                                 |
|-----------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| Annotated endpoint with string message                                                  | [EchoEndpointA](/src/main/java/org/sample/websocket/annotated/EchoEndpointA.java) | [EchoEndpointATest](/src/test/java/org/sample/websocket/annotated/EchoEndpointATest.java) |
| Annotated endpoint with binary message                                                  | [EchoEndpointB](/src/main/java/org/sample/websocket/annotated/EchoEndpointB.java) | [EchoEndpointBTest](/src/test/java/org/sample/websocket/annotated/EchoEndpointBTest.java) |
| Programmatic endpoint with binary messages                                              | [package](/src/main/java/org/sample/websocket/binary)                             | [package](/src/test/java/org/sample/websocket/binary)                                     |
| Programmatic endpoint with string messages                                              | [package](/src/main/java/org/sample/websocket/programatic)                        | [package](/src/test/java/org/sample/websocket/programatic)                                |
| Chat annotated endpoint with ( binary & string ) messages                               | [package](/src/main/java/org/sample/websocket/chat)                               | [package](/src/test/java/org/sample/websocket/chat)                                       |
| Json chat annotated endpoint with ( binary & string ) messages.Message codecs included. | [package](/src/main/java/org/sample/websocket/chat_json)                          | [package](/src/test/java/org/sample/websocket/chat_json)                                  |



