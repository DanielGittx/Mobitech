/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobitechgrpid.mobitech.Dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import mobitechgrpid.mobitech.Controllers.Devicedetails;
import mobitechgrpid.mobitech.Controllers.LastReceivedDetails;
import org.hibernate.Session;
import mobitechgrpid.mobitech.Services.NewHibernateUtil;
import mobitechgrpid.mobitech.Services.multipliers;
import mobitechgrpid.mobitech.Services.DeviceMessageConstants;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.ResponseBody;
import mobitechgrpid.mobitech.Controllers.ServerController;
import mobitechgrpid.mobitech.Controllers.Tankdetails;
import org.hibernate.SQLQuery;
import mobitechgrpid.mobitech.Controllers.Tankdetails;
/**
 *
 * @author danial
 */
public class DataAccessObject implements DeviceMessageConstants {
    
    //Session session = NewHibernateUtil.getSessionFactory().openSession();
    
    public static boolean adddevicedata( Double currentTankCapacity, String dateGeneratedOnDevice, String errorCode, String hwVersion, Double signalStrength, String tankId, double waterLevel,String DateSavedOnDb, long LastReceivedValueToAfricasTalking ){
        
        int recid = 0;
        ServerController sc = new ServerController();
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            
                                                 //Double currentTankCapacity, String dateGeneratedOnDevice, String errorCode, String hwVersion, Double signalStrength, String tankId, double waterLevel, String DateSavedOnDb                        
            Devicedetails dd  = new Devicedetails(currentTankCapacity, dateGeneratedOnDevice, errorCode, hwVersion, signalStrength, tankId, waterLevel, sc.StringToDateConverter(DateSavedOnDb), LastReceivedValueToAfricasTalking );
            recid = (Integer) session.save(dd);
            tx.commit();
            
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            if (tx!=null)
            {
             tx.rollback();
            }
        }finally
        {
            session.close();
        }
       
        
        return recid>0;
        
    }
    
    public static boolean addSMSProcessedData(double waterLevel,String tankID,double currentTankCapacity, String DateSavedOnDb, long LastReceivedValueToAfricasTalking ){
        ServerController sc = new ServerController();
        int recid = 0;
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            
                      // double waterLevel,String tankId,currentTankCapacity                   
            Devicedetails dd  = new Devicedetails(waterLevel,tankID,currentTankCapacity,sc.StringToDateConverter(DateSavedOnDb), LastReceivedValueToAfricasTalking);      
            recid = (Integer) session.save(dd);
            tx.commit();
            
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            if (tx!=null)
            {
             tx.rollback();
            }
        }finally
        {
            session.close();
        }
        return recid>0;
    }
    //Long LastReceivedValueToAfricasTalking
    
        public static boolean save_LastReceivedId(long LastReceivedValueToAfricasTalking, String DateSavedOnDb ){
        ServerController sc = new ServerController();
         int recid = 0;
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
             //System.out.println("This is saved on DB" +sc.StringToDateConverter(DateSavedOnDb));                 
             LastReceivedDetails lrd  = new LastReceivedDetails(LastReceivedValueToAfricasTalking, sc.StringToDateConverter(DateSavedOnDb));     // Create Object 
         
            recid = (Integer) session.save(lrd);
            tx.commit();
            
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            if (tx!=null)
            {
             tx.rollback();
            }
        }finally
        {
            session.close();
        }
        return recid>0;
    }
    
    public static String DashboardQuery (String tankID){
             
        Session session = NewHibernateUtil.getSessionFactory().openSession();
         
        try {

            session.beginTransaction();
           Criteria criteria = session.createCriteria(Devicedetails.class);

            criteria.add(Restrictions.eq("tankId", tankID));
            Devicedetails devdetails = (Devicedetails) criteria.uniqueResult();
            if (devdetails!=null) {

                System.out.println("Tank Details found:");

                System.out.println(devdetails.getTankId() + " - " + devdetails.getWaterLevel());
                return devdetails.getTankId() + " ---> " + devdetails.getWaterLevel() ;

            }
            session.getTransaction().commit();

        }
        catch (HibernateException e) {

            e.printStackTrace();

            session.getTransaction().rollback();

        }
           return "Error occured";
        
    }
    
       public static List LastReceivedIdValue  (){ 
    
         Session session = NewHibernateUtil.getSessionFactory().openSession();
         Query query = session.createQuery("SELECT lastReceivedValueToAfricasTalking FROM LastReceivedDetails dc ORDER BY dc.lastReceivedId DESC");  //HQL  
        /// SELECT recordid FROM mobiwaterDB.devicedetails ORDER BY recordid DESC LIMIT 1;      //SQL Works ok
         List results = query.setMaxResults(1).list();
         
        // System.out.println(results.get(0));
         
        return results;
 
       }
       
       /*
          Function - public static double TankVolume()
          Input Parameters - String tankID, Double water_level
          Comments - Ensure that water_level value passed is apprpriately processed 
                     i.e ultrasonic sensor or atmospheric pressure sensor
       
       */
        public static double TankVolume  (String tankID, Double _waterlevel){ 
    
         Session session = NewHibernateUtil.getSessionFactory().openSession();
         //Query query = session.createQuery("SELECT waterLevel FROM devicedetails dd WHERE dd.tankId = '"+tankID+"'");  //HQL  
         Query query1 = session.createQuery("SELECT tankBaseArea FROM Tankdetails dd WHERE dd.tankId = '"+tankID+"'");  //HQL  
       
        // List tankHeightList = query.list();        //current tank height
         List tankBaseAreaList = query1.list();
        
         //String tankHeightString = tankHeightList.get(0).toString();
         String tankBaseAreaString = tankBaseAreaList.get(0).toString();
         
        // double tankHeightDouble = Double.parseDouble( tankHeightString);
         double tankBaseAreaDouble = Double.parseDouble( tankBaseAreaString);
         return (_waterlevel * tankBaseAreaDouble * 1000);             //litres
    }
        
        
        public static double calculateCapacity(String tankID, double _waterlevel) {

        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Query query1 = session.createQuery("SELECT COALESCE (typeOfSensor,0) FROM Tankdetails dd WHERE dd.tankId = '" + tankID + "'");  //HQL  
        Query query2 = session.createQuery("SELECT tankHeight FROM Tankdetails dd WHERE dd.tankId = '" + tankID + "'");
        List typeofSensor = query1.list(); //
        List tankHeight = query2.list(); //
        
        int sensorType = Integer.parseInt(typeofSensor.get(0).toString());
        double fullTankHeight = Double.parseDouble(tankHeight.get(0).toString());
        

        
    /*    
        //System.out.println("returnQuery");
        System.out.println(typeofSensor);
        List<Tankdetails> tankdetailsList=null;
        int sensorType =0;
         double fullTankHeight = 0.0;
      
        
        //process the Results
	for(Tankdetails tkd:tankdetailsList){
		System.out.println("Object---->"+tkd);
		//get all childs of each parent
			
			System.out.println("Type of sensor--->"+tkd.getTypeOfSensor());
                        System.out.println("Tank Height--->"+tkd.getTankHeight());
                        sensorType = tkd.getTypeOfSensor();
                        fullTankHeight = tkd.getTankHeight();
        }
       */

        double processed_height = 0;
        processed_height = fullTankHeight - _waterlevel;

        switch (sensorType) {
            case 0:         //Transducer
                return DataAccessObject.TankVolume(tankID, _waterlevel);          //capacity litres for transducer
            case 1:         //Ultrasonic
                return DataAccessObject.TankVolume(tankID, processed_height);          //capacity litres for transducer
            default:
                return DataAccessObject.TankVolume(tankID, _waterlevel);      //capacity litres for transducer
        }

    }
        
      
    public static List DashboardQuery2 (){                  //return the whole database :)
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Devicedetails";
        Query query = session.createQuery(hql);
        List results = query.list(); 
        //session.close();
        return results;
        
    }
    
    public static List DashboardQuery3 (){                     //return all records of a given id
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Devicedetails dd WHERE dd.tankId = 00";
        Query query = session.createQuery(hql);
        List results = query.list();
        // session.close();
        return results;
        
    }
   //public static List DashboardQuery4 (String tankID){                     //return last record of a given id   (REALTIME!!!!!!!!)
    public List DashboardQuery4 (String tankID){
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    Calendar c = Calendar.getInstance();
    c.add(Calendar.HOUR, -24);         //parameterize query and give as an argument Date that is 7 days in past
    Date d = c.getTime();

    List results = 
        session.createQuery("FROM Devicedetails dd WHERE dd.tankId = '"+tankID+"' AND dd.dateSavedOnDb > :param")
          .setParameter("param", d)
          .list();
    
         return results ;
   }   
    @ResponseBody
    public static List DashboardQuery5 (String tankID){                     //return last record of a given id   (last 1 week!!!!!!!!)
    
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_YEAR, -7);         //parameterize query and give as an argument Date that is 7 days in past
    Date d = c.getTime();

    List results = 
        session.createQuery("FROM Devicedetails dd WHERE dd.tankId = '"+tankID+"' AND dd.dateSavedOnDb > :param")
          .setParameter("param", d)
          .list();
        // session.close();
       return results;
     }
       
    public static List DashboardQuery6 (String tankID){                     //return   (last 1 MONTH!!!!!!!!)
    
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_YEAR, -30);         //parameterize query and give as an argument Date that is 30 days in past
    Date d = c.getTime();

    List results = 
        session.createQuery("FROM Devicedetails dd WHERE dd.tankId = '"+tankID+"' AND dd.dateSavedOnDb > :param")
          .setParameter("param", d)
          .list();
        // session.close();
       return results;
     }
    
        @ResponseBody
    public static List DashboardQuery7(String tankID){                     //return    (last 1 year!!!!!!!!)
    
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_YEAR, -365);         //parameterize query and give as an argument Date that is 365 days in past
    Date d = c.getTime();

    List results = 
        session.createQuery("FROM Devicedetails dd WHERE dd.tankId = '"+tankID+"' AND dd.dateSavedOnDb > :param")
          .setParameter("param", d)
          .list();
        // session.close();
       return results;
     }
    
    
    
}
