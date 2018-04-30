package webSocketService;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import javax.inject.Inject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import publisher.Publisher;
import subscriber.Subscriber;
import subscriber.SubscriberImpl;
import util.MySubscriptionRequest;
import util.Global;

@ServerEndpoint("/ws")
public class WebSocketServer {
  
  @Inject
  Global global;

  @OnMessage
  public void onMessage(String message, Session session)
    throws IOException, SQLException {
    System.out.println("onMessage: " + message);

    Gson gson = new Gson();
    MySubscriptionRequest mySubsReq = gson.fromJson(message, MySubscriptionRequest.class);
    if(mySubsReq.type==MySubscriptionRequest.Type.ADD){
      Subscriber subscriber = new SubscriberImpl(session);
      global.getTopicManager().subscribe(mySubsReq.topic, subscriber);
    }
    else if(mySubsReq.type==MySubscriptionRequest.Type.REMOVE){
      Publisher publisher = global.getTopicManager().publisher(mySubsReq.topic);
      Subscriber subscriber = publisher.subscriber(session);
      global.getTopicManager().unsubscribe(mySubsReq.topic, subscriber);
    }
  }

  @OnOpen
  public void onOpen(Session session) {
    System.out.println("new session: " + session.getId());
  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("closed session: " + session.getId());
  }

}
