package webSocketService;

import apiREST.Cons;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import subscriber.Subscriber;
import util.MyEvent;
import util.MySubscriptionRequest;
import util.MySubscriptionClose;

@ClientEndpoint
public class WebSocketClient {

  static Map<String, Subscriber> subscriberMap;
  static Session session;

  public static String newInstance() { // Note: called when new clent created
    subscriberMap = new HashMap<String, Subscriber>();
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(WebSocketClient.class,
        URI.create(Cons.SERVER_WEBSOCKET));
        System.out.println("INFO -> Websocket -> Connected to server.");
        return "Connected to " + Cons.SERVER_WEBSOCKET;

    } catch (Exception e) {
        System.out.println("ERROR -> WebSocket -> No connection to the remote server!.");
        return "ERROR. Can't connect to " + Cons.SERVER_WEBSOCKET + "\nPlease ensure that server is running and restart this client to connect.";
        //e.printStackTrace();
    }
  }
  
  //only one subscriber per topic allowed:
  public static synchronized void addSubscriber(String topic_name, Subscriber subscriber) {
      if (!subscriberMap.containsKey(topic_name)){
        System.out.println("INFO -> WebSocet -> Trying to request for new subscriber..");
        Gson gson = new Gson();
        MySubscriptionRequest mySubsReq = new MySubscriptionRequest();
        mySubsReq.type=mySubsReq.type.ADD;
        mySubsReq.topic=topic_name;
        String json = gson.toJson(mySubsReq);
        try{
            session.getBasicRemote().sendText(json);
            subscriberMap.put(topic_name, subscriber);
            System.out.println("INFO -> WebSocet -> Subscribed successfully to topic '"+topic_name+"'.");

        } 
        catch (IOException exx) {
        exx.printStackTrace(); 
        System.out.println("INFO -> WebSocket -> Trouble sending subscription req");
        }
      }
      else{
            System.out.println("WARNING -> WebSocket -> Only one subscriber per topic allowed");
      }
    }
  

  public static synchronized void removeSubscriber(String topic_name) {
       if (!subscriberMap.containsKey(topic_name)){
        Gson gson = new Gson();
        MySubscriptionRequest mySubsReq = new MySubscriptionRequest();
        mySubsReq.type=mySubsReq.type.REMOVE;
        mySubsReq.topic=topic_name;
        String json = gson.toJson(mySubsReq);
        try{
            session.getBasicRemote().sendText(json);
            subscriberMap.remove(topic_name);
            System.out.println("INFO -> WebSocet -> Unsubscribed successfully from '"+topic_name+"'.");
        } 
        catch (IOException exx) {
            exx.printStackTrace(); 
            System.out.println("ERROR -> WebSocket -> Trouble removing subscription req");
        }
      }
    }

  public static void close() {
    try {
      session.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  public void onMessage(String message) {
    Gson gson = new Gson();
    MyEvent myEvent = gson.fromJson(message, MyEvent.class);
    String content = myEvent.content;
    //message to notify subscription close:
    if (content == null) {
        System.out.println("DEBUG: NON - ordinary (desubscribe) message received: " + content + myEvent.topic + " subs: "+subscriberMap);
        subscriberMap.get(myEvent.topic).onClose(myEvent.topic,"PUBLISHER");
        subscriberMap.remove(myEvent.topic);
    } 
    else {
        subscriberMap.get(myEvent.topic).onEvent(myEvent.topic,myEvent.content);
    }
  }
}