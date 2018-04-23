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
    private String eventData;
    

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
	
    public Punch(String badgeId, int terminalId, int punchTypeId, long timeStamp){

        this.badgeId = badgeId;
        this.terminalId = terminalId;
        this.eventtypeid = punchTypeId;
        this.punchTypeId = punchTypeId;
        original = new GregorianCalendar();
        adjusted = new GregorianCalendar();
        long originalts = timeStamp;
        original.setTimeInMillis(originalts);
        

        sdf = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(original.getTime()).toUpperCase();
             
      //sdf = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(original.getTime()).toUpperCase();
    }
    
    public void adjust (Shift s) {
       //sets the shift date to the date of the punch for sake of comparison
       adjusted = original;
       
       s.getStart().set(Calendar.YEAR, original.get(Calendar.YEAR));
       s.getStart().set(Calendar.MONTH, original.get(Calendar.MONTH));
       s.getStart().set(Calendar.DAY_OF_MONTH, original.get(Calendar.DAY_OF_MONTH));
       s.getStop().set(Calendar.YEAR, original.get(Calendar.YEAR));
       s.getStop().set(Calendar.MONTH, original.get(Calendar.MONTH));
       s.getStop().set(Calendar.DAY_OF_MONTH, original.get(Calendar.DAY_OF_MONTH));
       s.getLunchstart().set(Calendar.YEAR, original.get(Calendar.YEAR));
       s.getLunchstart().set(Calendar.MONTH, original.get(Calendar.MONTH));
       s.getLunchstart().set(Calendar.DAY_OF_MONTH, original.get(Calendar.DAY_OF_MONTH));
       s.getLunchstop().set(Calendar.YEAR, original.get(Calendar.YEAR));
       s.getLunchstop().set(Calendar.MONTH, original.get(Calendar.MONTH));
       s.getLunchstop().set(Calendar.DAY_OF_MONTH, original.get(Calendar.DAY_OF_MONTH));
       
        /*Each calendar represents the bounds for the critical zones*/
       GregorianCalendar beforeShiftStart = new GregorianCalendar();
       beforeShiftStart.setTimeInMillis(s.getStart().getTimeInMillis());
       beforeShiftStart.add(Calendar.MINUTE, -(s.getInterval()));
       
       GregorianCalendar beforeGraceStart = new GregorianCalendar();
       beforeGraceStart.setTimeInMillis(s.getStart().getTimeInMillis());
       beforeGraceStart.add(Calendar.MINUTE, s.getGracePeriod());
       
       GregorianCalendar afterStartDock = new GregorianCalendar();
       afterStartDock.setTimeInMillis(s.getStart().getTimeInMillis());
       afterStartDock.add(Calendar.MINUTE, s.getDock());
       
       GregorianCalendar beforeStopDock = new GregorianCalendar();
       beforeStopDock.setTimeInMillis(s.getStop().getTimeInMillis());
       beforeStopDock.add(Calendar.MINUTE, -( s.getDock() ) );
       
       GregorianCalendar beforeStopGrace = new GregorianCalendar();
       beforeStopGrace.setTimeInMillis(s.getStop().getTimeInMillis());
       beforeStopGrace.add(Calendar.MINUTE, -( s.getGracePeriod() ) );
       
       GregorianCalendar afterShiftStop = new GregorianCalendar();
       afterShiftStop.setTimeInMillis(s.getStop().getTimeInMillis());
       afterShiftStop.add(Calendar.MINUTE, s.getInterval());
       
     
       //gets information needed for special cases if it is one.
       int punchmins = original.get(Calendar.MINUTE);
       int diffmins = punchmins;
       int dayofweeknum = original.get(Calendar.DAY_OF_WEEK);
        while(diffmins > s.getInterval()){
           diffmins -= s.getInterval();
       }
       boolean isWeekend = dayofweeknum == 7 || dayofweeknum == 1;          //check if its a weekend or not, specific rules do not apply to them
       boolean beforeStartLine = original.compareTo(beforeShiftStart) == 0;
       boolean startGraceLine = original.compareTo(beforeGraceStart) == 0;
       boolean startDockLine = original.compareTo(afterStartDock) == 0;
       boolean stopDockLine = original.compareTo(beforeStopDock) == 0;
       boolean stopGraceLine = original.compareTo(beforeStopGrace) == 0;
       boolean afterStopLine = original.compareTo(afterShiftStop) == 0;
       
       
       if(original.before(s.getStart()) && original.after(beforeShiftStart) && !isWeekend || beforeStartLine){
           adjusted.setTimeInMillis(s.getStart().getTimeInMillis());
           eventData = "(Shift Start)";
       }
       else if(original.after(s.getStart()) && original.before(beforeGraceStart) && !isWeekend || startGraceLine){
           adjusted.setTimeInMillis(s.getStart().getTimeInMillis());
           eventData = "(Shift Start)";
       }
       else if(original.after(beforeGraceStart) && original.before(afterStartDock) && !isWeekend || startDockLine){
           adjusted.setTimeInMillis(afterStartDock.getTimeInMillis());
           eventData = "(Shift Dock)";
       }
       else if(original.after(s.getLunchstart()) && original.before(s.getLunchstop()) && eventtypeid == 0 && !isWeekend){
           adjusted.setTimeInMillis(s.getLunchstart().getTimeInMillis());
           eventData = "(Lunch Start)";
       }
       else if(original.after(s.getLunchstart()) && original.before(s.getLunchstop()) && eventtypeid == 1 && !isWeekend){
           adjusted.setTimeInMillis(s.getLunchstop().getTimeInMillis());
           eventData = "(Lunch Stop)";
       }
       else if(original.after(beforeStopDock) && original.before(beforeStopGrace) && !isWeekend || stopDockLine){
           adjusted.setTimeInMillis(beforeStopDock.getTimeInMillis());
           eventData = "(Shift Dock)";
       }
       else if(original.after(beforeStopGrace) && original.before(s.getStop()) && !isWeekend || stopGraceLine){
           adjusted.setTimeInMillis(s.getStop().getTimeInMillis());
           eventData = "(Shift Stop)";
       }
       else if(original.after(s.getStop()) && original.before(afterShiftStop) && !isWeekend || afterStopLine){
           adjusted.setTimeInMillis(s.getStop().getTimeInMillis());
           eventData = "(Shift Stop)";
       }
       else if(punchmins % s.getInterval() == 0){
          adjusted.setTimeInMillis(original.getTimeInMillis());
          eventData = "(None)";
       }
       else if( diffmins <= Math.floor(s.getInterval() / 2) ){
            if(diffmins == Math.floor(s.getInterval() / 2) && original.get(Calendar.SECOND) != 0){
                adjusted.setTimeInMillis(original.getTimeInMillis());
                adjusted.add(Calendar.MINUTE, (s.getInterval() - diffmins));
                adjusted.set(Calendar.SECOND, 0);
                eventData = "(Interval Round)";
            }
            else{
                adjusted.setTimeInMillis(original.getTimeInMillis());
                adjusted.add(Calendar.MINUTE, -diffmins);
                adjusted.set(Calendar.SECOND, 0);
                eventData = "(Interval Round)";
            }
        }
       else if(diffmins > Math.floor(s.getInterval() / 2)){
            adjusted.setTimeInMillis(original.getTimeInMillis());
            adjusted.add(Calendar.MINUTE, (s.getInterval() - diffmins));
            adjusted.set(Calendar.SECOND, 0);
            eventData = "(Interval Round)";
        }
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

    
    public String printAdjustedTimestamp(){
    
        
        String adj = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(adjusted.getTime()).toUpperCase();
     
        String Status = "";

       if (eventtypeid == 1) {
            Status = " CLOCKED IN: ";

        } else if (eventtypeid == 0) {
            Status = " CLOCKED OUT: ";

        } else {
            Status = " TIMED OUT: ";
        }

        return "#" + badgeId + Status + adj + " " +eventData;
    
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

        return "#" + badgeId + Status + sdf;
    }
}
   