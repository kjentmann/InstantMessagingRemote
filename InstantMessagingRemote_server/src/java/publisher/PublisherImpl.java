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
 
  
  // INFO: Functions copied 25/4 - consider update if improved FIX updateded may
  
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
           try{
            for (Subscriber sub : this.subscriberSet)
                   subscriber.onClose(this.topic,"SUBSCRIBER");
                    this.subscriberSet.remove(subscriber);

            }
        catch(Exception exx){
            System.out.println("WARNING -> Server -> Publisher -> Exception catched, sub dont exist");
            }

        //...
    }
    public void detachAllSubscribers() {
        try{
            for (Subscriber sub : this.subscriberSet)
            sub.onClose(topic,"PUBLISHER");
            this.subscriberSet.clear();
            }
        catch(Exception exx){
            System.out.println("WARNING -> Server -> Publisher -> Exception catched, sub dont exist");
            }
    }
    
    public void publish(String topic, String event) {
        int num =0;
        try{
        for (Subscriber sub : subscriberSet){
            num ++;
            sub.onEvent(topic, event);
            System.out.println("INFO -> Server -> Publisher -> Published to subscriber # " + num);
        }
        }
        catch(Exception ex){
            System.out.println("ERROR -> Server -> Publisher -> Failed to publish. Call a friend.");
        }
        if (subscriberSet.isEmpty()){
              System.out.println("WARNING -> Server -> Publisher -> No subscribers listening on the topic :( ");
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
  

