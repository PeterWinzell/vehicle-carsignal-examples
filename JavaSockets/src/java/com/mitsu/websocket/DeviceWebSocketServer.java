/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitsu.websocket;

/**
 *
 * @author peterwinzell
 */
import com.mitsubishi.VehicleSignalVerificationException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.Json;

@ApplicationScoped
@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
    
    @Inject
    private DeviceSessionHandler sessionHandler;
    
    
    @OnOpen
   public void open(Session session) {
            sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
        public void handleMessage(String message, Session session) {
            
        /*try {
            
            session.getBasicRemote().sendText("mupp");*/
            System.out.println("JSON :" + message);
            try (JsonReader reader = Json.createReader(new StringReader(message))) {
                JsonObject jsonMessage = reader.readObject();
                
                if ("subscribe".equals(jsonMessage.getString("action"))) {
                    if ("change".equals(jsonMessage.getString("type")))
                        sessionHandler.subscribe(session,jsonMessage.getString("path"));
                }
                
                if ("unsubscribe".equals(jsonMessage.getString("action"))) {
                    sessionHandler.stopsubscribe(session,jsonMessage.getString("path"));
                }
            }
            catch(VehicleSignalVerificationException exception){
                System.out.println(exception.getMessage());
                close(session);
            }
        /*}
        catch(IOException ex){
            Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}

