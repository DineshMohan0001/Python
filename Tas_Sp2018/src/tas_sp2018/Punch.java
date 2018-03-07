package tas_sp2018;

import java.util.*;
import java.text.SimpleDateFormat;

public class Punch {

    public Punch(){}
    
    private int shiftId;
    private String punchId;
    private String badgeId;
    private String punchDescription;
    private int terminalId;
    private GregorianCalendar original;
    private GregorianCalendar adjusted;
    private String sdf;
    private int eventtypeid;
    private int punchTypeId;

    public Punch(int terminalId, String badgeId, int shiftId, long originalts, int eventtypeid) {

        original = new GregorianCalendar();
        adjusted = new GregorianCalendar();
        //originalts = (originalts * 1000);
        //adjustedts = (originalts * 1000);
        //original.setTimeInMillis(originalts);
        //adjusted.setTimeInMillis(adjustedts);
        this.terminalId = terminalId;
        this.badgeId = badgeId;
        this.shiftId = shiftId;
        this.eventtypeid = eventtypeid;

        sdf = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(original.getTime()).toUpperCase();
    }
	
	public Punch(String badgeId, int terminalId, int punchTypeId, String timeStamp){

        this.badgeId = badgeId;
        this.terminalId = terminalId;
        this.eventtypeid = punchTypeId;
        this.punchTypeId = punchTypeId;
        original = new GregorianCalendar();
        adjusted = new GregorianCalendar();
        
    SimpleDateFormat lmao = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.US); 

    try{ 
    Date d = lmao.parse(timeStamp); 

    //cal = new GregorianCalendar(); 

    original.setTime(d); 
    sdf = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(original.getTime()).toUpperCase(); 

    }
        
    catch(Exception e){System.out.println(e);} 
             
      //sdf = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(original.getTime()).toUpperCase();
     }
    public String getPunchDescription() {

        return punchDescription;
    }

    public void setDescription(String punchDescription) {

        this.punchDescription = punchDescription;
    }

    public String getPunchId() {

        return punchId;
    }

    public void setPunchId(String punchId) {

        this.punchId = punchId;
    }

    public String getBadgeId() {

        return badgeId;
    }

    public int getTerminalId() {

        return terminalId;
    }

    public int getShiftId() {

        return shiftId;
    }

    public GregorianCalendar getOriginalTimeStamp() {

        return adjusted;
    }

    public GregorianCalendar getAdjustedTimeStamp() {

        return adjusted;
    }

    public int geteventtypeid(){

        return eventtypeid;
    }
public String printOriginalTimestamp() {

        String Status = "";

        if (eventtypeid == 1) {
            Status = " CLOCKED IN: ";

        } else if (eventtypeid == 0) {
            Status = " CLOCKED OUT: ";

        } else {
            Status = " TIMED OUT: ";
        }

        return "#" + terminalId + badgeId + Status + sdf;
    }
}
   