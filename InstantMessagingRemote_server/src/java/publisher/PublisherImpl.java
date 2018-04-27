package publisher;

import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;
import subscriber.Subscriber;
import subscriber.SubscriberImpl;

public class PublisherImpl implements PublisherAdmin, Publisher {

  protected Set<Subscriber> subscriberSet;
  protected int numPublishers;
  protected String topic;

  public PublisherImpl(String topic) {
    subscriberSet = new HashSet<Subscriber>();
    numPublishers = 1;
    this.topic = topic;
  }
 
  
  // INFO: Functions copied 25/4 - consider update if improved FIX
  
     public int incPublishers(){
        return ++numPublishers;
    }
    public int decPublishers(){
        return --numPublishers;
    }
    public void attachSubscriber(Subscriber subscriber) {
        this.subscriberSet.add(subscriber);
        //...
    }
    public void detachSubscriber(Subscriber subscriber) {
        this.subscriberSet.remove(subscriber);
        subscriber.onClose(topic,"SUBSCRIBER");
        //...
    }
    public void detachAllSubscribers() {
        try{
            for (Subscriber sub : this.subscriberSet)
            sub.onClose(topic,"PUBLISHER");
            }
        catch(Exception exx){
            System.out.println("DEBUG: exception catched, sub dont exist");
            }
        this.subscriberSet.clear();
    }
    
    public void publish(String topic, String event) {
        int num =0;
        for (Subscriber sub : subscriberSet){
            num ++;
            sub.onEvent(topic, event);
            System.out.println("DEBUG: Publisher published to subscriber # " + num);
        }  
    }
  
  
  public Subscriber subscriber(Session session) {
    for (Subscriber subscriber : subscriberSet) {
      SubscriberImpl subscriberImpl = (SubscriberImpl) subscriber;
      if (subscriberImpl.session == session) {
        return subscriber;
      }
    }
    return null;
  }


}



  /*
  @Override
  public int incPublishers() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int decPublishers() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void attachSubscriber(Subscriber subscriber) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void detachSubscriber(Subscriber subscriber) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void detachAllSubscribers() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void publish(String topic, String event) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  */
  

