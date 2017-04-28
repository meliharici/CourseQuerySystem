import java.util.ArrayList;

public class Student {
	private String fullName;
	private String id;
	private ArrayList<Course> selectedCourses;
	protected ArrayList<Course> coursesTaken;
	private ArrayList<String> possibleSections;
	protected boolean isCourseValid;
	protected boolean isSectionValid;
	protected boolean isCourseAdded;
	private Parser parser;
	ArrayList<Course> mondays, tuesdays, wednesdays, thursdays, fridays, saturdays, sundays;
	
	public Student(String fullName, String id) throws Exception{
		this.fullName = fullName;
		this.id = id;
		parser = new Parser();
		coursesTaken = new ArrayList<Course>();
	}
	
	public String getFullName(){
		return fullName;
	}
	
	public String getStudentID(){
		return id;
	}
	
	private boolean isTimeSlotOccupiedAlready(String day, String startingHour, String finishHour){
		boolean isFilled = false;
		for (int i = 0; i < coursesTaken.size(); i++) {
			if(coursesTaken.get(i).schedules.size() > 0 && coursesTaken.get(i).schedules.get(0).MeetingTimes.size() > 0){
				for (int k = 0; k < coursesTaken.get(i).schedules.get(0).MeetingTimes.size(); k++) {
					MeetingTime meetingTime = coursesTaken.get(i).schedules.get(0).MeetingTimes.get(k);
					if(meetingTime.Day.equals(day) && meetingTime.StartHour.equals(startingHour) && meetingTime.FinishHour.equals(finishHour))
						isFilled = true;
				}
			}
		}
		return isFilled;
	}
	
	protected void listDesiredCourses(String subjectName, String courseNo){
		selectedCourses = new ArrayList<Course>();
		possibleSections = new ArrayList<String>();
		for(int i=0; i < parser.courses.size(); i++){
			if(parser.courses.get(i).getSubjectName().equals(subjectName) && parser.courses.get(i).getCourseNo().equals(courseNo))
				selectedCourses.add(parser.courses.get(i));
		}
		if(selectedCourses.isEmpty()){
			System.out.println("Sorry system cannot find " + subjectName + " " + courseNo);
			isCourseValid = false;
		}else{
			isCourseValid = true;
			printSuitableSections(subjectName, courseNo);
		}
	}

	private void printSuitableSections(String subjectName, String courseNo) {
		System.out.println("You have " + selectedCourses.size() + " section(s) opportunity for this course.");
		System.out.println("Details of these sections listing below: ");
		System.out.println();
		for(int j=0; j < selectedCourses.size(); j++){
			System.out.println(subjectName + " " + courseNo + "(" + selectedCourses.get(j).getSectionNo() + ") --- Course Details:" );
			possibleSections.add(selectedCourses.get(j).getSectionNo());
			System.out.println("Weekly Schedule: ");
			if(selectedCourses.get(j).schedules.size() > 0 && selectedCourses.get(j).schedules.get(0).MeetingTimes.size() > 0){
				for (int k = 0; k < selectedCourses.get(j).schedules.get(0).MeetingTimes.size(); k++) {
					MeetingTime meetingTime = selectedCourses.get(j).schedules.get(0).MeetingTimes.get(k);
					System.out.println("Day: " + meetingTime.Day + " " + meetingTime.StartHour + " - " + meetingTime.FinishHour);
				}
			}
			System.out.println();
		}
	}
	
	protected void addSelectedCourse(String subjectName, String courseNo, String section){
		boolean isSectionFound = false;
		for (int i = 0; i < possibleSections.size(); i++) {
			if(possibleSections.get(i).equals(section))
				isSectionFound = true;
		}
		if(isSectionFound){
			boolean isCourseSuitable = true;
			Course desiredCourse = null;
			for (int j = 0; j < parser.courses.size(); j++) {
				if(parser.courses.get(j).getSubjectName().equals(subjectName) && parser.courses.get(j).getCourseNo().equals(courseNo) && 
				   parser.courses.get(j).getSectionNo().equals(section)){
				   desiredCourse = parser.courses.get(j);
				   isCourseSuitable = checkTimeSlot(isCourseSuitable, desiredCourse, j);
				}
			}
			addSuitableCourse(isCourseSuitable, desiredCourse);
		}else{
			isSectionValid = false;
			System.out.println("This course does not have section " + section);
			isCourseAdded = false;
		}
	}

