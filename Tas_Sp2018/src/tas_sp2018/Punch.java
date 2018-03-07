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
	
	public Punch(String badgeId, int terminalId, int punchTypeId){

        this.badgeId = badgeId;
        this.terminalId = terminalId;
        this.eventtypeid = punchTypeId;
        this.punchTypeId = punchTypeId;
        original = new GregorianCalendar();
        adjusted = new GregorianCalendar();
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
  private String getEventType(int eventTypeID) {

        switch (eventTypeID) {
            case 0:
                return "CLOCKED OUT: ";

            case 1:
                return "CLOCKED IN: ";

            default:
                return "TIMED OUT: ";
        }
    }

}
   