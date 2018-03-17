/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobitechgrpid.mobitech.Controllers;

/**
 *
 * @author danial
 */
public class DeviceDetailsToDashboard {
    private final double waterLevel;
    private final String imei;
    private final double signalStrength;;
    private final String deviceType;  //Hardware Version
    //private final String serialNumber;
    
    //Constructor
    public DeviceDetailsToDashboard(double signalStrength, String imei, double waterLevel, String deviceType) {
        
        this.signalStrength = signalStrength;
        this.imei = imei;
        this.waterLevel = waterLevel;
        this.deviceType = deviceType;
    }

    
    public double getsignalStrength() {
        return signalStrength;
    }
    public String getimei() {
        return imei;
    }
    public double getwaterLevel() {
        return waterLevel;
    }
    public String getdeviceType() {
        return deviceType;
    }
    
    
}
