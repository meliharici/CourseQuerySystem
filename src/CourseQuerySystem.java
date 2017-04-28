import java.util.ArrayList;
import java.util.Collections;

public class CourseQuerySystem {
	private static final String SUBJECT_KEYWORD = "subjectName";
	private static final String COURSE_NO_KEYWORD = "courseNo";
	private static final String ROOM_KEYWORD = "room";
	private static final String DAY_KEYWORD = "day";
	private static final String INSTRUCTOR_KEYWORD = "instructor";
	private static final String HOUR_KEYWORD = "hour";
	private Adder adder;
	protected ArrayList<Student> students;
	
	public CourseQuerySystem() {
		students = new ArrayList<Student>();
	}

	protected void giveStudentList(int i, Student std, int size){
		for (int j = 0; j < size; j++) {
			students.add(std);
		}
	}
	
	protected void getSpecificInfoAboutCourse(ArrayList<Course> courses, String SubjectName, String CourseNo) {
		String courseRooms = "";
		String courseDay = "";
		String hour = "";
		String instructorNameAndSurname = "";
		String date = "";
		Course c = null;
		for(int i=0; i < courses.size(); i++){
			if(courses.get(i).getSubjectName().equals(SubjectName) && courses.get(i).getCourseNo().equals(CourseNo)){
				c = courses.get(i);
				if(courses.get(i).schedules.size() > 0 && courses.get(i).schedules.get(0).MeetingTimes.size() > 0){
					date = courseDate(courses, date, i);
					for(int j = 0; j < courses.get(i).schedules.get(0).MeetingTimes.size(); j++){
						courseDay = courseDay(courses, courseDay, i, j);
						hour = hour(courses, hour, i, j);
						courseRooms = courseRooms(courses, courseRooms, i, j);
				}
			}
			instructorNameAndSurname = instructorNameAndSurname(courses, instructorNameAndSurname, i);
			}
		}
		printAllCourseInfo(courses, SubjectName, CourseNo, courseRooms, courseDay, hour, instructorNameAndSurname, date);
		printStudentsTakenCourse(c);
	}
	
	private void printStudentsTakenCourse(Course c){
		System.out.println("Students that takes this course:");
		for (int i = 0; i < students.size(); i++) {
			if(students.get(i).coursesTaken.size() > 0){
				for (int j = 0; j < students.get(i).coursesTaken.size(); j++) {
					String subjName = students.get(i).coursesTaken.get(j).getSubjectName();
					String coursNo = students.get(i).coursesTaken.get(j).getCourseNo();
					if(c.getSubjectName().equals(subjName) && c.getCourseNo().equals(coursNo)){
						System.out.println(students.get(i).getStudentID());
					}
				}
			}
		}
	}
	

	private String courseDate(ArrayList<Course> courses, String date, int i) {
		String courseDate = courses.get(i).schedules.get(0).StartDate + " - " + courses.get(i).schedules.get(0).FinishDate + " |";
		if(!date.contains(courseDate))
			date = date + " | " + courseDate;
		return date;
	}

	private String instructorNameAndSurname(ArrayList<Course> courses, String instructorNameAndSurname, int i) {
		if(courses.get(i).instructors.size() > 0){
			for(int w = 0; w < courses.get(i).instructors.size(); w++){
				String courseInstructor = courses.get(i).instructors.get(w).Name + " " + courses.get(i).instructors.get(w).Surname;
				if(!instructorNameAndSurname.contains(courseInstructor))
					instructorNameAndSurname = instructorNameAndSurname + "  | " + courseInstructor;
			}
		}
		return instructorNameAndSurname;
	}

	private void printAllCourseInfo(ArrayList<Course> courses, String SubjectName, String CourseNo, String courseRooms,
			String courseDay, String hour, String instructorNameAndSurname, String date)  {
		boolean hasSuchAcourse = hasSuchCourse(courses, SubjectName, CourseNo);
		if(hasSuchAcourse){
			String endline = "\n";
			String information = "Course Subject Name is: " + SubjectName + endline +
								 "Course No is: "			+ CourseNo    + endline +
								 "Course Room is: "			+ courseRooms + endline +
								 "Course Day  is: "			+ courseDay   + endline +
								 "Course Instructor is: "   + instructorNameAndSurname + endline + 
								 "Course hour is: "			+ hour 		  + endline +  
								 "Course date is: "         + date ;
			System.out.println(information);
		}
	}

