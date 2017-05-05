package com.example.peterwinzell.w3cdemo_2;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.ToggleButton;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.cardiomood.android.controls.gauge.SpeedometerGauge;

import java.net.URI;
import java.net.URISyntaxException;

public class W3CActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private SpeedometerGauge speedometer;
    private SpeedometerGauge rpmmeter;
    private WebSocketClient mWebSocketClient;
    private TextView messageView;

    private ToggleButton m_brakeButton;
    private ToggleButton m_cruiseButton;

    private String m_SpeedSubscriptionId;
    private String m_rpmSubscriptionId;

    private String m_requestSpeedId = "1";
    private String m_requestRPMId = "2";

    private int m_brakerequestId = 1000;
    private int m_cruiserequestId = 3000;

    private int m_speed = 0;
    private int m_rpm;

    String  m_text1 = "";
    String  m_text2 = "";

    private UIHandler ui_updater = new UIHandler();

     //private String serverURL = "ws://192.168.31.122:8080";

    private String serverURL = "ws://192.168.0.10:8080";

    class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg){

            messageView.setText(m_text1 + m_text2);
            speedometer.setSpeed(m_speed,100,0);
            rpmmeter.setSpeed(m_rpm,100,0);

            sendMessageDelayed(obtainMessage(0),50);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    // Set parking brake signal sent to server
    private CompoundButton.OnCheckedChangeListener mBrakeButtonListener
            = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            String json = JsonDataUtility.getJSONDataSetBoolean(Integer.toString(m_brakerequestId++),JsonDataUtility.parkingbrake_VSS_leaf,isChecked);
            try {
                mWebSocketClient.send(json);
            }catch(Exception ex){
                ex.printStackTrace();
                m_text1 = ex.toString();
            }

        }
    };

    // Set cruise control signal sent to server
    private CompoundButton.OnCheckedChangeListener mCruiseButtonListener
            = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            String json = JsonDataUtility.getJSONDataSetBoolean(Integer.toString(m_cruiserequestId++),JsonDataUtility.cruisecontrol_VSS_leaf,isChecked);
            try {
                mWebSocketClient.send(json);
            }
            catch(Exception ex){
                ex.printStackTrace();
                m_text1 = ex.toString();
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w3_c);

        mTextMessage = (TextView) findViewById(R.id.messages);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        speedometer = (SpeedometerGauge) findViewById(R.id.speedometer3);
        rpmmeter = (SpeedometerGauge) findViewById(R.id.rpmeter);
        messageView = (TextView) findViewById(R.id.messages);

        speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                String s = String.valueOf((int) Math.round(progress));
                return s;
            }
        });

        rpmmeter.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                String s = String.valueOf((int) Math.round(progress));
                return s;
            }
        });

        speedometer.setMaxSpeed(200);
        speedometer.setLabelTextSize(25);
        speedometer.setMajorTickStep(30);
        speedometer.setMinorTicks(2);
        speedometer.addColoredRange(0, 80, Color.GREEN);
        speedometer.addColoredRange(80,150, Color.YELLOW);
        speedometer.addColoredRange(150, 200, Color.RED);
        //speedometer.setSpeed(100, 1000, 300);

        rpmmeter.setMaxSpeed(8000);
        rpmmeter.setLabelTextSize(30);
        rpmmeter.setMajorTickStep(1000);
        rpmmeter.setMinorTicks(50);
        rpmmeter.addColoredRange(0, 2000, Color.WHITE);
        rpmmeter.addColoredRange(2000, 6500, Color.YELLOW);
        rpmmeter.addColoredRange(6500, 8000, Color.RED);
        //rpmmeter.setSpeed(6000, 1000, 300);

        connectWebSocket();
        //connect listener with brake button res
        m_brakeButton = (ToggleButton) findViewById(R.id.brake);
        m_brakeButton.setOnCheckedChangeListener(mBrakeButtonListener);
        m_brakeButton.setChecked(true);

        // connect listener with cruise listener res
        m_cruiseButton = (ToggleButton) findViewById(R.id.cruisecontrol);
        m_cruiseButton.setOnCheckedChangeListener(mCruiseButtonListener);
        m_cruiseButton.setChecked(true);
    }

    private void HandleSpeed(JSONObject json){

        Double speed_d = null;
        try {
            speed_d = json.getDouble("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final int speed = (int) Math.round(speed_d);
        //final int progress = speed - m_oldspeed;

        //System.out.println("new speed is " + speed + " oldspeed is " + m_oldspeed + " progress is " + progress);
        final String json_s = json.toString();
        m_speed = speed;
        m_text1 = json_s;
        /*new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
            @Override
            public void run() {
                messageView.setText(json_s + messageView.getText() );
                speedometer.setSpeed(speed,200,0);
                // m_oldspeed = (int) Math.round(speedometer.getSpeed());
                        /*TextView textView = (TextView)findViewById(R.id.messages);
                        textView.append(outputS);
            }
        });*/
    }

    private void HandleRPM(JSONObject json){
        Double rpm_d = null;
        try {
            rpm_d = json.getDouble("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final int rpm = (int) Math.round(rpm_d);
        //final int progress = rpm -
        final String json_s = json.toString();
        m_rpm = rpm;
        m_text2 = json_s;
        /*new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
            @Override
            public void run() {
                //messageView.setText(json_s + messageView.getText() );
                rpmmeter.setSpeed(rpm,200,0);
            }
        });*/
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI(serverURL);
            ui_updater.sendMessage(ui_updater.obtainMessage(0));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");

                String jsonData1,jsonData2;



                jsonData1 = JsonDataUtility.getJSONDataSubscribe(m_requestSpeedId,JsonDataUtility.speed_VSS_leaf);
                mWebSocketClient.send(jsonData1);
                jsonData2 = JsonDataUtility.getJSONDataSubscribe(m_requestRPMId,JsonDataUtility.rpm_VSS_leaf);
                mWebSocketClient.send(jsonData2);
            }

            @Override
            public void onMessage(String s) {

                String message = s;
                int sp = 0;
                try {
                    JSONObject json = new JSONObject(message);
                    String action = json.getString("action");

                    if (action.equals("subscription")) {
                        String subId = json.getString("subscriptionId");
                        if (subId.equals(m_SpeedSubscriptionId)){
                            HandleSpeed(json);
                        }
                        else if (subId.equals(m_rpmSubscriptionId)){
                            HandleRPM(json);
                        }

                        message = action + " : " + json.getString("value") + " ";

                    } else if (action.equals("subscribe")) {

                        String requestId = json.getString("requestId");

                        if (requestId.equals(m_requestSpeedId))
                            m_SpeedSubscriptionId = json.getString("subscriptionId");
                        else if (requestId.equals(m_requestRPMId))
                            m_rpmSubscriptionId =  json.getString("subscriptionId");

                        message = action + " : " + requestId + " ";

                    }else if (action.equals("set")){
                        String requestId = json.getString("requestId");
                        if (json.isNull("error"))
                            message = "set succesful " + requestId;
                        else{
                            JSONObject errors = json.getJSONObject("error");
                            String emessage = errors.getString("message");
                            message = " set " + requestId + " " + emessage;
                        }
                    }

                } catch (org.json.JSONException ex) {
                    message = ex.getMessage();
                }
                System.out.println(message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };


        mWebSocketClient.connect();
    }
}

