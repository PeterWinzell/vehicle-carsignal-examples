/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitsubishi;

/**
 *
 * @author peterwinzell
 */
public class VehicleSignalVerificationException extends Exception {
    public VehicleSignalVerificationException(String message) {
        super(message);
    }

    public VehicleSignalVerificationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
