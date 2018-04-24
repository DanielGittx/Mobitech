/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobitechgrpid.mobitech.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpSession;

import static mobitechgrpid.mobitech.Controllers.ServerController.printSQLException;
//import mobitechgrpid.mobitech.Dao.dataToDb;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import mobitechgrpid.mobitech.Controllers.Devicedetails;
import mobitechgrpid.mobitech.Dao.DataAccessObject;
import org.hibernate.Session;
import mobitechgrpid.mobitech.Services.NewHibernateUtil;
import org.hibernate.Transaction;
//import org.hibernate.mapping.List;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author danial
 */
@RestController
public class DashboardController {

    //dataToDb databaseOps = new dataToDb();
    //public Connection conn = databaseOps.getRemoteConnection();

    /**
     *
     * @param waterLevelStatus
     * @param tankID
     * @return
     * @throws SQLException
     * @throws JSONException
     */

    @RequestMapping(value = "/dashboard")
    @ResponseBody
    public List viewTable(
            //  public JSONObject toDashboard ()
            @RequestParam(value = "waterLevelStatus") int waterLevelStatus,
            @RequestParam(value = "tankID") String tankID) throws SQLException, JSONException {

        Gson Gsonresults = new Gson();
          Gson gson = new Gson();
        // Tasks
        // Data points for the whole day (like 4 points)
        // Email 
        
        switch (waterLevelStatus) {
            case 0: //Most Recent record
             return DataAccessObject.DashboardQuery4(tankID);
//                 JSONObject json = new JSONObject(DataAccessObject.DashboardQuery4(tankID)); //Converts MAP to JsonObject
//              return json;
            
            case 1: //Last 1 week
                return DataAccessObject.DashboardQuery5(tankID);
                       
                
            case 2: //Last 1 month
                //return DataAccessObject.DashboardQuery6(tankID);
                                
            case 3: // Last 1 Year
                return DataAccessObject.DashboardQuery7(tankID);
                
            default:       //most recent record (realtime) 
              //int res = (int)DataAccessObject.LastReceivedId().get(0);
              
                 //return DataAccessObject.TankVolume(tankID);
                return DataAccessObject.DashboardQuery4(tankID);  //records for last 24 hours(default realtime)
                //return DataAccessObject.LastReceivedId();         
        }

    }
        
}    
  