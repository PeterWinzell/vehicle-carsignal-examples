//
//  ViewController.swift
//  W3CDemo
//
//  Created by Peter Winzell on 07/11/16.
//  Copyright © 2016 Melco. All rights reserved.
//

import UIKit
import WMGaugeView


class ViewController: UIViewController {
    var gaugeView: WMGaugeView!

    override func viewDidLoad() {
        super.viewDidLoad()
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
        
        subscribe.backgroundColor = UIColor.black
        
        self.view.backgroundColor = UIColor.black
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
   

    @IBOutlet weak var SpeedLabel: UILabel!

    @IBOutlet weak var subscribe: UIButton!
    @IBOutlet weak var VSSServerUrl: UITextField!
    @IBOutlet weak var Label: UILabel!
    @IBOutlet weak var ChangeText: UIButton!
    
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

