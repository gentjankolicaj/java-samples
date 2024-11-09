# websocket

### What is it ?

A maven module that contains samples related to java websocket api & tomcat.

### Samples

| topic                                                                                   | source code                                                                              | test code                                                                                        |
|-----------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| Annotated endpoint with string message                                                  | [EchoEndpointA](./src/main/java/org/sample/websocket/annotated/EchoEndpointA.java)       | [EchoEndpointATest](./src/test/java/org/sample/websocket/annotated/EchoEndpointATest.java)       |
| Annotated endpoint with binary message                                                  | [EchoEndpointB](./src/main/java/org/sample/websocket/annotated/EchoEndpointB.java)       | [EchoEndpointBTest](./src/test/java/org/sample/websocket/annotated/EchoEndpointBTest.java)       |
| Annotated endpoints with binary & string messages                                       | [package](./src/main/java/org/sample/websocket/annotated)                                | [BothEndpointsTest](./src/test/java/org/sample/websocket/annotated/BothEndpointsTest.java)       |
| Programmatic endpoint with binary messages                                              | [BinaryEndpoint](./src/main/java/org/sample/websocket/programatic/BinaryEndpoint.java)   | [BinaryEndpointTest](./src/test/java/org/sample/websocket/programatic/BinaryEndpointTest.java)   |
| Programmatic endpoint with string messages                                              | [StringEndpoint](./src/main/java/org/sample/websocket/programatic/StringEndpoint.java)   | [StringEndpointTest](./src/test/java/org/sample/websocket/programatic/StringEndpointTest.java)   |
| Echo programmatic endpoint with string messages                                         | [EchoEndpoint](./src/main/java/org/sample/websocket/echo/EchoEndpoint.java)              | [EchoEndpointTest](./src/test/java/org/sample/websocket/echo/EchoEndpointTest.java)              |
| Chat annotated endpoint with ( binary & string ) messages                               | [ChatEndpoint](./src/main/java/org/sample/websocket/chat/ChatEndpoint.java)              | [ChatEndpointTest](./src/test/java/org/sample/websocket/chat/ChatEndpointTest.java)              |
| Json chat annotated endpoint with ( binary & string ) messages.Message codecs included. | [JSONChatEndpoint](./src/main/java/org/sample/websocket/chat_json/JSONChatEndpoint.java) | [JSONChatEndpointTest](./src/test/java/org/sample/websocket/chat_json/JSONChatEndpointTest.java) |

### POM dependencies

| Dependencies list                                     | version |
|-------------------------------------------------------|---------|
| <artifactId>jakarta.websocket-api</artifactId>        | 2.2.0   |
| <artifactId>jakarta.websocket-client-api</artifactId> | 2.2.0   |
| <artifactId>tomcat-embed-core</artifactId>            | 11.0.0  |
| <artifactId>tomcat-embed-websocket</artifactId>       | 11.0.0  |
| <artifactId>tomcat-embed-logging-juli</artifactId>    | 11.0.0  |
