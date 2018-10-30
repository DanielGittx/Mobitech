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
import java.sql.Connection;
import java.sql.SQLException;
import static mobitechgrpid.mobitech.Controllers.ServerController.printSQLException;
import mobitechgrpid.mobitech.Dao.DataAccessObject;
//import mobitechgrpid.mobitech.Dao.dataToDb;
import org.json.*;

public class shortCodeProcessing implements DeviceMessageConstants{
   
public double SMSMessageProcessing (String SMSmessage, String tankID){             //Return liquid level: dont care normal, alarm , recover

        String messageType = null;
        String SMSmessageReadyForParsing = null;
        String SMSmessageIdentifier = "Mobiwater Level";
        String SMSmessage_substring;
        String []tempbuff;
        
        String level = null;
        String date = null;
        String time=null;
               
        SMSmessage_substring = SMSmessage.substring(0, 15);
        
    try{
        
       if (SMSmessage_substring.equals(SMSmessageIdentifier)) {

           
       if ( SMSmessage.length() > 100 && SMSmessage.length() < 110)              //Sms message from DGTS device
        {
            double []currentTankCapacity = {0.00,0.00};
            String[] SMSmessageWithoutKeyword = SMSmessage.split(" ");
	    String[] tokens = SMSmessageWithoutKeyword[1].split(":");
            if (tokens[1].equals("ID")) { 
                  double water_level = Double.parseDouble(tokens[11]);  
                  double signal_strength = Double.parseDouble(tokens[9]);
                  if (water_level >= 0) { //+VE Levels
                                           
                      //currentTankCapacity = DataAccessObject.TankVolume(tankID, water_level);      //Get current tank capacity (Cubic Metres)
                      //currentTankCapacity *= 1000; // Cubic metres to Litres
                      currentTankCapacity = DataAccessObject.calculateCapacity(tankID, water_level);
                      
                      DataAccessObject.adddevicedata(currentTankCapacity[0],  "dateGeneratedOnDevice", "errorCode", "hwVersion",signal_strength, "tankID", currentTankCapacity[1], "DateSavedOnDbNOW", 11111111, 0xffff);      //Todo SMS
                      return -1;
                  }
           }
           
        }    
            //  Mobiwater WaterLevel:0.13,Normal
            //  Mobiwater Level Low Alarm 0.0000.06;2018-03-26;09:31;     - Alarm
            //  Mobiwater Level High Recover 0.0010;2018-03-12;15:05;     - Recover
        String[] SMSmessageWithoutKeyword = SMSmessage.split(" ");                 // Pick park of string without keyword
                 if (SMSmessageWithoutKeyword.length < 3){
     
             // for (String t:SMSmessageWithoutKeyword) 
                    SMSmessageReadyForParsing = SMSmessageWithoutKeyword[1]; 
            
                 for(int i=0;i<SMSmessageReadyForParsing.length();i++){
                    if (SMSmessageReadyForParsing.charAt(i) == 'l' && SMSmessageReadyForParsing.charAt(i+1) == ':' ) //Normal
                     {
                      messageType = "Normal";
                      //System.out.println(messageType);
                      break;   //We found what we we looking for :)
                     }
                   }
            
                } 
                        //  Mobiwater Level Low Alarm 0.0000.06;2018-03-26;09:31;     - Alarm
                        //  Mobiwater Level High Recover 0.0010;2018-03-12;15:05;     - Recover
                 else
                {
     
                // for (String t:SMSmessageWithoutKeyword) {                             //Mobiwater Level Low Alarm 0.0000.06;2018-03-26;09:31;
                         SMSmessageReadyForParsing = SMSmessageWithoutKeyword[4];        //[3] - Alarm, //[4] - 0.0000.06;2018-03-26;09:31;
                         messageType = "AlarmOrRecover";                               
                // }
   
                }
  
        if (messageType != null){          
            String waterLevelString = null;
            String liquidLevelNormalRaw = null;
            //DataAccessObject dao = new DataAccessObject();
            
           switch (messageType)
            {
                case "Normal":
			String[] tokens = SMSmessageReadyForParsing.split(":");
			//for (String t:tokens){  
                            waterLevelString = tokens[0];
                            liquidLevelNormalRaw = tokens[1];     //
     			//}
                        
                    String liquidLevelNormalRawRefined = liquidLevelNormalRaw.substring(0, liquidLevelNormalRaw.indexOf(',')); //Copy string untill we get ','
     	            System.out.println("waterLevelStringNormal: "+liquidLevelNormalRawRefined);   //Useless string 
                    //System.out.println(liquidLevelNormalRawRefined);   //Save Water Level Normal in DB
                    try{
                    double liquidLevelNormalRawRefined_double = Double.parseDouble(liquidLevelNormalRawRefined);
                    //toDB.insertSMSProcessedDataToDatabase(conn,liquidLevelNormalRawRefined_double);
                    return liquidLevelNormalRawRefined_double;
                    }
                    catch (NumberFormatException ex ){
                        System.out.println( ex);
                    }
                     
                    break;
                        // 0.0000.06;2018-03-26;09:31 Alarm
                        // 0.0010;2018-03-12;15:05;   Recover
                case "AlarmOrRecover": //High or Low Recover
                    String[] alarmOrRecoverParse = SMSmessageReadyForParsing.split(";");  //Mobiwater Level Low Alarm 0.0000.06;2018-03-26;09:31;
                    // for (String x : alarmOrRecoverParse) {
                         level = alarmOrRecoverParse[0];
                         //date  = alarmOrRecoverParse[1];   // Messages missing in DB issue (from kelvin) - 
                                                             // "Recover" - Only has water level 
                         //time  = alarmOrRecoverParse[2];   // "Alarm"   - Only has water level and date 
                                                             // The above being the case, the algorithm flops as it expects 3 tokens within alarmOrRecoverParse array
                                                             // Fix - Just parse for whats necessary (level)and ignore the rest to avoid arrayOutOfBounds exception
                   //  }
                     
                        //System.out.println("level:"+level+"");
                        //System.out.println("date:"+date+"");
                        //System.out.println("time:"+time+"");
                    
                    try{
                    double waterLevelRecovertringRefined_double = Double.parseDouble(level);
                    //toDB.insertSMSProcessedDataToDatabase(conn,waterLevelRecovertringRefined_double);
                    return waterLevelRecovertringRefined_double;
                    }
                    catch (NumberFormatException ex ){
                        System.out.print( ex);
                    }
                    
                    break;
                 default:
            
            }
          }else {return -1 ;}  

        } else {return -2 ;}
 
 
        }catch (NullPointerException | ArrayIndexOutOfBoundsException np)
        {
            System.out.println("Null pointer or Index out of bounds Exception -> "+np);
            System.out.println("SMSmessage -> "+SMSmessage);
            System.out.println("messageType -> "+messageType);
            System.out.println("SMSmessageReadyForParsing -> "+SMSmessageReadyForParsing);
        }
 
       return -1;
    }
}


