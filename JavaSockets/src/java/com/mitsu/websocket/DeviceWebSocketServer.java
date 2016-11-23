/* 
 * Copyright (C) 2016 Peter Winzell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

