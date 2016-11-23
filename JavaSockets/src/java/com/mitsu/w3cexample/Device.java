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
package com.mitsu.w3cexample;

/**
 *
 * @author peterwinzell
 */


/*public class Device {

    private int id;
    private String name;
    private String status;
    private String type;
    private String description;

    public Device() {
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}*/

public class Device {

    private int id;
    private String model;
    private int speed;
    public Device() {
    }
    
    public int getId() {
        return id;
    }
    
    public String getModel() {
        return model;
    }


    public void setId(int id) {
        this.id = id;
    }
    
    public void setModel(String model) {
        this.model = model;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }
}