	private void addSuitableCourse(boolean isCourseSuitable, Course desiredCourse) {
		if(isCourseSuitable){
			coursesTaken.add(desiredCourse);
			isCourseAdded = true;
			System.out.println("Course added!");
		}else{
			System.out.println("Time slot is not empty for this course");
			isCourseAdded = false;
		}
	}

	private boolean checkTimeSlot(boolean isCourseSuitable, Course desiredCourse, int j) {
		if(parser.courses.get(j).schedules.size() > 0 && parser.courses.get(j).schedules.get(0).MeetingTimes.size() > 0){
			for (int k = 0; k < parser.courses.get(j).schedules.get(0).MeetingTimes.size(); k++) {
				String desiredDay  = desiredCourse.schedules.get(0).MeetingTimes.get(k).Day;
				String desiredStartingHour = desiredCourse.schedules.get(0).MeetingTimes.get(k).StartHour;
				String desiredFinishHour = desiredCourse.schedules.get(0).MeetingTimes.get(k).FinishHour;
				if(isTimeSlotOccupiedAlready(desiredDay, desiredStartingHour, desiredFinishHour)){
					isCourseSuitable = false;
				}
			}
		}
		return isCourseSuitable;
	}
	
	protected void createWeeklyPlan(){
		initializeDayArrays();
		for (int i = 0; i < coursesTaken.size(); i++) {
			if(coursesTaken.get(i).schedules.size() > 0 && coursesTaken.get(i).schedules.get(0).MeetingTimes.size() > 0){
				for(int j=0; j < coursesTaken.get(i).schedules.get(0).MeetingTimes.size(); j++){
					MeetingTime meetingTime = coursesTaken.get(i).schedules.get(0).MeetingTimes.get(j);
					categorizeDayByDay(i, meetingTime);
				}
			}
		}
		printWeeklyPlan(mondays, tuesdays, wednesdays, thursdays, fridays, saturdays, sundays);
	}

	private void categorizeDayByDay(int i, MeetingTime meetingTime) {
		if(meetingTime.Day.equals("Monday"))
			mondays.add(coursesTaken.get(i));
		else if(meetingTime.Day.equals("Tuesday"))
			tuesdays.add(coursesTaken.get(i));
		else if(meetingTime.Day.equals("Wednesday"))
			wednesdays.add(coursesTaken.get(i));
		else if(meetingTime.Day.equals("Thursday"))
			thursdays.add(coursesTaken.get(i));
		else if(meetingTime.Day.equals("Friday"))
			fridays.add(coursesTaken.get(i));
		else if(meetingTime.Day.equals("Saturday"))
			saturdays.add(coursesTaken.get(i));
		else if(meetingTime.Day.equals("Sunday"))
			sundays.add(coursesTaken.get(i));
	}

	private void initializeDayArrays() {
		mondays = new ArrayList<Course>();
		tuesdays = new ArrayList<Course>();
		wednesdays = new ArrayList<Course>();
		thursdays = new ArrayList<Course>();
		fridays = new ArrayList<Course>();
		saturdays = new ArrayList<Course>();
		sundays = new ArrayList<Course>();
	}
	
	private void printWeeklyPlan(ArrayList<Course> mondays, ArrayList<Course> tuesdays, ArrayList<Course> wednesdays, 
			ArrayList<Course> thursdays, ArrayList<Course>  fridays, ArrayList<Course> saturdays, ArrayList<Course>  sundays){
		printDayPlan("Monday"   , mondays);
		printDayPlan("Tuesday"  , tuesdays);
		printDayPlan("Wednesday", wednesdays);
		printDayPlan("Thursday" , thursdays);
		printDayPlan("Friday"   , fridays);
		printDayPlan("Saturday" , saturdays);
		printDayPlan("Sunday"   , sundays);
	}

	private void printDayPlan(String day, ArrayList<Course> coursesOfday){
		System.out.println("----------------------------");
		System.out.println(day);
		for (int i = 0; i < coursesOfday.size(); i++) {
			System.out.println(coursesOfday.get(i).getSubjectName() + " " + coursesOfday.get(i).getCourseNo() + " " + coursesOfday.get(i).getSectionNo());
			if(coursesOfday.get(i).schedules.size() > 0 && coursesOfday.get(i).schedules.get(0).MeetingTimes.size() > 0){
				for(int j=0; j < coursesOfday.get(i).schedules.get(0).MeetingTimes.size(); j++){
					MeetingTime meetingTime = coursesOfday.get(i).schedules.get(0).MeetingTimes.get(j);
					if(meetingTime.Day.equals(day))
						System.out.println(meetingTime.StartHour + " - " + meetingTime.FinishHour);
				}
			}
		}
		System.out.println();
	}
}
