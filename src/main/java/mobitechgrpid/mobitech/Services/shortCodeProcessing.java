/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Make sure the downloaded jar file is in the classpath
// Make sure the downloaded jar file is in the classpath

package mobitechgrpid.mobitech.Services;

/**
 *
 * @author danial
 */
import java.sql.SQLException;
import static mobitechgrpid.mobitech.Controllers.ServerController.printSQLException;
import org.json.*;

public class shortCodeProcessing {
    
    public static void SMSReceiving ()
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
        int lastReceivedId = 0;
                    JSONArray results = null;
            JSONObject result = null;
    
        // Here is a sample of how to fetch all messages using a while loop
        try {

            do {
                results = gateway.fetchMessages(lastReceivedId);
                for(int i = 0; i < results.length(); ++ i) {
                    result = results.getJSONObject(i);
                    System.out.println("From: " + result.getString("from"));
                    System.out.println("To: " + result.getString("to"));
                    System.out.println("Message: " + result.getString("text"));
                    System.out.println("Date: " + result.getString("date"));
                    System.out.println("linkId: " + result.getString("linkId"));
                    lastReceivedId = result.getInt("id");
                    
                    
                }
            } while ( results.length() > 0 );
  
        } catch (Exception e) {
            System.out.println("Caught an Exception: " + e.getMessage());
    }
    // NOTE: Be sure to save lastReceivedId here for next time
    
       shortCodeProcessing scproc = new shortCodeProcessing();
       try{
              scproc.SMSMessageProcessing(result.getString("text"));   //Null pointer Exception is handled inside SMSMessageProcessing()
          }
       catch (Exception e ) {
              System.out.println("Exception trying to process SMS messages: '"+e+"' ");
              //e.printStackTrace();
              
        
    }
    }
    
public void SMSMessageProcessing (String SMSmessage){
       
        String messageType = null;
   
        if (SMSmessage != null){
        for(int i=0;i<SMSmessage.length();i++){
        //for (char character: SMSmessage.toCharArray()){
            //System.out.println(SMSmessage.charAt(i));
          if (SMSmessage.charAt(i) == 'l' && SMSmessage.charAt(i+1) == ':' ) //Normal
            {
                messageType = "Normal";
                System.out.println(messageType);
                System.out.println("Success algo.....");
                //SMSmessage = null;
                break;   //We found what we we looking for :)
            }
            else if (SMSmessage.charAt(i) == 'm' && SMSmessage.charAt(i+1) == ' ')  // Alarm - Either High or Low level Alarm
            {
                messageType = "LevelAlarm";
                System.out.println(messageType);
                System.out.println("Success algo.....");
                //SMSmessage = null;
                break;   //We found what we we looking for :)
            }

            else if (SMSmessage.charAt(i) == 'r' && SMSmessage.charAt(i+1) == ' ')  // Recover - Either Hoigh or Low level Recover
            {
                messageType = "LevelRecoverAlarm";
                System.out.println(messageType);
                System.out.println("Success algo.....");
                //SMSmessage = null;
                break;   //We found what we we looking for :)
            }
            
        }
        }
        else {System.out.println("Failure: SMSmessage is null - SMS Processing Stopped!");};
        
        if (messageType != null){
            String waterLevelString = null;
            String liquidLevelNormalRaw = null;
            String waterLevelAlarmStringRaw = null;
            String waterLevelRecoverStringRaw = null;
            
           switch (messageType)
            {
                case "Normal":
			String[] tokens = SMSmessage.split(":");
			for (String t:tokens){  
                            waterLevelString = tokens[0];
                            liquidLevelNormalRaw = tokens[1];     //
     			}
                        
                    String liquidLevelNormalRawRefined = liquidLevelNormalRaw.substring(0, liquidLevelNormalRaw.indexOf(',')); //Copy string untill we get ','
     	            System.out.println(waterLevelString);   //Useless string 
                    System.out.println(liquidLevelNormalRawRefined);   //Save Water Level Normal in DB
                
                    break;
                case "LevelAlarm": //High or Low Alarm
                    String[] tokensLevelAlarm = SMSmessage.split(" ");
                    	for (String t:tokensLevelAlarm){  
                            waterLevelAlarmStringRaw = tokensLevelAlarm[3];  
                            //liquidLevelNormalRaw = tokensLevelAlarm[1];     //
     			}
                    String waterLevelAlarmStringRefined = waterLevelAlarmStringRaw.substring(0, waterLevelAlarmStringRaw.indexOf(';'));
                    System.out.println(waterLevelAlarmStringRefined);   //Save Water Level Alarm in DB
                    break;
                    
                case "LevelRecoverAlarm": //High or Low Recover
                    String[] tokensLevelRecover = SMSmessage.split(" ");
                    	for (String t:tokensLevelRecover){  
                            waterLevelRecoverStringRaw = tokensLevelRecover[3];  
                            //liquidLevelNormalRaw = tokensLevelAlarm[1];     //
     			}
                    String waterLevelRecovertringRefined = waterLevelRecoverStringRaw.substring(0, waterLevelRecoverStringRaw.indexOf(';'));
                    System.out.println(waterLevelRecovertringRefined);   //Save Water Level Recover in DB
                    
                    break;
                 default:
            
            }
        }else {System.out.println("Failure: messageType is null - Message Processing Stopped!");}
        
    }
    
    
}
