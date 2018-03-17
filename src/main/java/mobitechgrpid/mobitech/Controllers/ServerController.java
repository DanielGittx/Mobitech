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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import mobitechgrpid.mobitech.Dao.dataToDb;
import mobitechgrpid.mobitech.Services.AfricasTalkingGateway;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mobitechgrpid.mobitech.Services.multipliers;

import org.json.JSONArray;
import org.json.JSONObject;

@RestController
public class ServerController{

    private static final String template = "123456";   //unused
    private final AtomicLong counter = new AtomicLong();  //Atomic Long is best to use in Multithreaded environments 

    multipliers procesing = new multipliers();
    dataToDb databaseOperations = new dataToDb();
    
    public Connection conn = databaseOperations.getRemoteConnection();

    @RequestMapping("/test")            //Endpoint - :8080/test?imei=124578&signalStrength=90&waterLevel=500
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
              //procesing.Logs(signalStrength, imei, waterLevel, deviceType);   //signalStrength, imei, waterLevel, deviceType
              conn = databaseOperations.getRemoteConnection();
              databaseOperations.insertDataToDatabase(conn, imei, waterLevel, signalStrength, deviceType);
                                                
            

               
        return new DeviceDetails( counter.incrementAndGet(),       //This is for the display (not configured)
                                  signalStrength,    
                                  imei,              
                                  waterLevel,        
                                  deviceType);    
                              
    }
    
    
    @RequestMapping("/sms")            //Endpoint - 
    public void devdetails()
    {       
                // Specify your login credentials
        String username = "sandbox";
        String apiKey   = "0bb0fa6cd0a312440049c71f983b5fdf218af684decc1d63136842f9c0b53f79";
        // Create a new instance of our awesome gateway class
        AfricasTalkingGateway gateway  = new AfricasTalkingGateway(username, apiKey, "sandbox");
        /*************************************************************************************
            NOTE: If connecting to the sandbox:
            1. Use "sandbox" as the username
            2. Use the apiKey generated from your sandbox application
                https://account.africastalking.com/apps/sandbox/settings/key
            3. Add the "sandbox" flag to the constructor
            AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey, "sandbox");
        **************************************************************************************/
        // Our gateway will return 10 messages at a time back to you, starting with
        // what you currently believe is the lastReceivedId. Specify 0 for the first
        // time you access the gateway, and the ID of the last message we sent you
        // on subsequent results
        System.out.println("We've just hit AFRICASTALKING API.......");
        
        int lastReceivedId = 0;
    
        // Here is a sample of how to fetch all messages using a while loop
        try {
            JSONArray results = null;
            do {
                results = gateway.fetchMessages(lastReceivedId);
                for(int i = 0; i < results.length(); ++ i) {
                    JSONObject result = results.getJSONObject(i);
                    System.out.println("From: " + result.getString("from"));
                    System.out.println("To: " + result.getString("to"));
                    System.out.println("Message: " + result.getString("text"));
                    System.out.println("Date: " + result.getString("date"));
                    System.out.println("linkId: " + result.getString("linkId"));
                    lastReceivedId = result.getInt("id");
                    
//insertSMSDeviceDataintoDatabase (Connection conn, String shortCode, String message, String linkId, String dateGeneratedOnDevice, String devicePhoneNumber )
                    if (lastReceivedId == 10) {
                    databaseOperations.insertSMSDeviceDataintoDatabase(conn, 
                           result.getString("to"), result.getString("text"), 
                           result.getString("linkId"), 
                           result.getString("date"), 
                           result.getString("from"));
                 
                   System.out.print("Successfully loaded SMS into database.........!");
                    }
                   
                  
                }
            } while ( results.length() > 0 );
        } catch (Exception e) {
            System.out.println("Caught an Exception: " + e.getMessage());
    }
    // NOTE: Be sure to save lastReceivedId here for next time
    }
 
            // waterLevel Status  
            //  Realtime/most recent record  - 0
            //  weekly    - 1
            //  monthly   - 2
            //  yearly    - 3
            //  Location          - Name of tank, longitude&latitude
    
        @RequestMapping("/dashboard")            //Endpoint - 
        public DeviceDetailsToDashboard viewTable( 
            @RequestParam(value="waterLevelStatus") int waterLevelStatus, 
            @RequestParam(value="tankID") int tankID) throws SQLException {
            
            
           Statement stmt = null;
           String query = null;
           JSONObject tablecontents = new JSONObject();   //unused
           String Imei = null;
           double waterlevel = 0;
           double signalquality = 0;
           String devicetype = null;
           String latitude = null;
           String Longitude = null;
    
    
        switch (waterLevelStatus) {
            case 0: //Most Recent record
                query = "SELECT * FROM mobiwaterDB.DeviceData  WHERE tankId = '"+tankID+"' ORDER BY recordid DESC LIMIT 1" ;     // Realtime - Most recent record 
                break;
            case 1: //Last 1 Week
                query = "select * from mobiwaterDB.DeviceData where  `dataSavedOnDB` >= DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND tankId = '"+tankID+"' ";
                break;
            case 2:  //Last 1 Month
                query = "select * from mobiwaterDB.DeviceData where  `dataSavedOnDB` >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AND tankId = '"+tankID+"' ";
                break;
            case 3:  //Last 1 Year
                query = "select * from mobiwaterDB.DeviceData where  `dataSavedOnDB` >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) AND tankId = '"+tankID+"' ";
                break;
            default:
                query = "SELECT * FROM mobiwaterDB.DeviceData  WHERE tankId = '"+tankID+"' ORDER BY recordid DESC LIMIT 1" ;
                
                break;
        }

    try {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
             Imei = rs.getString("imei");
             waterlevel = rs.getInt("waterLevel");
             signalquality = rs.getFloat("signalStrength");
             devicetype = rs.getString("deviceType");
             
                  System.out.print("Returning JSON 1");
                  return new DeviceDetailsToDashboard( signalquality,  Imei,  waterlevel,  devicetype); 
         
        }
    } catch (SQLException e ) {
              printSQLException(e);
        
    } finally {
        if (stmt != null) { stmt.close(); }
    }
                 // We wont get here if everything went okay!
                  return new DeviceDetailsToDashboard( signalquality,  Imei,  waterlevel,  devicetype);                     
    
}
    public static void printSQLException(SQLException ex) {
    for (Throwable e : ex) {
      if (e instanceof SQLException) {
        if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
          e.printStackTrace(System.err);
          System.err.println("SQLState: " + ((SQLException)e).getSQLState());
          System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
          System.err.println("Message: " + e.getMessage());
          Throwable t = ex.getCause();
          while (t != null) {
            System.out.println("Cause: " + t);
            t = t.getCause();
          }
        }
      }
    }
  }
    
    public static boolean ignoreSQLException(String sqlState) {
    if (sqlState == null) {
      System.out.println("The SQL state is not defined!");
      return false;
    }
    // X0Y32: Jar file already exists in schema
    if (sqlState.equalsIgnoreCase("X0Y32"))
      return true;
    // 42Y55: Table already exists in schema
    if (sqlState.equalsIgnoreCase("42Y55"))
      return true;
    return false;
  }
        
     
}
