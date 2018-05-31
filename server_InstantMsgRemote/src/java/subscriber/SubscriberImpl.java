package subscriber;

import com.google.gson.Gson;
import java.io.IOException;
import javax.websocket.Session;
import util.MySubscriptionClose;
import util.MyEvent;

public class SubscriberImpl implements Subscriber {

  public Session session;

  public SubscriberImpl(Session session) {
    this.session = session;
  }

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

  @Override
  public void onClose(String topic, String cause) {
    Gson gson = new Gson();
    MySubscriptionClose mySubsClose = new MySubscriptionClose();
    mySubsClose.topic = topic;
    if (cause.equals("PUBLISHER")) {
      mySubsClose.cause = MySubscriptionClose.Cause.PUBLISHER;
    }
    else{
      mySubsClose.cause = MySubscriptionClose.Cause.SUBSCRIBER;
    }
    String json = gson.toJson(mySubsClose);
    try {
      session.getBasicRemote().sendText(json);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
