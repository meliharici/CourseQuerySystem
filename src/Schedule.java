import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Schedule {
	protected String StartDate, FinishDate;
	protected ArrayList<MeetingTime> MeetingTimes;
	
	public Schedule(String StartDate, String FinishDate, ArrayList<MeetingTime>MeetingTimes){
		this.StartDate = StartDate;
		this.FinishDate = FinishDate;
		this.MeetingTimes = MeetingTimes;
		if(StartDate.contains("Date") || FinishDate.contains("Date"))
			convertStringToDate();
	}
	
	private void convertStringToDate(){
		String startdate = StartDate.substring(6, StartDate.length()-2);
		long epochTime = Long.valueOf(startdate).longValue();
		Date startDate = new Date(epochTime);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String formatted = format.format(startDate);
		StartDate = formatted;
		String finishdate = FinishDate.substring(6, FinishDate.length()-2);
		long epochTime2 = Long.valueOf(finishdate).longValue();
		Date finishDate = new Date(epochTime2);
		DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		String formatted2 = format2.format(finishDate);
		FinishDate = formatted2;
	}
}
