//
//  ViewController.swift
//  W3CDemo
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


import UIKit
import WMGaugeView



class ViewController: UIViewController, UITextFieldDelegate{
    var gaugeView: WMGaugeView!
    
    @IBOutlet weak var SpeedLabel: UILabel!
    @IBOutlet weak var subscribe: UIButton!
    @IBOutlet weak var VSSServerUrl: UITextField!
    @IBOutlet weak var Label: UILabel!
    @IBOutlet weak var ChangeText: UIButton!
    
    var currentUrl  = "ws://192.168.31.125:8080/W3CSocketish/actions";
    
    @IBAction func TouchedOutside(_ sender: Any) {
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        self.VSSServerUrl.delegate = self;
        VSSServerUrl.text = currentUrl;
        SocketIOManager.sharedInstance.setURL(VSSServerUrl.text!)
        // Do any additional setup after loading the view, typically from a nib.
        NotificationCenter.default.addObserver(self, selector: #selector(self.updateSpeedLabel), name: NSNotification.Name(rawValue: "updateSpeed"), object: nil)
        
        // use a childView to bound the gauge control
        let cgrect = childView.bounds
        gaugeView = WMGaugeView()
        gaugeView.frame = cgrect
        childView.addSubview(gaugeView)
        
        // Gauge view attributes
        gaugeView.style = WMGaugeViewStyle3D()
        gaugeView.maxValue = 240.0;
        gaugeView.showRangeLabels = true
        gaugeView.rangeValues = [50,120,240.0]
        //let color = UIColor(red: 232/255, green: 111/255, blue: 33/255, alpha: 1.0)
        gaugeView.rangeLabelsFontColor = UIColor.black
        gaugeView.rangeColors = [UIColor(red: 27/255,green: 202/255,blue: 33/255,alpha: 1.0), UIColor(red: 255/255,green: 231/255,blue: 0/255,alpha: 1.0),
                                                                 UIColor(red: 231/255,green: 32/255,blue: 43/255,alpha: 1.0)]
            
        gaugeView.rangeLabels = ["ECO","NORMAL","AUTOBAHN"]
        gaugeView.unitOfMeasurement = "km/h"
        gaugeView.showUnitOfMeasurement = true
        gaugeView.scaleDivisionsWidth = 0.008
        gaugeView.scaleSubdivisionsWidth = 0.006
        //gaugeView.rangeLabelsFontColor = UIColor(cgColor: UIColor.black as! CGColor)
        gaugeView.rangeLabelsWidth = 0.04
        gaugeView.rangeLabelsFont = UIFont(name: "Helvetica",size: 0.04)
        
        subscribe.backgroundColor = UIColor.darkGray
        self.view.backgroundColor = UIColor.black
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true;
    }
    
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        currentUrl = textField.text!
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        textField.text = currentUrl
        return true
    }
    
    
    func textFieldShouldClear(_ textField: UITextField) -> Bool {
        return false
    }
    
    @IBOutlet weak var childView: UIView!
    /*@IBAction func setLabel(){
        SocketIOManager.sharedInstance.sendMessage(message:"{\"action\":\"subscribe\",\"type\":\"change\",\"path\":\"Vehicle.speed\"}")
    }Ä*/
    
    
    @IBAction func getURL(){
        // let urlString = VSSServerUrl.text;
        
    }
    
    func updateSpeedLabel() {
        gaugeView.setValue(Float(SocketIOManager.sharedInstance.zespeed),animated: true,duration: 1.6)
        //gaugeView.value = Float()
    }

    @IBAction func subsscribeToSpeed(_ sender: UIButton) {
        SocketIOManager.sharedInstance.sendMessage(message:"{\"action\":\"subscribe\",\"type\":\"change\",\"path\":\"Vehicle.speed\"}")
        
    }
}

