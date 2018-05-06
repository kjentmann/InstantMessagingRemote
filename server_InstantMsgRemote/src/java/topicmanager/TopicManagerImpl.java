package topicmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherAdmin;
import publisher.PublisherImpl;
import subscriber.Subscriber;

public class TopicManagerImpl implements TopicManager {

  protected Map<String, PublisherAdmin> topicMap;

  public TopicManagerImpl() {
    topicMap = new HashMap<String, PublisherAdmin>();
  }
  
    public boolean isTopic(String topic){
        if( this.topicMap.containsKey(topic))
            return true;
        else
            return false;
    }
     @Override
    public Set<String> topics(){
        Set<String> topicSet = new HashSet<String>();
        if (topicMap.isEmpty()){
            return null;
        }
        try{
            for (Map.Entry<String, PublisherAdmin> entry : topicMap.entrySet()){
                topicSet.add(entry.getKey());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            }
        return topicSet;
    }
     @Override
    public Publisher addPublisherToTopic(String topic){
        PublisherAdmin publishAdm;
        if (isTopic(topic)){
            publishAdm = topicMap.get(topic);
            publishAdm.incPublishers();
            System.out.println("INFO -> Server -> TopicManager -> One more pub on topic " + topic + " added");
        }
        else{
            System.out.println("INFO -> Server -> TopicManager -> Publisher on topic '" + topic + "' does not exist. Creating..");
            publishAdm = new PublisherImpl(topic);
            topicMap.put(topic, publishAdm);
            System.out.println("INFO -> Server -> TopicManager -> Publisher on topic '" + topic + "' added.");

        }
        System.out.println("TopicMAP: " + topicMap);
        return publishAdm;
    }
    
     @Override
    public boolean subscribe(String topic, Subscriber subscriber){
        if(isTopic(topic)){
           this.topicMap.get(topic).attachSubscriber(subscriber);
            System.out.println("INFO -> Server -> TopicManager -> Subscribed");
            return true;
        }else{
            System.out.println("INFO -> Server -> TopicManager -> Subscribtion failed");
            return false;
        }
    }
    
     @Override
    public boolean unsubscribe(String topic, Subscriber subscriber){
        if(isTopic(topic)){
            this.topicMap.get(topic).detachSubscriber(subscriber);
            return true;
        }
        else{
            return false;
        }
    }
    public Publisher publisher(String topic) {
    return topicMap.get(topic);
  }
    @Override
    public int removePublisherFromTopic(String topic) {
         if (topicMap.get(topic).decPublishers()<1){
            topicMap.get(topic).detachAllSubscribers();
            this.topicMap.remove(topic);
            System.out.println("INFO -> Server -> TopicManager ->  No more publisher of " + topic + ". Removing pub..");
            return -1;
        }
         else{
            System.out.println("INFO -> Server -> TopicManager ->  Publishers still exist on topic. Subscribers not removed.");
            return 0;
        }
    }
}
