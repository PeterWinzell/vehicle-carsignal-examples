//
//  SocketIOManager.swift
//  W3CDemo
//
//  Created by Peter Winzell on 09/11/16.
//  Copyright © 2016 Melco. All rights reserved.
//

import Foundation
import Starscream
import SwiftyJSON


class SocketIOManager: WebSocketDelegate{
    static let sharedInstance = SocketIOManager()
    
    var socket: WebSocket!
    // global access through singleton...darn ugly. Must be a better way of doing this.
    var zespeed: Int!
    
    init() {
        
    }
    
    func setURL(_ urlString: String){
        print(urlString)
        socket = WebSocket(url: URL(string: urlString)!)
        socket.delegate = self
        socket.connect()
        
    }
    
    // MARK: Websocket Delegate Methods.
    
    func websocketDidConnect(socket: WebSocket) {
        print("websocket is connected")
    }
    
    func websocketDidDisconnect(socket: WebSocket, error: NSError?) {
        if let e = error {
            print("websocket is disconnected: \(e.localizedDescription)")
        } else {
            print("websocket disconnected")
        }
    }
    
    
   
    
    // Post notification
     func websocketDidReceiveMessage(socket: WebSocket, text: String) {
        let json = text;
        let data = json.data(using: String.Encoding.utf8)
        let jsonarray = JSON(data: data!)
        let path = jsonarray["path"].string
        print(text)
        // check if path is correct
        if (path == "Vehicle.speed"){
            zespeed = jsonarray["value"].int!
            if zespeed != nil{
                // notify UI through notification center (GOTO ViewController)
                print(zespeed)
                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateSpeed"), object: nil)
            }
        }
    }
    
    func websocketDidReceiveData(socket: WebSocket, data: Data) {
        print("Received data: \(data.count)")
    }
    
    func sendMessage(message: String) {
        socket.write(string: message)
    }
    
    
}
