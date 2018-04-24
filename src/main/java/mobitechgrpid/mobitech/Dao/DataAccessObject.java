/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobitechgrpid.mobitech.Dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import mobitechgrpid.mobitech.Controllers.Devicedetails;
import org.hibernate.Session;
import mobitechgrpid.mobitech.Services.NewHibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author danial
 */
public class DataAccessObject {
    
    //Session session = NewHibernateUtil.getSessionFactory().openSession();
    
    public static boolean adddevicedata(double waterLevel, Double signalStrength, String hwVersion, String tankId,String dateGeneratedOnDevice, String errorCode){
        
        int recid = 0;
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            
                                            //double waterLevel, Double signalStrength, String hwVersion, Date dateSavedOnDb, String tankId, Integer organizationId, String dateGeneratedOnDevice, String errorCode
            Devicedetails dd  = new Devicedetails(  waterLevel,  signalStrength,  hwVersion, tankId,   dateGeneratedOnDevice,  errorCode);
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
    
    public static boolean addSMSProcessedData(double waterLevel,String tankID, Long lastReceivedId ){
        
         int recid = 0;
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            
                      // double waterLevel,String tankId,lastReceivedId                   
            Devicedetails dd  = new Devicedetails(waterLevel,tankID, lastReceivedId );        //lastReceivedId - our lastrecord from africastalking 
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
    
       public static List LastReceivedId  (){ 
    
         Session session = NewHibernateUtil.getSessionFactory().openSession();
         Query query = session.createQuery("SELECT lastReceivedId FROM Devicedetails dd ORDER BY dd.recordid DESC");  //HQL  
        /// SELECT recordid FROM mobiwaterDB.devicedetails ORDER BY recordid DESC LIMIT 1;      //SQL Works ok
         List results = query.setMaxResults(1).list();
         
        // System.out.println(results.get(0));
         
        return results;
 
       }
 
       public static double TankVolume  (String tankID){ 
    
         Session session = NewHibernateUtil.getSessionFactory().openSession();
         Query query = session.createQuery("SELECT tankHeight FROM Tankdetails dd WHERE dd.tankId = '"+tankID+"'");  //HQL  
         Query query1 = session.createQuery("SELECT tankBaseArea FROM Tankdetails dd WHERE dd.tankId = '"+tankID+"'");  //HQL  
       
         List tankHeightList = query.list();
         List tankBaseAreaList = query1.list();
        
         String tankHeightString = tankHeightList.get(0).toString();
         String tankBaseAreaString = tankBaseAreaList.get(0).toString();
    
         double tankHeightDouble = Double.parseDouble( tankHeightString);
         double tankBaseAreaDouble = Double.parseDouble( tankBaseAreaString);
         return (tankHeightDouble * tankBaseAreaDouble);
    
 
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
   public static List DashboardQuery4 (String tankID){                     //return last record of a given id   (REALTIME!!!!!!!!)
    
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    Calendar c = Calendar.getInstance();
    c.add(Calendar.HOUR, -24);         //parameterize query and give as an argument Date that is 7 days in past
    Date d = c.getTime();

    List results = 
        session.createQuery("FROM Devicedetails dd WHERE dd.tankId = '"+tankID+"' AND dd.dateSavedOnDb > :param")
          .setParameter("param", d)
          .list();
        // session.close();
       return results;
       
       /*
         Session session = NewHibernateUtil.getSessionFactory().openSession();
         Query query = session.createQuery("from Devicedetails dd WHERE dd.tankId = '"+tankID+"' ORDER BY dd.recordid DESC");
         List results = query.setMaxResults(1).list();
         // session.close();
           return results;
           */
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
        //@ResponseBody
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
