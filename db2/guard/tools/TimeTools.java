package db2.guard.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTools
{
	private long startTime;
	private long endTime;

    public TimeTools()
    {
    }
    
    public static String getTimeStampFile(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
    	return sdf.format((new java.util.Date()));

    }
    
    public static String getTimeYearPath(){
    	String strPath=null;
    	Date oDate = (new java.util.Date());
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	strPath = sdf.format(oDate);   	
    	 return strPath;
    }
    
    public static String getTimeMonthPath(){
    	String strPath=null;
    	Date oDate = (new java.util.Date());
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	strPath = sdf.format(oDate);
    	sdf = new SimpleDateFormat("MM");
    	strPath += "\\" +sdf.format(oDate);	
    	
    	 return strPath;
    }
    
    public static String getTimeDayPath(){
    	String strPath=null;
    	Date oDate = (new java.util.Date());
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	strPath = sdf.format(oDate);
    	sdf = new SimpleDateFormat("MM");
    	strPath += "\\" +sdf.format(oDate);
    	sdf = new SimpleDateFormat("dd");
    	strPath += "\\" +sdf.format(oDate);  	
    	
    	 return strPath;
    }
    public static String getCurTimeStamp(){
    	return (new Timestamp((new java.util.Date()).getTime())).toString();
    }

	public long getStartTime()
	{
		setStartTime();
		return startTime;
	}

    public long getEndTime()
    {
    	setEndTime();
        return endTime;
    }

	public void setStartTime()
	{
		startTime = System.currentTimeMillis();
		
	}

    public void setEndTime()
    {
        endTime = System.currentTimeMillis();
        
    }

	public long getElapsedTime()
	{
		if (getStartTime() == 0 || getEndTime() == 0)
		{
			return 0;
		}
		return getEndTime() - getStartTime();
	}

}

