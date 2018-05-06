package topicmanager;

import apiREST.apiREST_TopicManager;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherStub;
import subscriber.Subscriber;
import webSocketService.WebSocketClient;

public class TopicManagerStub implements TopicManager {
  apiREST_TopicManager apiREST;
  public String user;
  public String server_status;

  public TopicManagerStub(String user) {
    this.server_status = WebSocketClient.newInstance();
    this.user = user;
    apiREST = new apiREST_TopicManager();
  }

  public void close() {
    WebSocketClient.close();
  }

  public Publisher addPublisherToTopic(String topic) {
        PublisherStub newPublisher = new PublisherStub(topic);
        apiREST.addPublisherToTopic(topic);
        System.out.println("INFO -> TopicManager -> New publisher requested created on topic '"+topic+"'.");
        return newPublisher;
  }

  public int removePublisherFromTopic(String topic) {

      System.out.println("INFO -> TopicManager -> Publisher requested removed from topic '"+topic+"'.");
      return apiREST.removePublisherFromTopic(topic);

  }//

  public boolean isTopic(String topic_name) {
      return apiREST.isTopic(topic_name); 
  } //

  public Set<String> topics() {
      return apiREST.topics();
  } //

  public boolean subscribe(String topic, Subscriber subscriber) {
    WebSocketClient.addSubscriber(topic, subscriber);
    return true;
  } //

  public boolean unsubscribe(String topic, Subscriber subscriber) {
      WebSocketClient.removeSubscriber(topic);
    return true;
  } //
}