	private boolean hasSuchCourse(ArrayList<Course> courses, String SubjectName, String CourseNo) {
		boolean hasSuchAcourse = false;
		for(int z=0; z < courses.size(); z++){
			if(courses.get(z).getSubjectName().equals(SubjectName) && courses.get(z).getCourseNo().equals(CourseNo))
				hasSuchAcourse = true;
		}
		return hasSuchAcourse;
	}

	private String courseRooms(ArrayList<Course> courses, String courseRooms, int i, int j) {
		if (courses.get(i).schedules.get(0).MeetingTimes.get(j).rooms.size() > 0) {
			for(int l=0; l < courses.get(i).schedules.get(0).MeetingTimes.get(j).rooms.size(); l++){
				String roomCode = courses.get(i).schedules.get(0).MeetingTimes.get(j).rooms.get(l).RooomCode;
				if(!courseRooms.contains(roomCode))
					courseRooms = courseRooms + "  | " + roomCode;
			}
		}
		return courseRooms;
	}

	private String hour(ArrayList<Course> courses, String hour, int i, int j) {
		String timeStap = courses.get(i).schedules.get(0).MeetingTimes.get(j).StartHour + " - " +
			courses.get(i).schedules.get(0).MeetingTimes.get(j).FinishHour;
		if(!hour.contains(timeStap)){
			hour = hour + "  | " + timeStap; ;
		}
		return hour;
	}

	private String courseDay(ArrayList<Course> courses, String courseDay, int i, int j) {
		String dayOfCourse = courses.get(i).schedules.get(0).MeetingTimes.get(j).Day;
		if(!courseDay.contains(dayOfCourse))
			courseDay = courseDay + "  | " + dayOfCourse;
		return courseDay;
	}

	protected void queryByType(ArrayList<Course> courses, String type, String name , String extra){
		if(type.equals(SUBJECT_KEYWORD))
			queryBySubjectName(courses, name);
		else if(type.equals(COURSE_NO_KEYWORD))
			queryByCourseNo(courses,name);
		else if(type.equals(INSTRUCTOR_KEYWORD))
			queryByInstructors(courses, name, extra);
		else if(type.equals(DAY_KEYWORD))
			queryByDay(courses, name);
		else if(type.equals(ROOM_KEYWORD))
			queryByRoom(courses, name);
		else if(type.equals(HOUR_KEYWORD))
			queryByStartTime(courses, name);
	}
	
	private void queryBySubjectName(ArrayList<Course> courses, String subjectName){
		ArrayList<String> queriedCourses = new ArrayList<String>();
		for(int i=0; i < courses.size(); i++){
			if(courses.get(i).getSubjectName().equals(subjectName)){
				String name = courses.get(i).getSubjectName() + " " + courses.get(i).getCourseNo();
				if(!alreadyHas(queriedCourses, name)){
					queriedCourses.add(name);
				}
			}
		}
		printQueriedCourses(SUBJECT_KEYWORD,queriedCourses);
	}
	
	private void queryByCourseNo(ArrayList<Course> courses, String courseNo){
		ArrayList<String> queriedCourses = new ArrayList<String>();
		for(int i=0; i < courses.size(); i++){
			if(courses.get(i).getCourseNo().equals(courseNo)){
				String name = courses.get(i).getSubjectName() + " " + courses.get(i).getCourseNo();
				if(!alreadyHas(queriedCourses, name)){
					queriedCourses.add(name);
				}
			}
		}
		printQueriedCourses(COURSE_NO_KEYWORD,queriedCourses);
	}
	
