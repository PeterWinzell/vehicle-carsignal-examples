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
public class SignalListener {

    
    private Session m_session;
    private CarSignalListener m_listener;
    private String m_signalpath;
    
    public SignalListener(Session session, CarSignalListener listener, String signalpath){
        m_session = session;
        m_listener = listener;
        m_signalpath = signalpath;
    }
    
    public Session getM_session() {
        return m_session;
    }

    public CarSignalListener getM_listener() {
        return m_listener;
    }

    public String getM_signalpath() {
        return m_signalpath;
    }
    
    public boolean Equal(SignalListener anotherListener){
        return (anotherListener.m_session.getId().equals(m_session.getId()) && 
                anotherListener.m_signalpath.equals(m_signalpath) );
    }
}
