package util;

/**
 *
 * @author juanluis
 */
public class MySubscriptionRequest {
  public enum Type { ADD, REMOVE };
  public Type type;
  public String topic;
}
