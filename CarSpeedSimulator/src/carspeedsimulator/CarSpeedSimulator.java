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
package carspeedsimulator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.GridPane;
import eu.hansolo.medusa.FGauge;
import eu.hansolo.medusa.FGaugeBuilder;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeDesign;
import eu.hansolo.medusa.GaugeDesign.GaugeBackground;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javax.json.spi.JsonProvider;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.stream.JsonParser;
import javax.json.JsonReader;
import javax.json.stream.JsonParser.Event;


//import java.awt.event.WindowEvent;
/**
 *
 * @author peterwinzell
 */
public class CarSpeedSimulator extends Application {

    // private Vehicle vehicle;
    private GridPane pane;
    private Gauge speedGauge;
    private Button btn;
    
    private String subId;
    private VehicleClient vehicleClient;

    public synchronized void speedChange(int speed) {
        speedGauge.setValue(speed);
    }

    @Override
    public void init() {
        
        vehicleClient = new VehicleClient();
        speedGauge = new Gauge();
        speedGauge.setSkinType(Gauge.SkinType.SIMPLE_DIGITAL);
        //speedGauge.setBackgroundPaint(PAINT);
        speedGauge.setBarColor(Color.CHARTREUSE);
        speedGauge.setUnit("km/h");
        speedGauge.setBarBackgroundColor(Color.WHITE);
        speedGauge.setUnitColor(Color.WHITE);
        speedGauge.setValueColor(Color.CHARTREUSE);
        speedGauge.setMinValue(0);
        speedGauge.setMaxValue(250);
        speedGauge.setDecimals(0);
        speedGauge.setUnitColor(Color.CORAL);
        speedGauge.setPrefSize(500, 500);
        speedGauge.setNeedleBehavior(Gauge.NeedleBehavior.OPTIMIZED);

        FGauge fGauge = FGaugeBuilder
                .create()
                .prefSize(500, 500)
                .gauge(speedGauge)
                .gaugeDesign(GaugeDesign.METAL)
                .gaugeBackground(GaugeBackground.CARBON)
                .foregroundVisible(true)
                .build();

        pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(15);
        pane.setBackground(new Background(new BackgroundFill(Color.rgb(39, 44, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.add(fGauge, 0, 1);

        // Start button
        btn = new Button();
        btn.setText("LISTEN TO SPEED !!! ");
       
        pane.add(btn, 0, 0);

    }

    private int ValidateJSonMessage(String message){
        System.out.println(message);
        
        if (message.contains("SHIT"))
            return -1;
         JsonReader aReader = Json.createReader(new StringReader(message));
         JsonObject jsonObject = aReader.readObject();
         
         
         String actionS = jsonObject.getString("action");
         if (actionS.equals("subscription")){
             //subId = jsonObject.getString("subscriptionid");
             String value = jsonObject.getString("value");
             float val = Float.parseFloat(value);
             return Math.round(val);
         }
         else if(actionS.equals("subscribe")){
             subId = jsonObject.getString("subscriptionId");
             return 0;
         }
         return -1;
        
    }
    
    @Override
    public void start(Stage primaryStage) { 
        
        try {
            URI uri = new URI("wss://127.0.0.1:8080");
            System.out.println("URI is: " + uri.toString());
            
            
            vehicleClient.addMessageHandler(new VehicleClient.MessageHandler() {
                public void handleMessage(String message) {
                   
                    int val = ValidateJSonMessage(message);
                    if (val >= 0) speedChange(val);
                    else
                        System.out.println(" Message error , validation failed " +  message);
                }
            });
            
            Scene scene = new Scene(pane);

            
            btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                JsonProvider provider = JsonProvider.provider();
                
                
                JsonObject messageObject = provider.createObjectBuilder()
                        .add("action","subscribe")
                        .add("path","Signal.Drivetrain.Transmission.Speed")
                        .add("requestId","1")
                        .add("timestamp",new Timestamp(System.currentTimeMillis()).toString())
                        .build();
                
                StringWriter stWriter = new StringWriter();
                try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
                    jsonWriter.writeObject(messageObject);
                }
                
                String payLoad = stWriter.toString();
                
                vehicleClient.sendMessage(payLoad);
            }
        });
       
        primaryStage.setTitle("Speed simulation");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                
                JsonProvider provider = JsonProvider.provider();
                
                JsonObject messageObject = provider.createObjectBuilder()
                        .add("action","unsubscribe")
                        .add("subscriptionId",subId)
                        .add("requestId",10)
                        .build();
                
                StringWriter stWriter = new StringWriter();
                JsonWriter jsonWriter;
                jsonWriter = Json.createWriter(stWriter);
                jsonWriter.writeObject(messageObject);
                jsonWriter.close();
                
                String payLoad = stWriter.toString();
                vehicleClient.sendMessage(payLoad);
                
            }
        });

        primaryStage.show();
        vehicleClient.connectToServer(uri);
        
        } 
        catch (URISyntaxException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(CarSpeedSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
