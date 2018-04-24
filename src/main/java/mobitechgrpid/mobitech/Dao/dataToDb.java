/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobitechgrpid.mobitech.Dao;

/**
 *
 * @author danial
 */
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
/*

public class dataToDb {
    
    final static Logger logger = Logger.getLogger(dataToDb.class); // https://www.mkyong.com/logging/log4j-hello-world-example/
    
    public static Connection getRemoteConnection() {
    System.out.println ("Inside getRemoteConnection()............");
    
    //if (System.getenv("")!= null)  {
       // System.out.println ("We found the end point............");
      try {
        System.out.println ("Now Hitting the database............");
      Class.forName("com.mysql.jdbc.Driver");
      
      //String password = "";     //MOBIWATERLOCAL DB
      //String jdbcUrl = "jdbc:mysql://localhost:3306/mobiwaterlocal?user=root&password="+password;          //local
              
       String jdbcUrl = "jdbc:mysql://localhost:3306/mobiwaterDB?user=root&password=mobimobi1234*#";      //production
                
     
      logger.trace("Getting remote connection with connection string from environment variables.");
      System.out.println ("Getting remote connection with connection string from environment variables.............");
      Connection con = DriverManager.getConnection(jdbcUrl);
      logger.info("Remote connection successful.");
      System.out.println ("Remote connection successful............");
      
      
      return con;
    }
    catch (ClassNotFoundException e) { logger.warn(e.toString()); System.out.println (e.toString()); return null;}
    catch (SQLException e) { logger.warn(e.toString()); System.out.println (e.toString()); return null;}
   }
public void insertDataToDatabase (Connection conn, String tankID, double waterLevel, double signalStrength, String HW_version, String errorCode, String dateGeneratedOnDevice) throws SQLException
   {
            //java.sql.Timestamp date = new java.sql.Timestamp (javaDate.getTime());
            //imei, waterLevel, signalStrength, deviceType
       
            String sql = "INSERT INTO DeviceData (tankID,waterLevel,signalStrength,HW_version, errorCode, dateGeneratedOnDevice) VALUES(?,?,?,?,?,?)";
 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,tankID);
            pstmt.setDouble(2,waterLevel);
            pstmt.setDouble(3,signalStrength);
            pstmt.setString(4,HW_version);
            pstmt.setString(5,errorCode);
            pstmt.setString(6,dateGeneratedOnDevice);
            pstmt.executeUpdate();
   }

//The entire SMS message 
public void insertSMSDeviceDataintoDatabase (Connection conn, String shortCode, String message, String linkId, String dateGeneratedOnDevice, String devicePhoneNumber ) throws SQLException
   {
            //java.sql.Timestamp date = new java.sql.Timestamp (javaDate.getTime());
            //imei, waterLevel, signalStrength, deviceType
                    
            String sql = "INSERT INTO DeviceDataSMS (shortCode,message,linkId,dateGeneratedOnDevice,devicePhoneNumber) VALUES(?,?,?,?,?)";
 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,shortCode);
            pstmt.setString(2,message);
            pstmt.setString(3,linkId);
            pstmt.setString(4,dateGeneratedOnDevice);
            pstmt.setString(5,devicePhoneNumber);
            pstmt.executeUpdate();
   }
// Pieces of SMS data 
public void insertSMSProcessedDataToDatabase (Connection conn,double waterLevel, String tankID) throws SQLException
   {
            //java.sql.Timestamp date = new java.sql.Timestamp (javaDate.getTime());
            //imei, waterLevel, signalStrength, deviceType
       
            String sql = "INSERT INTO DeviceData (waterLevel, tankID) VALUES(?,?)";
 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1,waterLevel);
            pstmt.setString(2,tankID);
            pstmt.executeUpdate();
   }
  
}

*/