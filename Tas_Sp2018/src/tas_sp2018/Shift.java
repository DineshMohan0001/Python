
package tas_sp2018;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Shift {
        private String id = null;
	private String description = "";
	//private String start;
        //private String stop;
        private GregorianCalendar start;
	private GregorianCalendar stop;
	private int interval = 0;
	private int graceperiod = 0;
	private int dock = 0;
        //private String lunchstart;
        //private String lunchstop;  
        private GregorianCalendar lunchstart;
	private GregorianCalendar lunchstop;
	private int lunchdeduct = 0;
	
	/* Default Constructor */
	public Shift(){
		//start = new GregorianCalendar();
		
	}
        /*
	public Shift(int id, String description, GregorianCalendar start, GregorianCalendar stop, 
				 int interval, int graceperiod, int dock, GregorianCalendar lunchstart, 
				 GregorianCalendar lunchstop, int lunchdeduct){
		
		this.id = id;
		this.description = description;
		this.start = start;
		this.stop = stop;
		this.interval = interval;
		this.graceperiod = graceperiod;
		this.dock = dock;
		this.lunchstart = lunchstart;
		this.lunchstop = lunchstop;
		this.lunchdeduct = lunchdeduct;
		
	}
        */
        public Shift(String desc,String id, String start, String stop, String lunchstart, String lunchstop){
            
            description = desc;
            this.id = id;
            this.start = new GregorianCalendar();
            this.stop = new GregorianCalendar();
            this.lunchstart = new GregorianCalendar();
            this.lunchstop = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
            
            try{
                Date begin = sdf.parse(start);
                Date end = sdf.parse(stop);
                Date lunchbegin = sdf.parse(lunchstart);
                Date lunchend = sdf.parse(lunchstop);
                
                this.start.setTime(begin);
                this.stop.setTime(end);
                this.lunchstart.setTime(lunchbegin);
                this.lunchstop.setTime(lunchend);
                
                
            }
            catch(Exception e){System.out.println(e);}
        }
        
    public String getId() {
        return id;
    }

    
    public String getDescription() {
        return description;
    }

   
    public GregorianCalendar getStart() {
        return start;
    }

   
    public GregorianCalendar getStop() {
        return stop;
    }

    
    public int getInterval() {
        return interval;
    }

    
    public int getGraceperiod() {
        return graceperiod;
    }

   
    public int getDock() {
        return dock;
    }

    
    public GregorianCalendar getLunchstart() {
        return lunchstart;
    }

    
    public GregorianCalendar getLunchstop() {
        return lunchstop;
    }

    
    public int getLunchdeduct() {
        return lunchdeduct;
    }

    
    public void setId(String id) {
        this.id = id;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    
    public void setStart(GregorianCalendar start) {
        this.start = start;
    }

    
    public void setStop(GregorianCalendar stop) {
        this.stop = stop;
    }

    
    public void setInterval(int interval) {
        this.interval = interval;
    }

    
    public void setGraceperiod(int graceperiod) {
        this.graceperiod = graceperiod;
    }

    
    public void setDock(int dock) {
        this.dock = dock;
    }

    
    public void setLunchstart(GregorianCalendar lunchstart) {
        this.lunchstart = lunchstart;
    }

    
    public void setLunchstop(GregorianCalendar lunchstop) {
        this.lunchstop = lunchstop;
    }

    
    public void setLunchdeduct(int lunchdeduct) {
        this.lunchdeduct = lunchdeduct;
    }

   
    
    @Override
    public String toString(){
        
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        
        Date sStart = start.getTime();
        Date sStop = stop.getTime();
        Date lStart = lunchstart.getTime();
        Date lStop = lunchstop.getTime();
        
        long sDiff = ((sStop.getTime() - sStart.getTime()) / (60 * 1000));
        long lDiff = (lStop.getTime() - lStart.getTime()) / (60 * 1000) ;
        
        return description + ": " + f.format( start.getTime() ) +  " - " + f.format( stop.getTime() ) + " (" + sDiff +" minutes); Lunch: " + f.format(lunchstart.getTime() ) + " - " + 
                f.format(lunchstop.getTime()) + " (" + lDiff + " minutes)";
    }
}
