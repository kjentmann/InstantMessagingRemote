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

  public TopicManagerStub(String user) {
    WebSocketClient.newInstance();
    this.user = user;
    apiREST = new apiREST_TopicManager();
  }

  public void close() {
    WebSocketClient.close();
  }

  public Publisher addPublisherToTopic(String topic) {
        PublisherStub newPublisher = new PublisherStub(topic);
        apiREST.addPublisherToTopic(topic);
        System.out.println("New publisher requested created on topic '"+topic+"'.");
        return newPublisher;
        // TODO:FIX Tis func creates a new pub every time. OK?
  }

  public int removePublisherFromTopic(String topic) {
       System.out.println("Publisher requested removed from topic '"+topic+"'.");
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

        /*
        
        if (isTopic(topic)){
            newPublisher = topicMap.get(topic);
            newPublisher.incPublishers();
            System.out.println("DEBUG: One more pub on topic " + topic + " added");
        }
        else{
            System.out.println("DEBUG: publisher on topic " + topic + " does not exist. Creating..");
            newPublisher = new PublisherStub(topic);
            topicMap.put(topic, publishAdm);
        }
        return publishAdm;
      
     // PublisherStub
    //...
    return null;

  }
*/