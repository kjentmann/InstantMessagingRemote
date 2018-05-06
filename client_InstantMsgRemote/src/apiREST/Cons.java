/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apiREST;

/**
 *
 * @author upcnet
 */
public interface Cons {
  
  //String SERVER_IP="localhost:8080";  //Run server locally
  String SERVER_IP="46.249.225.122:62987";                //Run server remotly (Norway)<<<<<<<<<<<<<<<<<<<
  String SERVER_REST = "http://"+SERVER_IP+"/InstantMessagingRemote_server/webresources";
  String SERVER_WEBSOCKET = "ws://"+SERVER_IP+"/InstantMessagingRemote_server/ws";
  
}
