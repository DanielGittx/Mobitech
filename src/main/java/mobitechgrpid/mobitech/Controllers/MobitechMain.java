/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author danial
 */
package mobitechgrpid.mobitech.Controllers;


import java.sql.Connection;
import java.sql.SQLException;
import mobitechgrpid.mobitech.Dao.dataToDb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mobitechgrpid.mobitech.Services.shortCodeProcessing;
//import mobitechgrpid.mobitech.Services.dashboardAPIs;

@SpringBootApplication
public class MobitechMain {
    
    public static void main(String[] args) throws SQLException {
         Connection conn;
        
        SpringApplication.run(MobitechMain.class, args);
        
        //shortCodeProcessing scp = new shortCodeProcessing();
        //dashboardAPIs dashApi = new dashboardAPIs();
         //dataToDb databaseOperations = new dataToDb();
        
        
        //conn = databaseOperations.getRemoteConnection();
        //dashApi.viewTable(conn,0);
        
        //scp.SMSReceiving();      //Code running ok; needs testing facilitated by afrocastalking guys
        //System.out.println(scp.sum(10, 20));             //Testing ok :)
     
    }
}
