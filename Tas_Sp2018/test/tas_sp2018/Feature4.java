package tas_sp2018;

import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class Feature4 {
    
    private TASDatabase db;
    
    @Before
    public void setup() {
        db = new TASDatabase();
    }
    
    @Test
    public void testMinutesAccruedShift1Weekday() {
		
        /* Get Punch */
        
        Punch p = db.getPunch(3634);
        Badge b = db.getBadge(p.getBadgeId());
        Shift s = db.getShift(b);
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginalTimeStamp());
		
        /* Compute Pay Period Total */
        
        int m = TASLogic.calculateTotalMinutes(dailypunchlist, s);
		
        /* Compare to Expected Value */
        
        assertEquals(480, m);
        
    }
    
    @Test
    public void testMinutesAccruedShift1Weekend() {
		
        /* Get Punch */
        
        Punch p = db.getPunch(1087);
        Badge b = db.getBadge(p.getBadgeId());
        Shift s = db.getShift(b);
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginalTimeStamp());
		
        /* Compute Pay Period Total */
        
        int m = TASLogic.calculateTotalMinutes(dailypunchlist, s);
		
        /* Compare to Expected Value */
        
        assertEquals(m, 360);
        
    }

    @Test
    public void testMinutesAccruedShift2Weekday() {
		
        /* Get Punch */
        
        Punch p = db.getPunch(4943);
        Badge b = db.getBadge(p.getBadgeId());
        Shift s = db.getShift(b);
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginalTimeStamp());
        
        /* Compute Pay Period Total */
        
        int m = TASLogic.calculateTotalMinutes(dailypunchlist, s);
		
        /* Compare to Expected Value */
        
        assertEquals(540, m);
        
    }
    
}