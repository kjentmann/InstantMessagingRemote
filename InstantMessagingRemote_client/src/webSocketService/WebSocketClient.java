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

  public static void newInstance() { // Note: called when new clent created
    subscriberMap = new HashMap<String, Subscriber>();
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(WebSocketClient.class,
        URI.create(Cons.SERVER_WEBSOCKET));
        System.out.println("SYSTEM: Connected to server.");

    } catch (Exception e) {
        System.out.println("SYSTEM: ERROR. No connection to the remote server!");
        //e.printStackTrace();
    }
  }
  
  //only one subscriber per topic allowed:
  public static synchronized void addSubscriber(String topic_name, Subscriber subscriber) {
      if (!subscriberMap.containsKey(topic_name)){
        System.out.println("WS trying to request for new subscriber..");
        Gson gson = new Gson();
        MySubscriptionRequest mySubsReq = new MySubscriptionRequest();
        mySubsReq.type=mySubsReq.type.ADD;
        mySubsReq.topic=topic_name;
        String json = gson.toJson(mySubsReq);
        try{
            session.getBasicRemote().sendText(json);
            subscriberMap.put(topic_name, subscriber);
        } 
        catch (IOException exx) {
        exx.printStackTrace(); 
        System.out.println("DEBUG: Trouble sending subscription req");
        }
      }
      else{
            System.out.println("DEBUG: Only one subscriber per topic allowed");
      }
    }
  

  public static synchronized void removeSubscriber(String topic_name) {
       if (!subscriberMap.containsKey(topic_name)){
        System.out.println("WS trying to remove subscriber..");
        Gson gson = new Gson();
        MySubscriptionRequest mySubsReq = new MySubscriptionRequest();
        mySubsReq.type=mySubsReq.type.REMOVE;
        mySubsReq.topic=topic_name;
        String json = gson.toJson(mySubsReq);
        try{
            session.getBasicRemote().sendText(json);
            subscriberMap.remove(topic_name);
        } 
        catch (IOException exx) {
            exx.printStackTrace(); 
            System.out.println("DEBUG: Trouble removing subscription req");
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
        System.out.println("DEBUG: NON - ordinary message received: " + content);
        subscriberMap.get(myEvent.topic).onClose(myEvent.content,myEvent.topic);
    } 
    //ordinary message from topic:
    else {
        subscriberMap.get(myEvent.topic).onEvent(myEvent.content,myEvent.topic);
      //...
    }
  }
}