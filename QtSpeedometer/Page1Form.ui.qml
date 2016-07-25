import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0

Item {
    property alias button1: button1
    property alias button2: button2

    RowLayout {
        anchors.centerIn: parent

        Button {
            id: button1
            text: qsTr("Subscribe to speed")
            checked: true
        }

        Button {
            id: button2
            text: qsTr("Unsubscribe")
        }
    }
}
