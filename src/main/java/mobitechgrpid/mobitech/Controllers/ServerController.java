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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;
import mobitechgrpid.mobitech.Dao.dataToDb;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mobitechgrpid.mobitech.Services.multipliers;

@RestController
public class ServerController{

    private static final String template = "123456";   //unused
    private final AtomicLong counter = new AtomicLong();  //Atomic Long is best to use in Multithreaded environments 
    private Connection conn;
    
   
        
    multipliers procesing = new multipliers();
    dataToDb databaseOperations = new dataToDb();

    @RequestMapping("/test")            //Endpoint - http://192.168.0.156:8080/test?imei=124578&signalStrength=90&waterLevel=500
    public DeviceDetails devdetails(
                                    @RequestParam(value="signalStrength") double signalStrength,
                                    @RequestParam(value="imei") String imei,
                                    @RequestParam(value="waterLevel") double waterLevel,
                                    @RequestParam(value="deviceType") String deviceType) throws SQLException
    {
                      /* ALGORITHM
                            1. Process data received
                            2. Convert to json file (deviceDetails.json) to enable saving to dynamoDB
                            3. Publishing to HTTP/HTTPs Endpoints for dashboard (Upande guys)
                            4. Find out how long this method takes to execute; should be optimized to facilitate many transactions concurrently
                            5. 
                     */
         //Processing before saving to DB take place here
        // if (waterLevel <= 90)        // Minimum water level
               //send_SMS_to_clustomer();
              procesing.Logs(signalStrength, imei, waterLevel, deviceType);   //signalStrength, imei, waterLevel, deviceType
              conn = databaseOperations.getRemoteConnection();
              databaseOperations.insertDataToDatabase(conn, imei, waterLevel, signalStrength, deviceType);
                                                
            
            
              
              //insertDataToDb.Connection(signalStrength, imei, waterLevel, deviceType);
               
        return new DeviceDetails( counter.incrementAndGet(),       //This is for the display (not configured)
                                  signalStrength,    
                                  imei,              
                                  waterLevel,        
                                  deviceType);       
                           
    }
    
        
}
