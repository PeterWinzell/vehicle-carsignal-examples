import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0

Item {
    id: item1
    property alias button1: button1
    property alias button2: button2

    RowLayout {
        anchors.verticalCenterOffset: 189
        anchors.horizontalCenterOffset: 1
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 31
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
