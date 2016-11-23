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
