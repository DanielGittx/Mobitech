/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobitechgrpid.mobitech.Services;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.csvreader.CsvWriter;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author danial
 */
public class multipliers extends IOException{
    
    long recordsTracker =0;         //Transactions tracker - resets only when server is restarted
    public void Logs (double signalStrength, String imei, double waterLevel, String deviceType)      //Csv, Json Logs
    {
                String outputFile = "H:/MOBITECH/MobitechServerLogs/devicedetails.csv";
		
                  ///////////////csv//////////////
                
		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
                                csvOutput.write("deviceType");
				csvOutput.write("Transactions");
				csvOutput.write("signalStrength");
                                csvOutput.write("imei");
                                csvOutput.write("waterLevel");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			
			// write out a few records
                        csvOutput.write(deviceType);                                           
                        csvOutput.write(String.valueOf(recordsTracker));       //Keep Logging every Request: Reset only when server is stopped
			csvOutput.write(Double.toString(signalStrength));
                        csvOutput.write(imei);
			csvOutput.write(Double.toString(waterLevel));
			csvOutput.endRecord();
		
			csvOutput.close();
                        
		} catch (IOException e) {
			e.printStackTrace();
		}
                
       ///////////////jsonify//////////////
        JSONObject obj = new JSONObject();
        obj.put("deviceType", deviceType);
        obj.put("Transactions", recordsTracker);        //Could have timestamp substitute this
        obj.put("imei", imei);
        obj.put("waterLevel", waterLevel);
        obj.put("signalStrength", signalStrength);

//        JSONArray list = new JSONArray();
//        list.add("waterLevel");       //Variables
//        list.add("imei");
//        list.add("signalStrength");
//        obj.put("MobitechHardwareRecords", list);
        
        
        String outputFileJson = "H:/MOBITECH/MobitechServerLogs/devicedetails.json";
        try (FileWriter file = new FileWriter(outputFileJson)) {
            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Save in Database
        
        recordsTracker++;
        System.out.print(obj);
    
}
    
}
