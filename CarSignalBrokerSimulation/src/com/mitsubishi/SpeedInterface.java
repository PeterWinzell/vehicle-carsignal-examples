/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitsubishi;

import javax.websocket.Session;

/**
 *
 * @author peterwinzell
 */
public interface SpeedInterface extends CarSignalListener {
    static int MAX_SPEED_LISTENERS = 10;
    void speedChange(Session session,int speed);
};
