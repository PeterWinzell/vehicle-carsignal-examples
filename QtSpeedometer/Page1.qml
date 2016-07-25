import QtQuick 2.7
import QtWebSockets 1.0

Page1Form {

    property string subStr:'{"action":"subscribe","type":"change","path":"Vehicle.speed"}'
    property string unsubStr:'{"action":"unsubscribe","path":"Vehicle.speed"}'

    WebSocket {
        id : socket
        url: "ws://localhost:8080/W3CSocketish/actions"


        onTextMessageReceived: {
          console.log(message);
        }

        onStatusChanged: if (socket.status == WebSocket.Error) {
                             console.log("Error: " + socket.errorString)
                         } else if (socket.status == WebSocket.Open) {
                             socket.sendTextMessage(subStr);
                         } else if (socket.status == WebSocket.Closed) {
                             console.log("\nSocket closed");
                         }
        active: false
    }

    button1.onClicked: {
        console.log("Button 1 clicked.")
        socket.active = true
    }

    button2.onClicked: {
        socket.sendTextMessage(unsubStr)
        socket.active = false
        console.log("Button 2 clicked.")
    }
}
