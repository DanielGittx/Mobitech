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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mobitechgrpid.mobitech.Controllers.ServerController.printSQLException;
import mobitechgrpid.mobitech.Dao.DataAccessObject;
//import mobitechgrpid.mobitech.Dao.dataToDb;
import mobitechgrpid.mobitech.Services.AfricasTalkingGateway;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mobitechgrpid.mobitech.Services.multipliers;
import mobitechgrpid.mobitech.Services.shortCodeProcessing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RestController
public class ServerController{
    multipliers procesing = new multipliers();
    //dataToDb databaseOperations = new dataToDb();
    //public Connection conn = databaseOperations.getRemoteConnection();
    

    @RequestMapping("/test")            //Endpoint for BrckDevice
   // public DeviceDetails devdetails(
             public String devdetails(
                                    @RequestParam(value="SS") double signalStrength,
                                    @RequestParam(value="tankID") String tankID,
                                    @RequestParam(value="WL") double waterLevel,
                                    @RequestParam(value="ER") String errorCode,
                                    @RequestParam(value="DG") String dateGeneratedOnDevice,      //TODO - convert to DATE
                                    @RequestParam(value="HW") String HW_version) throws SQLException
    {
                      /* ALGORITHM
                            1. Process data received
                            2. Convert to json file (deviceDetails.json) to enable saving to dynamoDB
                            3. Publishing to HTTP/HTTPs Endpoints for dashboard (Upande guys)
                            4. Find out how long this method takes to execute; should be optimized to facilitate many transactions concurrently
                      */
                                        
            // Store records in the database --WE MUST HAVE CAPACITY (Register Device into DB) TO GET RECORDS IN DB
        double currentTankCapacity = DataAccessObject.TankVolume(tankID, waterLevel);      //Get current tank capacity (Cubic Metres)
        currentTankCapacity *= 1000; // Cubic metres to Litres
        
                
       if (DataAccessObject.adddevicedata(currentTankCapacity, dateGeneratedOnDevice, errorCode,HW_version, signalStrength,  tankID,waterLevel, "BrckDevice"))   /// save DATE NOW for BrckDevice
                                          
        {
            return "Sucess : Device data added";
        }
        else{
            return "Error : Device data not added";
        }

    }
    
    
    @RequestMapping("/sms")            //Endpoint - 
    public String devdetails()
    { 
        
        ServerController ServController = new ServerController();
                // Specify your login credentials
        String username = "";
        String apiKey   = "";         //For production short code key
       
        // Create a new instance of our awesome gateway class
        AfricasTalkingGateway gateway  = new AfricasTalkingGateway(username, apiKey);
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
        long lastReceivedId = 0;
            if ( DataAccessObject.LastReceivedIdValue().get(0) != null) 
                   lastReceivedId = (Long)DataAccessObject.LastReceivedIdValue().get(0) ;  
                    System.out.println("retrieved lastReceivedId: " + lastReceivedId);
   
            JSONArray results = null;
            JSONObject result = null;
        // Here is a sample of how to fetch all messages using a while loop
        try {

            do {
                results = gateway.fetchMessages(lastReceivedId);    // lastReceivedId retrieved from Database
                for(int i = 0; i < results.length(); ++ i) {
                    result = results.getJSONObject(i);
                    
                    //System.out.println("From: " + result.getString("from"));
                    //System.out.println("To: " + result.getString("to"));
                    System.out.println("Message: " + result.getString("text"));
                    System.out.println("Print Date: " + result.getString("date"));
                    //System.out.println("linkId: " + result.getString("linkId"));
                    lastReceivedId = result.getLong("id");          // Most recent lastReceivedId to be saved in Database
                    //System.out.println("saved lastReceivedId: " + lastReceivedId);
                    
                    try {
                         ServController.processing(result, lastReceivedId );               //Save lastReceivedId
                    }catch (Exception ex)
                    {
                       // System.out.println("Processing function exception -> " +ex );
                    }
      
                }
                
            } 
            
            while ( results.length() > 0 );
        } catch (Exception e) {
            System.out.println("Caught an Exception when tring to fetch SMS from Africastalking: " + e.getMessage());
        }
            
    // NOTE: Be sure to save lastReceivedId here for next time
       return "Processed sms stored in DBase";
    }
    
 public void processing (JSONObject result, long LastReceivedValueToAfricasTalking )  {
    shortCodeProcessing sCodeProc = new shortCodeProcessing();
    if  (result != null)
        
     try {
          
	   if (sCodeProc.SMSMessageProcessing(result.getString("text")) >= 0 );    // water level to store in dB
		{   
		   double level = sCodeProc.SMSMessageProcessing(result.getString("text"));       //TODO:- Cleaner way of doing this!
		   String tankID = result.getString("from");
		   tankID = tankID.substring(1,13);         //+254792714708 ignore the + for easy query of tankID on Database
		   //databaseOperations.insertSMSProcessedDataToDatabase(conn, level, tankID);  //double level, string devicephonenumber
		   //Date dateSavedOnDb = new Date();            //Now
		   if (level < 0 || (sCodeProc.SMSMessageProcessing(result.getString("text")) < 0 ) )  {        //no negtives in DB                    
			   //System.out.println("No negatives in DB");  
                           //Just chill
		   }else{
                            // Store records (level etc) in the database
                            double currentTankCapacity = DataAccessObject.TankVolume(tankID, level);      //Get current tank capacity (Cubic Metres)
                            currentTankCapacity *= 1000; // Cubic metres to Litres
                            DataAccessObject.addSMSProcessedData(level, tankID,currentTankCapacity,result.getString("date") );   // save level, capacity
                            DataAccessObject.save_LastReceivedId(LastReceivedValueToAfricasTalking,result.getString("date") );   // save updated lastReceivedId on LastReceived Table
            
                   }

		}
			  
		}catch ( JSONException ex)
		{
			  // return "SQL or JSON Exception -fetch- "+ex;
                        System.out.println("SQL or JSON Exception -fetch- "+ex );
		}
    }
 
  public Date StringToDateConverter (String dateSavedOnDb)
    {
        if (dateSavedOnDb.equals("BrckDevice")){
           Date parsed_date_embedded = new Date();            //Now
           return parsed_date_embedded;                       //Brck Device
        }

        Instant instant = Instant.parse( dateSavedOnDb );    // `Instant` is always in UTC
        Date parsed_date = Date.from( instant );             // Pass an `Instant` to the `from` method.      
        System.out.println("Parsed Date -> " +parsed_date); 
        return parsed_date; 
 
     }
      
   public static void printSQLException(SQLException ex) {
    for (Throwable e : ex) {
      if (e instanceof SQLException) {
        if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
          e.printStackTrace(System.err);
//          System.err.println("SQLState: " + ((SQLException)e).getSQLState());
//          System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
//          System.err.println("Message: " + e.getMessage());
          Throwable t = ex.getCause();
          while (t != null) {
           // System.out.println("Cause: " + t);
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
    
    
     

        // Sets custom port number on runtime
        // This is to move away from default 8080 set by embedded Tomcat

   @Configuration
   public class ServletConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> {
            container.setPort(8080);        
        });
    }
 }
       
}
