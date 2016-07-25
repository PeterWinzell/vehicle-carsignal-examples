/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carspeedsimulator;

/**
 *
 * @author peterwinzell
 */
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * Vehicle Information Client
 *
 * @author Peter Winzell
 */
@ClientEndpoint
public class VehicleClient {

    Session userSession = null;
    private MessageHandler messageHandler;
    private WebSocketContainer container;

    public VehicleClient() {
        try {
            container = ContainerProvider.getWebSocketContainer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void connectToServer(URI endpointURI)  {
        try {
             container.connectToServer(this, endpointURI);
        } catch (DeploymentException|IOException ex) {
            Logger.getLogger(VehicleClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        System.out.println(" sending " +  message);
        this.userSession.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     *
     * @author Peter Winzell
     */
    
    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}