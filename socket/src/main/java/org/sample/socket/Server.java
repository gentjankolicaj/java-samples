package org.sample.websocket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.sample.websocket.api.Packet;
import org.sample.websocket.api.Request;
import org.sample.websocket.api.Response;
import org.sample.websocket.api.Status;

@Slf4j
public class Server {

  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(3333);
    Socket socket = null;
    while (socket == null) {
      socket = serverSocket.accept();
    }
    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

    //Sending from server to client
    List<Packet> packetList = getPackets();
    log.info("Server : packets " + packetList);
    out.writeObject(new Request(0, 3));
    out.flush();

    Object inObject = in.readObject();
    while (inObject == null) {
      inObject = in.readObject();
    }
    if (inObject instanceof Response) {
      Response response = (Response) inObject;
      if (response.getStatus().name().equals(Status.OK.name())) {
        for (int i = 0; i < packetList.size(); i++) {
          Packet packet = packetList.get(i);
          out.writeObject(packet);
          out.flush();
          Object packetObject = in.readObject();
          while (packetObject == null) {
            packetObject = in.readObject();
          }
          Response packetResponse = null;
          if (packetObject instanceof Response) {
            packetResponse = (Response) packetObject;
            log.info("Server : - " + i + " sent packet " + packet + ", client-response "
                + packetResponse);
          }
          if (!packetResponse.getStatus().name().equals(Status.OK.name())) {
            log.info(
                "Server : stopped sending packets , client-response " + packetResponse);
            break;
          }
        }
      }
    } else {
      inObject = null;
    }

    //Receiving from client to server
    inObject = in.readObject();
    while (inObject == null) {
      inObject = in.readObject();
    }
    if (inObject instanceof Request) {
      Request request = (Request) inObject;
      List<Packet> packets = new ArrayList<>(request.getPackets());
      Response response = new Response(1, Status.OK);
      out.writeObject(response);
      out.flush();
      log.info("Server : acceptance response for client sent " + response);

      for (int i = 0; i < request.getPackets(); i++) {
        Object inPacket = in.readObject();
        while (inPacket == null) {
          inPacket = in.readObject();
        }
        Packet packet = (Packet) inPacket;
        out.writeObject(response);
        out.flush();
        packets.add(packet);
        log.info(
            "Server : received from client " + packet + ", server-response " + response);
      }
    }

    in.close();
    out.close();
    socket.close();
    serverSocket.close();
  }


  public static List<Packet> getPackets() {
    List<Packet> packetList = new ArrayList<>();
    packetList.add(new Packet(0, 1, "Hello"));
    packetList.add(new Packet(0, 1, "World"));
    packetList.add(new Packet(0, 1, "This is testing"));
    return packetList;

  }
}
