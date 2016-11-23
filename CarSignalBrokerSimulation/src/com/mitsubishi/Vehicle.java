/* 
 * The MIT License
 *
 * Copyright 2016 Peter Winzell.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.mitsubishi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.websocket.Session;

/**
 * The Car accelerates initially 100 km/h in 10s with a weight of 1000kg
 *
 * @author peterwinzell
 */


public class Vehicle {
    
    
    long t1, t2;
    private final ArrayList<SignalListener> speedListeners = new ArrayList(10);
    private int speed = 0;
    private double speed_r = 0;
    
    private final Acceleration acceleration; // approx 100 km/h in 10 s
    // private static int weight = 1000; // The car weighs 1000 kilos

    private final double elaspedtime = 0;

    private Object SpeedLock = new Object();
    Timer speedTimer; // get new speed from current acceleration
    Timer gaugeUpdate; // update gauge
    private int delay = 10;

    // Doors object
    Doors doors;
    
    public Vehicle() {
        speedTimer = new Timer();
        gaugeUpdate = new Timer();
        acceleration = new Acceleration();     
        doors = new Doors();
    }

    
    
    public void addSpeedListener(SpeedInterface speedlistener,Session session,String path) {
        SignalListener newL = new SignalListener(session,speedlistener,path);
        if (!SignalListenerMember(speedListeners,newL))
            speedListeners.add(newL);
    }

    public void addDoorChangeListeners(DoorInterface doorListener,Session session,String path){
        SignalListener newL = new SignalListener(session,doorListener,path);
        if (!SignalListenerMember(doors.getDoorListeners(),newL))
            doors.addListener(newL);
    }
    
    private void removeListener(Session session, String path,ArrayList listeners){
        
        boolean found = false;
        int index = 0;
        while (!found && index <= listeners.size()){
            SignalListener sl = (SignalListener) listeners.get(index);
            found = (sl.getM_session().getId().equals(session.getId())) && (path.equals(sl.getM_signalpath()));
            if (found){
                listeners.remove(sl);
                break;
            }    
            index++;
        }
            
    }
    
    public boolean SignalListenerMember(ArrayList<SignalListener> listeners,SignalListener newL){
        for (SignalListener listener : listeners){
            if (listener.Equal(newL))
                return true;
        }
        return false;
    }
    
    public void removeAllSessionListeners(Session session,ArrayList<SignalListener> listeners){
        List<SignalListener> foundsessions = new ArrayList<SignalListener>();
        for(SignalListener listener : listeners){
            if(listener.getM_session().getId().equals(session.getId())){
                foundsessions.add(listener);
            }
        }
        listeners.removeAll(foundsessions);
    }
    
    public void removeListener(Session session,String path){
        if (path.contains("speed")){
            removeListener(session,path,speedListeners);
        }
        else if (path.contains("door")){
            removeListener(session,path,doors.getDoorListeners());
        }
        else if (path.equals("session")){
            removeAllSessionListeners(session,speedListeners);
            removeAllSessionListeners(session,doors.getDoorListeners());
        }
    }
    
    public void GentleMenStartYourEnginge(){
        t1 = System.currentTimeMillis(); // take first time
        speedTimer.schedule(new EnginePulse(), 10, 10);
        gaugeUpdate.schedule(new DataUpdate(),0,50);
    }
    
    public void stop(){
            acceleration.stop();
            speedTimer.cancel();
            gaugeUpdate.cancel();
    }
    
    class EnginePulse extends TimerTask {

        public void run() {
            calcCurrentSpeed();
        }
 
        private void calcCurrentSpeed() {
            // V = v0 + a * t;
            synchronized (SpeedLock) {

                t2 = System.currentTimeMillis();
                
                double elaspedTime = ( (t2 - t1) / 1000.0);
                double accel = acceleration.getAcceleration();
                
                speed_r = speed_r + accel * elaspedTime; 
                if (speed_r <= 0.0) speed_r = 0.0;
                
                if (speed_r >= 69) 
                    speed_r = 69;
                
                speed = (int) Math.round(speed_r);
                t1 = System.currentTimeMillis();
                
            }
        }
    }
    
    class DataUpdate extends TimerTask {

        public void run() {
            calcCurrentSpeed();
        }
 
        private void calcCurrentSpeed() {
            // V = v0 + a * t;
            synchronized (SpeedLock) {
                for (int i = 0; i < speedListeners.size(); i++) {
                    SignalListener listener = (SignalListener)speedListeners.get(i);
                    SpeedInterface speedInterface = (SpeedInterface)listener.getM_listener();
                    speedInterface.speedChange(listener.getM_session(),speed);
                }
            }
        }
    }

}
