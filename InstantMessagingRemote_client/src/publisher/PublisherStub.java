package publisher;

import apiREST.apiREST_Publisher;
import util.MyEvent;

public class PublisherStub implements Publisher {
  String topic;

  public PublisherStub(String topic) {
    this.topic = topic;
  }
  public void publish(String topic, String event) {
      MyEvent newEvent = new MyEvent();
      newEvent.content=event;
      newEvent.topic = topic;
      apiREST_Publisher pub = new  apiREST_Publisher();
      apiREST_Publisher.publish(newEvent);
  }
}
