package facadeREST;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import publisher.Publisher;
import util.MyEvent;
import util.Global;

/**
 *
 * @author upcnet
 */
@Stateless
@Path("publisher")
public class PublisherFacadeREST {
  
  @Inject
  Global global;
  
  @POST
  @Path("publish")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public void publish(MyEvent myEvent) {
      System.out.println("DEBUG: server received message! redirecting..");
    Publisher publisher = global.getTopicManager().publisher(myEvent.topic);
    publisher.publish(myEvent.topic, myEvent.content);
  }

}
