import java.util.ArrayList;

public class MeetingTime {
	protected String Day, StartHour, FinishHour;
	protected ArrayList<Room> rooms;
	
	public MeetingTime(String Day, String StartHour, String Finishour, ArrayList<Room> rooms){
		this.Day = Day;
		this.StartHour = StartHour;
		this.FinishHour = Finishour;
		this.rooms = rooms;
	}
}
