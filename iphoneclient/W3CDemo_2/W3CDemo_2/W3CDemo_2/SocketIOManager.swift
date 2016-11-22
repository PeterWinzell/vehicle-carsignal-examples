//
//  SocketIOManager.swift
//  W3CDemo
//
//
//  The MIT License (MIT)
//  Copyright (c) <09/11/16> <Peter Winzell>
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
//  associated documentation files (the "Software"), to deal in the Software without restriction, including
//  without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
//  following conditions:
//
//  The above copyright notice and this permission notice shall be included in all copies or substantial
//  portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
//  LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
//  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
//  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
//  THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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
