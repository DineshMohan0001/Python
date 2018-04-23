/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_sp2018;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author Andrew
 */


public class TASDatabase {
    
    
    //Initialization of objects needed in the "get" methods and creation of a statement
    private Punch finalPunch = new Punch();
    private Badge finalBadge = new Badge();
    private Shift finalShift = new Shift();
    static Statement stmt;
    /*I initialized the connection here instead of the constructor so that I can use it in the insertPunch 
    method hopefully it won't break anything -Chris*/
    private Connection con;
    
    
    public TASDatabase(){
        
        //Opens the connection to the database and creates a statement for interacting with it
        
        String url = null;
        
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            url = "jdbc:mysql://localhost:3306/tas";
            con = DriverManager.getConnection(url , "root", "Te#4RoPa");
            stmt = con.createStatement();
        }
        catch(Exception e){System.out.println(e);}

    }
    
    public void closeCon(Connection con){
        
        //Closes the connection to the database
        
        try{
            con.close();
        }
        catch(Exception e){System.out.println(e);}
    }
    
    public void closeStmt(Statement stmt){
        
        //Closes the statement
        
        try{
            stmt.close();
        }
        catch(Exception e){System.out.println(e);}
    }
    
    public Punch getPunch(int punchid){
       String idString =  Integer.toString(punchid);
       
       String badgeid;
       int termIDInt;

       //Query for Punch info
       try{
           //Query for Punch info
            ResultSet rs = stmt.executeQuery("SELECT *, UNIX_TIMESTAMP(originaltimestamp) * 1000 AS `timestamp` FROM event WHERE id='"+idString +"'");
            if (rs != null ){
                rs.next();
                String id = rs.getString("id");
                String termID= rs.getString("terminalid");
                badgeid = rs.getString("badgeid");
                String originalTS = rs.getString("originaltimestamp");
                String eventID= rs.getString("eventtypeid");
                String evData = rs.getString("eventdata");
                String longTimeStamp = rs.getString("timestamp");
                //String adjustedTS = rs.getString("adjustedtimestamp");
                
                //Converts query data into parameters for objects

                termIDInt = Integer.parseInt(termID);
                int punchtypeID = Integer.parseInt(eventID);
                long timeStamp = Long.parseLong(longTimeStamp);
                
                
                //Creates and populates the Punch object
                finalPunch = new Punch(badgeid, termIDInt, punchtypeID, timeStamp) ;
                    
            }
                   
       }
       catch(Exception e){System.out.println(e);}
       
       return finalPunch;
    }
    
    public Badge getBadge(String id){
        
        
        //Query for the badge info
        try{
            
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM badge WHERE id='"+id+"'");
            if (rs != null){
               rs.next();
               String badge = rs.getString("id");
               String desc = rs.getString("description");
               
               
               //Creates and populates the Badge object
               finalBadge = new Badge(desc, badge);
               
            }
        }
        catch(Exception e){System.out.println(e);}
        
        return finalBadge;
    }
    
    public Shift getShift(int shiftid){
        String shiftidString = Integer.toString(shiftid);
        
        //Query for shift ruleset info
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM shift WHERE id=" + shiftidString);
            if (rs != null){
                rs.next();
                String id = rs.getString("id");
                String desc = rs.getString("description");
                String start = rs.getString("start");
                String stop = rs.getString("stop");
                String interval = rs.getString("interval");
                String GP = rs.getString("graceperiod");
                String dock = rs.getString("dock");
                String lunchStart = rs.getString("lunchstart");
                String lunchStop = rs.getString("lunchstop");
                String lunchDe= rs.getString("lunchdeduct");
                //String maxTime= rs.getString("maxtime");
                //String OTHold= rs.getString("overtimethreshold");
                
                
                //Creates and populates the Shift object
                finalShift = new Shift(id, desc, start, stop, interval, GP, dock, lunchStart, lunchStop, lunchDe);
                
            }
        }
        catch(Exception e){System.out.println(e);}
        
        return finalShift;
    }
    
    public Shift getShift(Badge badge){
    String badgeID = badge.getId();
        
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee WHERE badgeid ='" + badgeID + "'");
            if (rs != null){
                rs.next();
                String shiftid = rs.getString("shiftid");
                
                int intShiftID = Integer.parseInt(shiftid);
                
                finalShift = getShift(intShiftID);
               
                
            
            }
        }
        catch(Exception e){System.out.println(e);}
        
        return finalShift;
    }
    
    public ArrayList getDailyPunchList (Badge b, GregorianCalendar ts){
        
        //Step 2: Make a query for records of the punches for the Badge //Problem 1: How to pull punches for the Badge for specific dates
        //Step 3: Put punches into finalList
        
        ArrayList finalList = new ArrayList();
        
        //Step 1
        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(ts.getTimeInMillis());
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        //Initialize a stop
        GregorianCalendar stop = new GregorianCalendar();
        stop.setTimeInMillis(ts.getTimeInMillis());
        stop.set(Calendar.HOUR, 23);
        stop.set(Calendar.MINUTE, 59);
        stop.set(Calendar.SECOND, 59);

      //Step 2
        String badgeid = b.getId();
        
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM event WHERE UNIX_TIMESTAMP(originaltimestamp) >= (start.getTimeInMillis() / 1000) "
                    + "AND UNIX_TIMESTAMP(originaltimestamp) <= (stop.getTimeInMillis()/1000) AND"
                    + " badgeid='"+badgeid+"' AND ORDER BY originaltimestamp");
            
            //Step 3
            int columnCount = rs.getMetaData().getColumnCount();
            while(rs.next()){
                String[] row = new String[columnCount];
                for(int i=0; i < columnCount; i++){
                    row[i] = rs.getString(i + 1);
                }
                finalList.add(row);
            }
        }
        catch(Exception e){System.out.println(e);}
        
        return finalList;
    }
    
    public int insertPunch(Punch punch){
        //Initializing and prepping variables
        int newPunchId= 0 ;
        GregorianCalendar cal = punch.getOriginalTimeStamp();
        int rs = 0;
        cal.setTimeInMillis(cal.getTimeInMillis());
       
        //format query to be inserted into database
        String query= "INSERT INTO event (id,terminalid,badgeid,originaltimestamp,eventtypeid,eventdata) VALUES(?,?,?,?,?,?)";                           
        //insert values into query
        try{
            Statement stmnt= con.createStatement();
            PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, punch.getPunchId());
            ps.setInt(2, (punch.getTerminalId()));
            ps.setString(3, punch.getBadgeId());
            ps.setString(4, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(cal.getTime()).toUpperCase() );
            ps.setInt(5, (punch.getPunchTypeId()));
            ps.setString(6, null);
            
            
            //this should update the database making the new punch the 1st item in the event section
            rs = ps.executeUpdate();
            ResultSet result;
            //if results exist, assign the punchId in row#1 col#1 to newPunchId
            if (rs ==  1){
                //row #1
                result = ps.getGeneratedKeys();
                //column #1
                if (result.next()){
                newPunchId=result.getInt(1);
                }
            }
        } 
        catch (Exception e){System.out.println(e);}
        return newPunchId;
        
    }
}
