/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitsubishi;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author peterwinzell
 */
public class Acceleration {

    double acceleration = 2.78; // m/s^2
    boolean go = true;
    Object Lock = null;
    Timer timer;
    
  
    public  Acceleration() {
        Lock = new Object();
        
        timer = new Timer();
        timer.schedule(new CalcAcceleration(),10000,5000);
    }
    
    class CalcAcceleration extends TimerTask{
        public void run(){
            if (go){
                setNewAcceleration();
            }
            else{
                timer.cancel();
            }
        }
    }
    
    void setGo(boolean goon){
        go = goon;
    }
    
    void setNewAcceleration(){
        synchronized(Lock){
            acceleration = (Math.random() * 60 - 30) / 10; 
        }    
    }
    
    double getAcceleration(){
       synchronized(Lock){
           return this.acceleration;
       }
    }
    
    void setAcceleration(double accel){
        synchronized(Lock){
            this.acceleration = accel;
        }
    }
    
    public void stop(){
        timer.cancel();
    }
}
