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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;





public class dataToDb {
    
    final static Logger logger = Logger.getLogger(dataToDb.class); // https://www.mkyong.com/logging/log4j-hello-world-example/
    
public static Connection getRemoteConnection() {
    System.out.println ("Inside getRemoteConnection()............");
    
    //if (System.getenv("")!= null)  {
        System.out.println ("We found the end point............");
      try {
        System.out.println ("Now Hitting the database............");
      Class.forName("com.mysql.jdbc.Driver");
      String dbName = System.getProperty("");  /// Name of Database
      String userName = System.getProperty("");      // Master User Name
      String password = System.getProperty(""); // Master Password
      String hostname = System.getProperty("");  //Endpoint
      //String port = System.getProperty(3306);
      String jdbcUrl = "";
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
public void insertDataToDatabase (Connection conn, String imei, double waterLevel, double signalStrength, String deviceType) throws SQLException
   {
            //java.sql.Timestamp date = new java.sql.Timestamp (javaDate.getTime());
            //imei, waterLevel, signalStrength, deviceType
       
            String sql = "INSERT INTO DeviceData (imei,waterLevel,signalStrength, deviceType) VALUES(?,?,?,?)";
 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,imei);
            pstmt.setDouble(2,waterLevel);
            pstmt.setDouble(3,signalStrength);
            pstmt.setString(4, deviceType);
//            pstmt.setTimestamp(5,javaDate.getTime() );
              pstmt.executeUpdate();
   }
    
}
