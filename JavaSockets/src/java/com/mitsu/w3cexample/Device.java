/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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