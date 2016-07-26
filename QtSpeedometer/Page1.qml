import QtQuick 2.7
import QtWebSockets 1.0
import QtQuick.Controls 1.4
import QtQuick.Controls.Styles 1.4
import QtQuick.Extras 1.4

Page1Form {

    property string subStr:'{"action":"subscribe","type":"change","path":"Vehicle.speed"}'
    property string unsubStr:'{"action":"unsubscribe","path":"Vehicle.speed"}'

    property var valueindex_start: 0
    property var valueindex_end: 0

    WebSocket {
        id : socket
        url: "ws://localhost:8080/W3CSocketish/actions"


        onTextMessageReceived: {

          // ugly hack, lets parse this JSON proper...sometime soon
          valueindex_start= message.lastIndexOf(":")+1
          valueindex_end = message.lastIndexOf("}")

          speedmeter.value = message.substring(valueindex_start,valueindex_end)

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

    Rectangle{
        color: Qt.rgba(1, 1, 1, 1)
        width: 310
        height: 310
        anchors.centerIn: parent
    CircularGauge {
        id: speedmeter

        minimumValue:0
        maximumValue: 220
        width: 300
        height: 300

        anchors.centerIn: parent

        style: CircularGaugeStyle {

            needle: Rectangle {
                y: outerRadius * 0.15
                implicitWidth: outerRadius * 0.03
                implicitHeight: outerRadius * 0.9
                antialiasing: true
                color: Qt.rgba(0.66, 0.3, 0, 1)
            }
        }
    }
    }
}
