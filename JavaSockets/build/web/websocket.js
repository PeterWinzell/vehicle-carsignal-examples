/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/* global data, doorparapraph */

window.onload = init;

var socket = new WebSocket("ws://localhost:8080/W3CSocketish/actions");

socket.onmessage = onMessage;
socket.onopen = onOpen;

var Vehicle = Vehicle || {};
var subHandlers = new Array();
var getHandlers = new Array();

var doorPath = "Vehicle.door";
var speedPath = "Vehicle.speed";


Vehicle.vehicleSpeed = {
            subscribe : function(callback) {
                subHandlers.push(function(obj) {
                    if (obj.path === speedPath){ // hate this check
                        callback(obj);
                    }    
              });
          }
};

Vehicle.vehicleDoor = {
    subscribe : function(callback) {
                subHandlers.push(function(obj) {
                    if (obj.path.indexOf(doorPath) !== -1)
                        callback(obj);
              });
          }
};



function onOpen(event){
     alert("Socket is opened...");
}

function onMessage(event) {
    var dataobj = JSON.parse(event.data);
    subHandlers.forEach(function(handler) {
      handler(dataobj);
    });
}



function showForm() {
    document.getElementById("addDeviceForm").style.display = '';
}

function hideForm() {
    document.getElementById("addDeviceForm").style.display = "none";
}


function subscribetoSpeed(){
    //var id = element;
    Vehicle.vehicleSpeed.subscribe(printSpeed);
    
    var DeviceAction = {
        action: "subscribe",
        type: "change",
        path: "Vehicle.speed"
    };
    socket.send(JSON.stringify(DeviceAction));
}

function stopsubscribetoSpeed(){
    var DeviceAction = {
        action: "unsubscribe",
        path:"Vehicle.speed"
    };
    socket.send(JSON.stringify(DeviceAction));
}


function subscribetoDoor(){
    Vehicle.vehicleDoor.subscribe(printDoor);
    // Listen to all door signal changes...
    var Action={
        action: "subscribe",
        type: "change",
        path: "Vehicle.door"
    }
    socket.send(JSON.stringify(Action));
}

function stopsubscribetoDoor(){
    var Action = {
        action: "unsubscribe",
        path:"Vehicle.door"
    };
    socket.send(JSON.stringify(Action));
}

function printSpeed(device){
   var speedparagraph = document.getElementById("Vehicle.speed");
   speedparagraph.innerHTML = device.value;
}

function printDoor(values){
  // console.log(values.path);
  var doorparagraph = document.getElementById(values.path);
  if (doorparagraph !== null)
    doorparagraph.innerHTML = values.value;
}

function init() {
    // hideForm();
}