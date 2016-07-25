/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitsubishi;

import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author peterwinzell
 */
public interface DoorInterface extends CarSignalListener {
 
        void doorDataChange(JsonObject payload, Session session);
        void doorDataValue(JsonObject payload, Session session);
}
