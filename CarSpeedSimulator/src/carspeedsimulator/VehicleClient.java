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

/**
 *
 * @author peterwinzell
 */
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;

/**
 * Vehicle Information Client
 *
 * @author Peter Winzell
 */
@ClientEndpoint
public class VehicleClient {

    private static Session userSession = null;
    private static MessageHandler messageHandler;
    // private WebSocketContainer container;
    private static ClientManager client;
    private static CountDownLatch latch;

    public VehicleClient() {
        try {

            // container = ContainerProvider.getWebSocketContainer();
            client = ClientManager.createClient();
            
           SslEngineConfigurator sslEngineConfigurator = new SslEngineConfigurator(new SslContextConfigurator());
            sslEngineConfigurator.setHostVerificationEnabled(false);
            client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToServer(URI endpointURI) throws InterruptedException {
        latch = new CountDownLatch(1);
        Thread thread;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    client.connectToServer(VehicleClient.class, endpointURI);
                    try {
                        latch.await();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VehicleClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (DeploymentException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        thread.start();

    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        VehicleClient.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        latch.countDown();
        System.out.println("closing websocket");
        VehicleClient.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a
     * client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (VehicleClient.messageHandler != null) {
            VehicleClient.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        VehicleClient.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        System.out.println(" sending " + message);
        VehicleClient.userSession.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     *
     * @author Peter Winzell
     */
    public interface MessageHandler {

        public void handleMessage(String message);
    }
}
