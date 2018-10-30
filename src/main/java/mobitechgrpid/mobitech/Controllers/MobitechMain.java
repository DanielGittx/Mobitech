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
import java.time.LocalDateTime;
import java.util.Date;
//import mobitechgrpid.mobitech.Dao.dataToDb;
import mobitechgrpid.mobitech.Services.CallAPIProxy;
//import mobitechgrpid.mobitech.Services.SendEmail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mobitechgrpid.mobitech.Services.shortCodeProcessing;
//import mobitechgrpid.mobitech.Services.dashboardAPIs;


import java.util.List;
import mobitechgrpid.mobitech.Dao.DataAccessObject;


@SpringBootApplication
public class MobitechMain {
    
    
    
    public static void main(String[] args) throws SQLException {
         //Connection conn;
         //SendEmail se = new SendEmail();
        
        SpringApplication.run(MobitechMain.class, args);
   
        System.out.println("Service that refreshes Africas talking SMS FETCHING API (every 30 minutes) ");
        CallAPIProxy callapi = new CallAPIProxy(30000);    ///30 seconds
        System.out.println("Service started...");
  
    }
        
        
        
      /*  
           String from = "danmburu.gitau@gmail.com";
           String to= "dangittxdev@gmail.com";
           String subject= "ping";
           String msg= "Testing email sending";
        
           
           try {
                       ///(String from, String to, String subject, String msg)
                      //se.sendMail(from, to, subject, msg);
           }catch (NullPointerException np){
             System.out.println("EXCEPTION -> " +np);
           }
           */
    }
       

