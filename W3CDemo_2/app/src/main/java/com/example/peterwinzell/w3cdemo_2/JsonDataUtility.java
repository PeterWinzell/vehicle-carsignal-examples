package com.example.peterwinzell.w3cdemo_2; /**
 * Created by peterwinzell on 02/05/17.
 */


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by peterwinzell on 02/05/17.
 */

public class JsonDataUtility {

    public static String speed_VSS_leaf         = "Signal.Drivetrain.Transmission.Speed";
    public static String rpm_VSS_leaf           = "Signal.Drivetrain.InternalCombustionEngine.RPM";
    public static String parkingbrake_VSS_leaf = "Signal.Chassis.ParkingBrake.IsEngaged";
    public static String cruisecontrol_VSS_leaf = "Signal.ADAS.CruiseControl.IsActive";

    static String getJSONDataSubscribe(String requestId, String signalvalue) {

        JSONObject jsonObject = new JSONObject();

        //JSONObject filterObject = new JSONObject();

        try {
            jsonObject.put("action", "subscribe");
            jsonObject.put("path", signalvalue);
            //filterObject.put("interval", 500);
            //jsonObject.put("filters", filterObject);
            jsonObject.put("requestId", requestId);
            jsonObject.put("timestamp", System.currentTimeMillis() / 1000);
        }
        catch(JSONException ex){
          ex.printStackTrace();
        }

        return jsonObject.toString();
    }

    static String getJSONDataSetBoolean(String requestId,String signalValue,boolean value){
        JSONObject jsonObject = new JSONObject();
        try{

            jsonObject.put("action","set");
            jsonObject.put("path",signalValue);
            jsonObject.put("value",value);
            jsonObject.put("requestId",requestId);
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
        return jsonObject.toString();
    }
}