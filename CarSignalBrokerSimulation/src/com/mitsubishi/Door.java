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
