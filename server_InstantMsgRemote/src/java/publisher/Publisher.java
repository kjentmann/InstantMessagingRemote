package publisher;

import javax.websocket.Session;
import subscriber.Subscriber;

public interface Publisher {
    public void publish(String topic, String event);
    
    //this is necessary on the remote version:
    Subscriber subscriber(Session sesion);
}
