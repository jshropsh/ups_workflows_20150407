/**
 * <p>Project Name : UPS_workflow_tasks</p>
 * <p>File Name    : ScheduleDelayHelper.java</p>
 * <p>Date Created : Feb 28, 2015</p>
 *
 * <p>Revision History :</p>
 *
 * <p>Date				Login             Revision 
 *================================================================
 * Feb 28, 2015			shropshire				Created	
 *</p>
 */
package com.hp.autonomy.proserv.pkg.ls.core.task;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.interwoven.cssdk.common.CSException;
import com.interwoven.cssdk.workflow.CSExternalTask;

/**
 * <p> Package Name     : com.hp.autonomy.proserv.pkg.ls.core.task</p>
 * <p> File Name        : ScheduleDelayHelper.java</p>
 * <p> Type Name 	: ScheduleDelayHelper</p>
 * <p> Date Created     : Feb 28, 2015</p>
 * <p> Description	: ScheduleDelayHelper does the date/time math needed to schedule repeated runs of the campaign publish task.</p>
 * @author shropshire
 *
 * <p>Revision History :</p>
 *
 * <p>Date			Login             	Revision </p>
 * <p>================================================================</p>
 * <p>Feb 28, 2015			shropshire			Created</p>
 * <p>Apr 06, 2015                      shropshire                      Added getHourInterval() and 
 *                                                                      getNextScheduledHour()	</p> 
 * <p>Apr 08, 2015                      shropshire                      Added getMinuteInterval() and 
 *                                                                      getNextScheduledInterval()                                                                     
 */

