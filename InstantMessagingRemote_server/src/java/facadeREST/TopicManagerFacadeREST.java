package facadeREST;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import util.MyBoolean;
import util.MyInteger;
import util.MyString;
import util.Global;

/**
 *
 * @author upcnet
 */
@Stateless
@Path("topicmanager")
public class TopicManagerFacadeREST {
  
  @Inject
  Global global;

  @POST
  @Path("addtopic")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public void addPublisherToTopic(MyString topic) {
    global.getTopicManager().addPublisherToTopic(topic.content);
  }

  @POST
  @Path("removetopic")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public MyInteger removePublisherFromTopic(MyString topic) {
    MyInteger myInteger = new MyInteger();
    myInteger.content = global.getTopicManager().removePublisherFromTopic(topic.content);
    return myInteger;
  }

  @POST
  @Path("istopic")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public MyBoolean isTopic(MyString topic) {
    MyBoolean myboolean = new MyBoolean();
    if (global.getTopicManager().isTopic(topic.content)) {
      myboolean.content = true;
    } else {
      myboolean.content = false;
    }
    return myboolean;
  }

  @GET
  @Path("topics")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public List<MyString> topics() {
      try{
    List<MyString> topics = new ArrayList<MyString>();
    for (String topic : global.getTopicManager().topics()) {
      MyString myString = new MyString();
      myString.content = topic;
      topics.add(myString);
    }
        return topics;

      }
      catch(Exception aa){
          System.out.println("TJAJAAA");
      }
              return null;

  }
  

}
