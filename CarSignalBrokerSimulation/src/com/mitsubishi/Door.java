/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitsubishi;

import javafx.geometry.Orientation;

/**
 *
 * @author peterwinzell
 */


        
public class Door {
    
    protected Bodyplacement m_orientation;
    protected Position m_position;
    protected int windowpos;
    protected boolean open;
    protected boolean locked;
    
    public Door(){}
    
    public Door(Bodyplacement p_o,Position p_p,int wp,boolean op,boolean locked){
        this.m_orientation = p_o;
        this.m_position = p_p;
        this.windowpos = wp;
        this.open = op;
        this.locked = locked;
    }
    
    public Bodyplacement getM_orientation() {
        return m_orientation;
    }

    public void setM_orientation(Bodyplacement m_orientation) {
        this.m_orientation = m_orientation;
    }

    public Position getM_position() {
        return m_position;
    }

    public void setM_position(Position m_position) {
        this.m_position = m_position;
    }

    public int getWindowpos() {
        return windowpos;
    }

    public void setWindowpos(int windowpos) {
        this.windowpos = windowpos;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
   
    
}
