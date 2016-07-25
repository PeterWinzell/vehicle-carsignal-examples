/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitsubishi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

/**
 *
 * @author peterwinzell
 */
public class Doors {

    private final List<Door> doors = new ArrayList<>(10);
    private final List<SignalListener> listeners = new ArrayList<>(10);

    Timer datachange;

    public Doors() {
        for (Position pos : Position.values()) {
            for (Bodyplacement orient : Bodyplacement.values()) {
                //Door aDoor = new Door();
                Door aDoor = new Door(orient, pos, 0, false, false);
                doors.add(aDoor);
            }
        }
        // Start simulation
        System.out.println(" door simulation has started ");
        (datachange = new Timer()).schedule(new DoorDataFetch(), 20000, 20000);
    }

    public void addListener(SignalListener listener) {
        listeners.add(listener);
    }
    
    public ArrayList getDoorListeners(){
        return (ArrayList) listeners;
    }

    // don't listen to this signalpath anymore
    /*public void removeListener(String signalpath) {
        boolean found = false;
        int index = 0;
        while (!found && index < listeners.size()) {
            if (listeners.get(index).getM_signalpath().equals(signalpath)) {
                found = true;
            }
            index++;
        }
        if (found) {
            listeners.remove(index);
        }
    }*/

    String getPositionString(Position pos) {

        switch (pos) {
            case FRONT:
                return "0";
            case MIDDLE:
                return "1";
            case REAR:
                return "2";
        }
        return "";
    }

    String getPlacementString(Bodyplacement place) {
        switch (place) {
            case LEFT:
                return "left";
            case RIGHT:
                return "right";
        }
        return "";
    }

    String Join(String path, String pos, String place, String signalchange) {
        String pathsub = path.substring(0, path.indexOf("door") + 4);
        return pathsub + "." + pos + "." + place + "." + signalchange;
    }

    JsonObject getPayload(boolean value, String zePath) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("path", zePath)
                .add("value", value).build();
    }

    JsonObject getPayload(int value, String zePath) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("path", zePath)
                .add("value", value).build();
    }

    /* this is where we check if the subscribed path matches 
        this door and the signal change, e.g open,locked or windos pos */
    boolean listenerPathMatch(Door aDoor, String signalchange, String signalpath) {
        return true;
    }

    public void CheckAndSend(Door aDoor, String signalchange) {
        // System.out.println(" enter check and send");
        for (int i = 0; i < listeners.size(); i++) {
            SignalListener listener = listeners.get(i);
            if (listenerPathMatch(aDoor, signalchange, listener.getM_signalpath())) {
                switch (signalchange) {
                    case "open":
                        {
                            boolean value = aDoor.isOpen();
                            String zeValuePath = Join(listener.getM_signalpath(),
                                    getPositionString(aDoor.getM_position()), getPlacementString(aDoor.getM_orientation()), signalchange);
                            ((DoorInterface) listener.getM_listener()).doorDataChange(getPayload(value, zeValuePath),
                                    listener.getM_session());
                            break;
                        }
                    case "locked":
                        {
                            boolean value = aDoor.isLocked();
                            String zeValuePath = Join(listener.getM_signalpath(),
                                    getPositionString(aDoor.getM_position()), getPlacementString(aDoor.getM_orientation()), signalchange);
                            ((DoorInterface) listener.getM_listener()).doorDataChange(getPayload(value, zeValuePath),
                                    listener.getM_session());
                            break;
                        }
                    case "windowpos":
                        {
                            int value = aDoor.getWindowpos();
                            String zeValuePath = Join(listener.getM_signalpath(),
                                    getPositionString(aDoor.getM_position()), getPlacementString(aDoor.getM_orientation()), signalchange);
                            ((DoorInterface) listener.getM_listener()).doorDataChange(getPayload(value, zeValuePath),
                                    listener.getM_session());
                            break;
                        }
                    default:
                        break;
                }
            }
        }
    }
    
    String doorLogString(Door aDoor){
        return "door." + getPlacementString(aDoor.getM_orientation()) + "." + getPositionString(aDoor.getM_position());
    }
    // Door simulation
    class DoorDataFetch extends TimerTask {

        @Override
        public void run() {
            SimulateDoorDataChanges(); // Don't care if the car is running 200 km/h...for now
        }

        private boolean flip_a_coin() {
            return (Math.random() > 0.5);
        }

        private int getWindowPos() {
            return (int) Math.round(Math.random() * 100);
        }

        private void SimulateDoorDataChanges() {
            int index = 0;
            for (Position pos : Position.values()) {
                for (Bodyplacement orient : Bodyplacement.values()) {
                    Door aDoor = (Door) doors.get(index++);
                    if (flip_a_coin()) {
                        aDoor.open = !aDoor.open;
                        // if match in changelistener add to return payload
                        CheckAndSend(aDoor, "open");
                        System.out.println(doorLogString(aDoor) + " open checked ");

                    }
                    if (flip_a_coin()) {
                        aDoor.locked = !aDoor.locked;
                        // if match in changelistener add to return payload
                        CheckAndSend(aDoor, "locked");
                        System.out.println(doorLogString(aDoor) + " locked checked ");
                    }
                    // if match in changelistener add to return payload
                    if (flip_a_coin()) { // fifty fifty to to change windowpos
                        aDoor.windowpos = getWindowPos();
                        CheckAndSend(aDoor, "windowpos");
                        System.out.println(doorLogString(aDoor) + " window checked");
                    }
                }
            }
        }

    }
}
