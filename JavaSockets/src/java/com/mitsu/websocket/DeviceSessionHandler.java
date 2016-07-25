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

import com.mitsubishi.Vehicle;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;
import com.mitsubishi.VehicleSignalVerificationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;


@ApplicationScoped
public class DeviceSessionHandler {
    private int deviceid = 0;
    private final Set sessions = new HashSet<>();
    private final Set devices = new HashSet<>();
    
    private  CarSignals carsignalsinstance = null;
    
    private  CarSignals getCarSignals() {
      if (carsignalsinstance == null) 
          carsignalsinstance = new CarSignals();
      return carsignalsinstance;
    }
    
    public void addSession(Session session) {
        getCarSignals(); // start simulation
        sessions.add(session);
        System.out.println("new session added");
    }
    

    public void removeSession(Session session) {
        System.out.println(" session removed and closed " + session.getId());
        sessions.remove(session);
        getCarSignals().removeAllSessionListeners(session);
    }
    
    public List getDevices() {
        return new ArrayList<>(devices);
    }

    private boolean verifyPath(String path) throws VehicleSignalVerificationException{
        if (!(path.contains("Vehicle.speed") || path.contains("Vehicle.door"))){
                throw new VehicleSignalVerificationException("unsubscribe path not verified " +  path);
        }
        return true;
    }
    
    public void subscribe(Session session,String path) throws VehicleSignalVerificationException{
        
        if (path.contains("Vehicle")){
            if (path.contains("speed")){
                    //path = path.replace("Vehicle.Speed","");
                    System.out.println(session.getId() +" "+ path);
                    getCarSignals().addListener(session,path,"speed");
            }
            if (path.contains("door")){
                    //path = path.replace("Vehicle.Door", "");
                    System.out.println(session.getId() + " " + path);
                    getCarSignals().addListener(session,path,"door");
            }
        }     
       else
                throw new VehicleSignalVerificationException("subscribe not able to parse " + path);
    }
    
    
    public void stopsubscribe(Session session,String path) throws VehicleSignalVerificationException{
       if (verifyPath(path)){
           getCarSignals().removeListener(session,path);
       }    
       else
          throw new VehicleSignalVerificationException("subscribe not able to parse " + path);
    }
    
    

    private void sendToAllListeners(JsonObject message) {
        // Need to handle per message and session. Is this session listening for signal x 
        sessions.stream().forEach((session) -> {
            sendToSession((Session)session, message);
        });
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
            //session.getAsyncRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class CarSignals implements com.mitsubishi.SpeedInterface, com.mitsubishi.DoorInterface{
        
        private com.mitsubishi.Vehicle vehicle = null;
        protected CarSignals(){
            vehicle = new com.mitsubishi.Vehicle();
            //vehicle.addSpeedListener(this);
            vehicle.GentleMenStartYourEnginge();
        }
        
        @Override
        public synchronized void speedChange(Session session,int speed) {
            JsonProvider provider = JsonProvider.provider();
            // Device device = getDeviceById(id);
            speed = (int )Math.round(3.6 * (long)speed);
            //@TODO move this code down to caller
            JsonObject addMessage = provider.createObjectBuilder()
                .add("path", "Vehicle.speed")
                .add("value",speed)
                .build();
            sendToSession(session,addMessage);
        }
        
        
        public void addListener(Session session, String path,String signal){
            
            if (signal.equals("speed"))
                vehicle.addSpeedListener(this, session, path);
            else if (signal.equals("door"))
                vehicle.addDoorChangeListeners(this,session,path);
        }
        
        public void removeListener(Session session, String path){
           vehicle.removeListener(session,path);    
        }
        
        public void removeAllSessionListeners(Session session){
            vehicle.removeListener(session,"session");
        }
        
        public void stop(){
            if(vehicle != null)
                vehicle.stop();
        }

        @Override
        public void doorDataChange(JsonObject payload,Session session) {
            System.out.println(payload);
            sendToSession(session,payload);
        }

        @Override
        public void doorDataValue(JsonObject payload,Session session) {
            sendToSession(session,payload);
        }
           
    }
}
