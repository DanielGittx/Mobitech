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
          DataAccessObject dao = new DataAccessObject();
        // Tasks
        // Data points for the whole day (like 4 points)
        // Email 
        
        switch (waterLevelStatus) {
            case 0: // Today records
             return dao.DashboardQuery4(tankID);
            case 1: //Last 1 week records
                return DataAccessObject.DashboardQuery5(tankID);
            case 2: //Last 1 month records
                return DataAccessObject.DashboardQuery6(tankID);                 
            case 3: //Last 1 year records
                return DataAccessObject.DashboardQuery7(tankID);        
            default:                
                return dao.DashboardQuery4(tankID);  // Today records (default)
                        
        }

    }
    
    @RequestMapping(value = "/mobidashboard", method = RequestMethod.GET , produces = "application/json")
    public @ResponseBody List viewTable_Upande(
           @RequestParam(value = "tankID") String tankID) throws SQLException, JSONException {

        Gson Gsonresults = new Gson();
        Gson gson = new Gson();
        DataAccessObject dao = new DataAccessObject();
        // Tasks
        // Data points for today (like 4 points)
        // Email 
        return dao.DashboardQuery4(tankID) ;  // Today records (default)
     }
        
}    
  