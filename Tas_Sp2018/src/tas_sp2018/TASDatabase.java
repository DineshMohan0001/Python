/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_sp2018;

import java.sql.*;
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
    
    public static void main(String[] args) {
  
    }
    
    
    public TASDatabase(){
        
        //Opens the connection to the database and creates a statement for interacting with it
        
        String url = null;
        Connection con;
        
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
       
       
       //Query for Punch info
       try{
           //Query for Punch info
            ResultSet rs = stmt.executeQuery("SELECT * FROM event WHERE id="+idString );
            if (rs != null){
                rs.next();
                String id = rs.getString("id");
                String termID= rs.getString("terminalid");
                String badgeid = rs.getString("badgeid");
                String OGTS = rs.getString("originaltimestamp");
                String ETID= rs.getString("eventtypeid");
                String evData= rs.getString("eventdata");
                //String adjTS = rs.getString("adjustedtimestamp");
                
                //Converts query data into parameters for objects
                //Badge punchBadge = getBadge(badgeid);
                int termIDInt = Integer.parseInt(termID);
                int punchtypeID = Integer.parseInt(ETID);
                //long ogts = Long.parseLong(OGTS);
                
                
                //Creates and populates the Punch object
                finalPunch = new Punch(badgeid, termIDInt, punchtypeID, OGTS);
                
                
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
               
               //Converts query data into parameters for objects
               //int id2 = Integer.parseInt(badge);
               
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
                finalShift = new Shift(desc, id, start, stop, lunchStart, lunchStop);
                
            }
        }
        catch(Exception e){System.out.println(e);}
        
        return finalShift;
    }
    
    public Shift getShift(Badge badge){
        
       
        
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee WHERE badgeid =" + badge);
            if (rs != null){
            
            }
        }
        catch(Exception e){System.out.println(e);}
        
        return null;
    }
}