public class ScheduleDelayHelper {

    
    /**
     * 
     */
    public ScheduleDelayHelper(CSExternalTask task) {
	LOGGER.info("In ScheduleDelayHelper constructor" );
	try {
	    setTask(task);
	    setWfDayOfMonth(getTaskVariable(WF_DAY_OF_MONTH_KEY));
	    setWfDayOfWeek(getTaskVariable(WF_DAY_OF_WEEK_KEY));
	    setWfEndDateTime(getTaskVariable(WF_END_DATE_TIME_KEY));
	    setWfHourOfDay(getTaskVariable(WF_HOUR_OF_DAY_KEY));
	    setWfSchedulingInterval(getTaskVariable(WF_SCHED_INTERVAL_KEY));
	    setWfStartDateTime(getTaskVariable(WF_START_DATE_TIME_KEY));
	    setWfHourInterval(getTaskVariable(WF_HOUR_INTERVAL_KEY));
	    this.setWfMinuteInterval(getTaskVariable(WF_MINUTE_INTERVAL_KEY)); 
	    nowCal = Calendar.getInstance(); 
	    LOGGER.info(this); 
	    
	    
	}  catch (Exception ex) {
	   LOGGER.error("Caught exception trying to instantiate ScheduleDelayHelper"); 
	   LOGGER.error(ex); 
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	System.out.println("Print out a date :");
	Calendar printCal = Calendar.getInstance(); 
	Date calDate = printCal.getTime(); 
	System.out.println(calDate); 
	// TODO Auto-generated method stub

    }
    
    public String getWfDayOfMonth() {
        return wfDayOfMonth;
    }

    public void setWfDayOfMonth(String wfDayOfMonth) {
        this.wfDayOfMonth = wfDayOfMonth;
    }

    public String getWfDayOfWeek() {
        return wfDayOfWeek;
    }

    public void setWfDayOfWeek(String wfDayOfWeek) {
        this.wfDayOfWeek = wfDayOfWeek;
    }

    public String getWfHourOfDay() {
        return wfHourOfDay;
    }

    public void setWfHourOfDay(String wfHourOfDay) {
        this.wfHourOfDay = wfHourOfDay;
    }

    public String getWfSchedulingInterval() {
        return wfSchedulingInterval;
    }

    public void setWfSchedulingInterval(String wfSchedulingInterval) {
        this.wfSchedulingInterval = wfSchedulingInterval;
    }

    public String getWfStartDateTime() {
        return wfStartDateTime;
    }

    public void setWfStartDateTime(String wfStartDateTime) {
        this.wfStartDateTime = wfStartDateTime;
    }

    public String getWfEndDateTime() {
        return wfEndDateTime;
    }

    public void setWfEndDateTime(String wfEndDateTime) {
        this.wfEndDateTime = wfEndDateTime;
    }
    
    public String getWfHourInterval() {
        return wfHourInterval;
    }

    public void setWfHourInterval(String wfHourInterval) {
        this.wfHourInterval = wfHourInterval;
    }

    public String getWfMinuteInterval() {
        return wfMinuteInterval;
    }

    public void setWfMinuteInterval(String wfMinuteInterval) {
        this.wfMinuteInterval = wfMinuteInterval;
    }

    public Long getDelayInMillis(){
	LOGGER.info("In getDelayInMillis");
	Calendar nextCal;
	try {
	    nextCal = this.getNextScheduledStart();
	    LOGGER.info("Date/time of next scheduled start : " + nextCal.getTime()); 
	    LOGGER.info("Value of next scheduled start time in millis = ");
	    LOGGER.info(nextCal.getTimeInMillis());
	    LOGGER.info("Current time : " + nowCal.getTime());
	    LOGGER.info("Value of current time in millis = ");
	    LOGGER.info(nowCal.getTimeInMillis());

	    Long delay = nextCal.getTimeInMillis() -  nowCal.getTimeInMillis();
	    if (delay < 0L ) {
		delay = 0L;
	    }
            LOGGER.info("Delay value = " + delay);
            return delay; 
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    LOGGER.info("Delay value = " + 0L);
	    return 0L; 
	}
	 
	
	
    }
    
    public boolean isScheduleExpired(){
	LOGGER.info("In isScheduleExpired() :  "); 
	try { 
 
	Calendar expirationCal = this.getEndDateFromDateString(this.wfEndDateTime);
	if ( nowCal.compareTo(expirationCal) > 0 ) {
	    LOGGER.info("Current date and time " + nowCal.toString() + " is greater than " + expirationCal.toString() + " : not expired"); 
	    return true;
	} else {
	    LOGGER.info("Current date and time " + nowCal.toString() + " is greater than " + expirationCal.toString() + " : is expired");
	    return false; 
	    
	}
	} catch (Exception ex){
	    return true; 
	}
	
    }
    
    public boolean isFirstIteration() {
	LOGGER.info("In isFirstIteration() : task ID = " + task.getId()); 
	try {
	    boolean firstIteration = true; 
	    String activationCount = task.getVariable(TASK_ACTIVATION_COUNT); 
	    if (activationCount == null || activationCount == "") {
		task.setVariable(TASK_ACTIVATION_COUNT, Integer.toString(0)); 
		LOGGER.info("Variable " + TASK_ACTIVATION_COUNT + " empty , initializing"); 
		return false; 
	    } else {
		int iActivationCount = Integer.parseInt(activationCount);
		firstIteration =  (iActivationCount == 0 );
		iActivationCount++;
		task.setVariable(TASK_ACTIVATION_COUNT, Integer.toString(iActivationCount)); 
		return firstIteration; 
		
	    }
	} catch (Exception ex) {
	   LOGGER.error("Error from isFirstIteration",ex);
	   return false; 
	}
	
    }
    public Calendar getNextScheduledStart() throws Exception {
	try {
	LOGGER.info("In getNextScheduledStart()"); 
	
	if (this.isFirstIteration()){
	    LOGGER.info("isFirstIteration returned true:  trying this.getDateFromString(getWfStartDateTime())"); 
	    return this.getDateFromString(getWfStartDateTime()); 
	    
	}
	else if (getWfSchedulingInterval().equalsIgnoreCase(WF_SCHED_INTERVAL_INTERVAL)){
	    LOGGER.info("getWfSchedulingInterval returned WF_SCHED_INTERVAL_INTERVAL:  trying getScheduledStartNextInterval");
	    return getScheduledStartNextInterval(); 
	    
	}
	
	else if (getWfSchedulingInterval().equalsIgnoreCase(WF_SCHED_INTERVAL_HOUR)){
	    LOGGER.info("getWfSchedulingInterval returned WF_SCHED_INTERVAL_DAY:  trying getScheduledStartTomorrow");
	    return getScheduledStartNextHour(); 
	    
	}
	else if (getWfSchedulingInterval().equalsIgnoreCase(WF_SCHED_INTERVAL_DAY)){
	    LOGGER.info("getWfSchedulingInterval returned WF_SCHED_INTERVAL_DAY:  trying getScheduledStartTomorrow");
	    return getScheduledStartTomorrow(); 
	    
	} else if (getWfSchedulingInterval().equalsIgnoreCase(WF_SCHED_INTERVAL_WEEK)) {
	    LOGGER.info("getWfSchedulingInterval returned WF_SCHED_INTERVAL_WEEK:  trying getScheduledStartNextWeek");
	    return getScheduledStartNextWeek();
	    
	} else if (getWfSchedulingInterval().equalsIgnoreCase(WF_SCHED_INTERVAL_MONTH)){
	    LOGGER.info("getWfSchedulingInterval returned WF_SCHED_INTERVAL_MONTH:  trying getScheduledStartNextMonth");
	    return getScheduledStartNextMonth(); 
	} else {
	    LOGGER.error("Error in getNextScheduledStart : scheduling interval = " + getWfSchedulingInterval() + " unexpected"); 
	    return getScheduledStartTomorrow(); 
	}
	} catch (Exception ex){
	    LOGGER.error("Error from getNextScheduledStart()",ex);
	    throw ex; 
	    
	}	
    }
    

    @Override
    public String toString() {
	return "ScheduleDelayHelper [wfDayOfMonth=" + wfDayOfMonth
		+ ", wfDayOfWeek=" + wfDayOfWeek + ", wfHourOfDay="
		+ wfHourOfDay + ", wfHourInterval=" + wfHourInterval
		+ ", wfMinuteInterval=" + wfMinuteInterval
		+ ", wfSchedulingInterval=" + wfSchedulingInterval
		+ ", wfStartDateTime=" + wfStartDateTime + ", wfEndDateTime="
		+ wfEndDateTime + ", nowCal=" + nowCal + "]";
    }

    public Date getNextScheduledStartDate() throws Exception {
	Date startDate = getNextScheduledStart().getTime(); 
	LOGGER.info("getNextScheduledStartDate() returns : " + startDate);
	return startDate; 
    }
    
    /**
     * @return
     */
    public Calendar getScheduledStartNextInterval() {
	LOGGER.info("In getScheduledStartNextInterval()"); 
	Calendar tomorrowCal = (Calendar) nowCal.clone();
	tomorrowCal.add(Calendar.HOUR_OF_DAY, getHourInterval());
	tomorrowCal.add(Calendar.MINUTE,getMinuteInterval());
	tomorrowCal.set(Calendar.SECOND, 0);
	tomorrowCal.set(Calendar.MILLISECOND, 0);
	LOGGER.info("Next scheduled start : " + tomorrowCal.toString()); 
	return tomorrowCal; 
    }

    
    public Calendar getScheduledStartNextHour(){
	LOGGER.info("In getScheduledStartNextHour()"); 
	Calendar tomorrowCal = (Calendar) nowCal.clone();
	tomorrowCal.add(Calendar.HOUR_OF_DAY, getHourInterval());
	tomorrowCal.set(Calendar.MINUTE, 0);
	tomorrowCal.set(Calendar.SECOND, 0);
	tomorrowCal.set(Calendar.MILLISECOND, 0);
	LOGGER.info("Next scheduled start : " + tomorrowCal.toString()); 
	return tomorrowCal; 
    }
    
    public Calendar getScheduledStartTomorrow(){
	LOGGER.info("In getScheduledStartTomorrow()"); 
	Calendar tomorrowCal = (Calendar) nowCal.clone();
	tomorrowCal.add(Calendar.DAY_OF_YEAR, 1);
	tomorrowCal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(wfHourOfDay)); 
	tomorrowCal.set(Calendar.MINUTE, 0);
	tomorrowCal.set(Calendar.SECOND, 0);
	tomorrowCal.set(Calendar.MILLISECOND, 0);
	LOGGER.info("Next scheduled start : " + tomorrowCal.toString()); 
	return tomorrowCal; 
    }
    