	private void queryByInstructors(ArrayList<Course> courses, String name, String surname){
		ArrayList<String> queriedCourses = new ArrayList<String>();
		for(int i=0; i < courses.size(); i++){
			if (courses.get(i).instructors.size() > 0) {
				for (int j = 0; j < courses.get(i).instructors.size(); j++) {
					if(courses.get(i).instructors.get(j).Name.equals(name) &&  courses.get(i).instructors.get(j).Surname.equals(surname)){
						String courseName = courses.get(i).getSubjectName() + " " + courses.get(i).getCourseNo();
						if(!alreadyHas(queriedCourses, courseName)){
							queriedCourses.add(courseName);
						}
					}
				}
			}
		}
		printQueriedCourses(INSTRUCTOR_KEYWORD,queriedCourses);
	}
	
	private void queryByDay(ArrayList<Course> courses, String day){
		ArrayList<String> queriedCourses = new ArrayList<String>();
		for(int i=0; i < courses.size(); i++){
			if(courses.get(i).schedules.size() > 0 && courses.get(i).schedules.get(0).MeetingTimes.size() > 0){
				for (int j = 0; j < courses.get(i).schedules.get(0).MeetingTimes.size(); j++) {
					if(courses.get(i).schedules.get(0).MeetingTimes.get(j).Day.equals(day)){
						String courseName = courses.get(i).getSubjectName() + " " + courses.get(i).getCourseNo();
						if(!alreadyHas(queriedCourses, courseName)){
							queriedCourses.add(courseName);
						}
					}
					
				}
			}
		}
		printQueriedCourses(DAY_KEYWORD,queriedCourses);
	}
	
	private void queryByRoom(ArrayList<Course> courses, String roomCode){
		ArrayList<String> queriedCourses = new ArrayList<String>();
		for(int i=0; i < courses.size(); i++){
			if(courses.get(i).schedules.size() > 0 && courses.get(i).schedules.get(0).MeetingTimes.size() > 0){
				for(int j=0; j < courses.get(i).schedules.get(0).MeetingTimes.size(); j++){
					if(courses.get(i).schedules.get(0).MeetingTimes.get(j).rooms.size() > 0){
						for(int k=0; k <courses.get(i).schedules.get(0).MeetingTimes.get(j).rooms.size(); k++){
							if(courses.get(i).schedules.get(0).MeetingTimes.get(j).rooms.get(k).RooomCode.equals(roomCode)){
								String courseName = courses.get(i).getSubjectName() + " " + courses.get(i).getCourseNo();
								if(!alreadyHas(queriedCourses, courseName)){
									queriedCourses.add(courseName);
								}
							}
						}
					}
				}
			}
		}
		printQueriedCourses(ROOM_KEYWORD,queriedCourses);
	}
	
	private void queryByStartTime(ArrayList<Course> courses, String startTime){
		ArrayList<String> queriedCourses = new ArrayList<String>();
		for(int i=0; i < courses.size(); i++){
			if(courses.get(i).schedules.size() > 0 && courses.get(i).schedules.get(0).MeetingTimes.size() > 0){
				for(int j=0; j < courses.get(i).schedules.get(0).MeetingTimes.size(); j++){
					if(courses.get(i).schedules.get(0).MeetingTimes.get(j).StartHour.equals(startTime)){
						String courseName = courses.get(i).getSubjectName() + " " + courses.get(i).getCourseNo();
						if(!alreadyHas(queriedCourses, courseName)){
							queriedCourses.add(courseName);
						}
					}
				}
			}
		}
		printQueriedCourses(HOUR_KEYWORD,queriedCourses);
	}
	
	private void printQueriedCourses(String type, ArrayList<String> queriedCourses){
		Collections.sort(queriedCourses);
		for(int j=0; j < queriedCourses.size(); j++){
			System.out.println(queriedCourses.get(j));
		}
		if(queriedCourses.isEmpty())
			System.out.println("Cannot find such a " + type + ".");
	}

	private boolean alreadyHas(ArrayList<String> variables, String key){
		for(int i=0; i < variables.size(); i++){
			if(variables.get(i).equals(key))
				return true;
		}
		return false;
	}
}
