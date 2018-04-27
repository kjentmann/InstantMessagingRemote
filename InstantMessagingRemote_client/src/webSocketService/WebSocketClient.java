package webSocketService;

import apiREST.Cons;
import com.google.gson.Gson;
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

  public static void newInstance() { // Note: called wwhen new client created
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
  
  
  
  /*
  DELETE THIS
  
  @Override
  public void onEvent(String topic, String event) {
    Gson gson = new Gson();
    MyEvent myEvent = new MyEvent();
    myEvent.topic = topic;
    myEvent.content = event;
    String json = gson.toJson(myEvent);
    try {
      session.getBasicRemote().sendText(json);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  
  */

  //only one subscriber per topic allowed:
  public static synchronized void addSubscriber(String topic_name, Subscriber subscriber) {
    
    //...
    
  }

  public static synchronized void removeSubscriber(String topic_name) {
    
    //...
    
  }

  public static void close() {
    try {
      session.close();
    } catch (Exception e) {
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
      
      //...
      
    } 
    //ordinary message from topic:
    else {
        session.
      
      //...
      
    }
  }

}
