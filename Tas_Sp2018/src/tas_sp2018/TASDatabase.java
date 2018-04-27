package tas_sp2018;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Accesses and inserts information from the database
 * 
 * @author Shauntara Green, Andrew Blair, Jacob O'Dell, Derrick Godwin, Zeth Malcom
 */
public class TASDatabase {
    
    //Initialization of objects needed in the "get" methods and creation of a statement
    private Punch finalPunch = new Punch();
    private Badge finalBadge = new Badge();
    private Shift finalShift = new Shift();
    static Statement stmt;
    private Connection con;
    
    public TASDatabase(){
        //Opens the connection to the database and creates a statement for interacting with it
        String url;
        
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
        catch(SQLException e){System.out.println(e);}
    }
    
    public void closeStmt(Statement stmt){
        
        //Closes the statement
        try{
            stmt.close();
        }
        catch(SQLException e){System.out.println(e);}
    }
    
    public Punch getPunch(int punchid){
       String idString =  Integer.toString(punchid);
       
       String badgeid;
       int termIDInt;

       //Query for Punch info
       try{
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
        catch(SQLException e){System.out.println(e);}
        
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

                //Creates and populates the Shift object
                finalShift = new Shift(id, desc, start, stop, interval, GP, dock, lunchStart, lunchStop, lunchDe);
                
            }
        }
        catch(SQLException e){System.out.println(e);}
        
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
        
        ArrayList finalList = new ArrayList();
        
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

        String badgeid = b.getId();
        try{
            ResultSet rs = stmt.executeQuery("SELECT * FROM event WHERE UNIX_TIMESTAMP(originaltimestamp) >= (start.getTimeInMillis() / 1000) "
                    + "AND UNIX_TIMESTAMP(originaltimestamp) <= (stop.getTimeInMillis()/1000) AND"
                    + " badgeid='"+badgeid+"' AND ORDER BY originaltimestamp");
            
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
                    newPunchId = result.getInt(1);
                }
            }
        } 
        catch (SQLException e){System.out.println(e);}
        
        return newPunchId;
    }
}