    public Calendar getScheduledStartNextWeek(){
	LOGGER.info("In getScheduledStartNextWeek()");
	Calendar nextWeekCal = (Calendar) nowCal.clone();
	int dayOfWeekToday = nowCal.get(Calendar.DAY_OF_WEEK);
	int dayOfWeekScheduled = getDayOfWeekIndex(this.wfDayOfWeek); 
	// Revision 1 3/2/2105 Joe Shropshire -- only increment week if today's day-of-week is greater than or equal to the day of week on the schedule
	if (dayOfWeekToday >= dayOfWeekScheduled) {
	    nextWeekCal.add(Calendar.DAY_OF_YEAR, 7);
	} 
	//Revision 1 3/2/2015 Joe Shropshire -- set day of week and week of month
	int weekOfMonth = nextWeekCal.get(Calendar.WEEK_OF_MONTH); 
	nextWeekCal.set(Calendar.DAY_OF_WEEK, dayOfWeekScheduled);
	nextWeekCal.set(Calendar.WEEK_OF_MONTH, weekOfMonth); 
	nextWeekCal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(wfHourOfDay));
	nextWeekCal.set(Calendar.MINUTE, 0);
	nextWeekCal.set(Calendar.SECOND, 0);
	nextWeekCal.set(Calendar.MILLISECOND, 0);
	LOGGER.info("Next scheduled start : " + nextWeekCal.toString());
	return nextWeekCal; 
    }
    public Calendar getScheduledStartNextMonth(){
	LOGGER.info("In getScheduledStartNextMonth()"); 
	Calendar nextMonthCal = (Calendar) nowCal.clone();
	nextMonthCal.add(Calendar.MONTH, 1);
	// Revision 1 3/2/2015 
	//correct for variable length of months 
	if (nextMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH) < Integer.parseInt(wfDayOfMonth)){
	    nextMonthCal.set(Calendar.DAY_OF_MONTH, nextMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
	    
	} else {
	    nextMonthCal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(wfDayOfMonth)); 
	}
	nextMonthCal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(wfHourOfDay)); 
	nextMonthCal.set(Calendar.MINUTE, 0);
	nextMonthCal.set(Calendar.SECOND, 0);
	nextMonthCal.set(Calendar.MILLISECOND, 0);
	LOGGER.info("Next scheduled start : " + nextMonthCal.toString()); 
	return nextMonthCal; 
    }
    public String getNextScheduledStartAsString(){
	try { 
	String s =  getNextScheduledStart().toString(); 
	return s; 
	} catch (Exception ex){
	    LOGGER.error(ex); 
	    return new String("Unknown"); 
	}
	
    }
    
    /**
     * Parses date string and returns date
     * @param dateString :  format is YYYY-MM-DD HH:MM:SS
     *                       indexes  0123456789012345678
     *                                0000000000011111111
     * 
     * @return date corresponding to dateString
     * @throws Exception
     * @TODO: handle additional formats
     */
    public Calendar getDateFromString(String dateString) {
	LOGGER.info("In getDateFromString():  date string = " + dateString);
	Calendar cal = Calendar.getInstance(); 
	try {
	    int year = Integer.parseInt(dateString.substring(0, 4)); 
	    int month = Integer.parseInt(dateString.substring(5,7)) -1; // java.util.Calendar uses zero-based array for months 
	    int day   = Integer.parseInt(dateString.substring(8,10));
	    int hour  = Integer.parseInt(dateString.substring(11,13));
	    int minute  = Integer.parseInt(dateString.substring(14,16));	    
	    int second  = 0;
	    LOGGER.info("Setting calendar : year = " + year + " month(zero-based) = " + month + " day = " + day + " hour = " + hour + " minute = " + minute + " second = " + second);
	    cal.clear();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.DAY_OF_MONTH, day);
	    cal.set(Calendar.HOUR_OF_DAY, hour);
	    cal.set(Calendar.MINUTE, minute);
	    cal.set(Calendar.SECOND, second);
	 
	    //cal.set(year,month,day,hour,minute,second);
	    //cal.se
	       
	} catch (Exception ex){
	    LOGGER.error("Caught exception trying to turn string into date :  string = -->" + dateString + "<--.  Returning current date : "); 
	    LOGGER.error(ex);
	    
	}
	LOGGER.info("Returning date = " + cal.getTime()); 
        return cal;  		
    }
    
    @SuppressWarnings("deprecation")
    public Calendar getEndDateFromDateString(String dateString ) throws Exception {
	LOGGER.info("In getEndDateFromDateString() : date string = " + dateString ); 
	Calendar cal = Calendar.getInstance(); 
	//Date retDate = new Date(); 
	if (dateString == null || dateString.isEmpty()) {
	    
	    cal.set(2100,1,1,0,0,0); 
	    LOGGER.info("End date/time string is null or empty :  returning " + cal.getTime()); 
	    return cal;     
	    
	} else {
	    return getDateFromString(dateString); 
	    
	}	
	
    }

    public CSExternalTask getTask() {
        return task;
    }

    public void setTask(CSExternalTask task) {
        this.task = task;
    }
    
    private String getTaskVariable(String key) throws CSException, NullPointerException {
	LOGGER.info("In getTaskVariable :  key = " + key ); 
	if (this.task == null ){
	    
	    LOGGER.error("task is null"); 
	    throw new NullPointerException("ScheduleDelayHelper.getTaskVariable:  task is null"); 
	} 
	   String value = this.task.getVariable(key);
	   if (value == null ) { 
	       LOGGER.error("Key = " + key + " Is empty"); 
	       if (key.equals(WF_END_DATE_TIME_KEY)) {
		   return new String("");
	       }
	       throw new NullPointerException("Variable key = " + key + " null value "); 
	       
	       
	   }
	   return value; 
	    

	
	
	
    }
    /**
     * Gets the index into the month from the month name 
     * @param monthName
     * @return
     */
    private int getMonthIndex(String monthName){
	if (monthName.toUpperCase().contains("JAN")) return Calendar.JANUARY;
	if (monthName.toUpperCase().contains("FEB")) return Calendar.FEBRUARY;
	if (monthName.toUpperCase().contains("MAR")) return Calendar.MARCH;
	if (monthName.toUpperCase().contains("APR")) return Calendar.APRIL;
	if (monthName.toUpperCase().contains("MAY")) return Calendar.MAY;
	if (monthName.toUpperCase().contains("JUN")) return Calendar.JUNE;
	if (monthName.toUpperCase().contains("JUL")) return Calendar.JULY;
	if (monthName.toUpperCase().contains("AUG")) return Calendar.AUGUST;
	if (monthName.toUpperCase().contains("SEP")) return Calendar.SEPTEMBER;
	if (monthName.toUpperCase().contains("OCT")) return Calendar.OCTOBER;
	if (monthName.toUpperCase().contains("NOV")) return Calendar.NOVEMBER;
	if (monthName.toUpperCase().contains("DEC")) return Calendar.DECEMBER;
	return Calendar.JANUARY; 	
    }
    /**
     * Gets the index into the day of week given the week name 
     * @param dayOfWeekName
     * @return
     */
    private int getDayOfWeekIndex(String dayOfWeekName) {
	if (dayOfWeekName.toUpperCase().contains("SUN")) return Calendar.SUNDAY;
	if (dayOfWeekName.toUpperCase().contains("MON")) return Calendar.MONDAY;
	if (dayOfWeekName.toUpperCase().contains("TUE")) return Calendar.TUESDAY;
	if (dayOfWeekName.toUpperCase().contains("WED")) return Calendar.WEDNESDAY;
	if (dayOfWeekName.toUpperCase().contains("THU")) return Calendar.THURSDAY;
	if (dayOfWeekName.toUpperCase().contains("FRI")) return Calendar.FRIDAY;
	if (dayOfWeekName.toUpperCase().contains("SAT")) return Calendar.SATURDAY;
	return Calendar.SUNDAY;
	
	
	
    }
    
    private int getHourInterval(){
	
	if (wfHourInterval == null || wfHourInterval.equals("")) {
	    LOGGER.error("In getHourInterval :  hour interval null or blank, returning default = 1");
	    return 1; 
	} else {
	    try {
		Integer hourInterval = Integer.parseInt(wfHourInterval, 1); 
		LOGGER.info("In getHourInterval() : returning value = " + hourInterval);
		return hourInterval; 
	    }
	    
	    catch (Exception ex) {
		return 1; 		
	    }	    
	}
    }
    
    private int getMinuteInterval(){
	
	if (wfMinuteInterval == null || wfMinuteInterval.equals("")) {
	    LOGGER.error("In getMinuteInterval :  minute interval null or blank, returning default = 1");
	    return 1; 
	} else {
	    try {
		Integer minuteInterval = Integer.parseInt(wfMinuteInterval, 1); 
		LOGGER.info("In getMinuteInterval() : returning value = " + minuteInterval);
		return minuteInterval; 
	    }
	    
	    catch (Exception ex) {
		return 0; 		
	    }	    
	}
    }


    private String wfDayOfMonth;
    private String wfDayOfWeek;
    private String wfHourOfDay;
    private String wfHourInterval;
    private String wfMinuteInterval;
    private String wfSchedulingInterval;
    private String wfStartDateTime;
    private String wfEndDateTime;
    private Calendar nowCal; //instantiate at construction 
    private CSExternalTask task;
    private static final String TASK_ACTIVATION_COUNT = "task_activation_count"; 
    private static final String WF_SCHED_INTERVAL_KEY = "wf_scheduling_interval";
    private static final String WF_SCHED_INTERVAL_INTERVAL = "Interval";
    private static final String WF_SCHED_INTERVAL_HOUR = "Hour";
    private static final String WF_SCHED_INTERVAL_DAY = "Day";
    private static final String WF_SCHED_INTERVAL_WEEK = "Week";
    private static final String WF_SCHED_INTERVAL_MONTH = "Month";
    private static final String WF_HOUR_OF_DAY_KEY = "wf_hour_of_day";
    private static final String WF_HOUR_INTERVAL_KEY = "wf_hour_interval";
    private static final String WF_MINUTE_INTERVAL_KEY = "wf_minute_interval";
    private static final String WF_DAY_OF_WEEK_KEY = "wf_day_of_week";
    private static final String WF_DAY_OF_MONTH_KEY = "wf_day_of_month";
    private static final String WF_START_DATE_TIME_KEY = "wf_start_date_time";
    private static final String WF_END_DATE_TIME_KEY = "wf_end_date_time";
    private static final String WF_DEPLOY_RECURRENCE_KEY = "wf_deploy_recurrence";
    private static final String WF_DEPLOY_END_TYPE_KEY = "wf_deploy_end_type";
    private static final String WF_DEPLOY_END_AFTER_KEY = "wf_deploy_end_after";
    private static final Log LOGGER = LogFactory.getLog(com.hp.autonomy.proserv.pkg.ls.core.task.ScheduleDelayHelper.class);

}
