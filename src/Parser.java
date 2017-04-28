import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Parser {
	protected ArrayList<String> instructorNames, rooms, subjectNames, courseNos;
	protected ArrayList<Course> courses;
	public Parser() throws Exception{
		createCourseArray();
	}
	
	private void createCourseArray() throws Exception {
		FileReader reader = new FileReader("CoursesOffered.json");
		JSONArray courseData = (JSONArray) JSONValue.parse(reader);
		courses = new ArrayList<Course>();
		for (int i = 0; i < courseData.size(); i++) 
			courses.add(createCourse(i, courseData));
		generateListingArrays(courses);
		sort();
	}
	
	private Course createCourse(int index, JSONArray courseData) {
		ArrayList<Instructors> instructors = new ArrayList<Instructors>();
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		ArrayList<MeetingTime> meetingTimes = new ArrayList<MeetingTime>();
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		JSONObject courseObject = (JSONObject) courseData.get(index);
		JSONObject currentCourse = (JSONObject) courseObject.get("Course");
		String subjectName = currentCourse.containsKey("SubjectName") ? (String) currentCourse.get("SubjectName"): null;
		String courseNo = currentCourse.containsKey("CourseNo") ? (String) currentCourse.get("CourseNo") : null;
		String sectionNo = currentCourse.containsKey("SectionNo") ? (String) currentCourse.get("SectionNo") : null;
		JSONArray Instructors = (JSONArray) currentCourse.get("Instructors");
		createInstructorObjects(instructors, Instructors);
		JSONArray Schedule = (JSONArray) currentCourse.get("Schedule");
		createScheduleObjects(schedules, meetingTimes, rooms, Schedule);
		Course course = new Course(subjectName, courseNo, sectionNo, schedules, instructors);
		return course;
	}

	protected void generateListingArrays(ArrayList<Course> courses) {
		instructorNames = new ArrayList<String>();
		rooms = new ArrayList<String>();
		subjectNames = new ArrayList<String>();
		courseNos = new ArrayList<String>();

		for (int i = 0; i < courses.size(); i++) {
			generateInstructorList(courses, i);
			generateCourseNoList(courses, i);
			generateSubjectNameList(courses, i);
			generateRoomList(courses, i);
		}
	}
	
	private void sort() {
		Collections.sort(instructorNames);
		Collections.sort(courseNos);
		Collections.sort(subjectNames);
		Collections.sort(rooms);
	}
	
	private void createScheduleObjects(ArrayList<Schedule> schedules, ArrayList<MeetingTime> meetingTimes, ArrayList<Room> rooms, JSONArray Schedule) {
		for (int n = 0; n < Schedule.size(); n++) {
			String StartDate = (String) ((JSONObject) Schedule.get(n)).get("StartDate");
			String FinishDate = (String) ((JSONObject) Schedule.get(n)).get("FinishDate");
			JSONArray MeetingTime = (JSONArray) ((JSONObject) Schedule.get(n)).get("MeetingTime");
			for (int k = 0; k < MeetingTime.size(); k++) {
				String Day = (String) ((JSONObject) MeetingTime.get(k)).get("Day");
				String StartHour = (String) ((JSONObject) MeetingTime.get(k)).get("StartHour");
				String FinishHour = (String) ((JSONObject) MeetingTime.get(k)).get("FinishHour");
				JSONArray Room = (JSONArray) ((JSONObject) MeetingTime.get(k)).get("Room");
				for (int l = 0; l < Room.size(); l++) {
					String RoomCode = (String) ((JSONObject) Room.get(l)).get("RoomCode");
					Room room = new Room(RoomCode);
					rooms.add(room);
				}
				MeetingTime meetingTime = new MeetingTime(Day, StartHour, FinishHour, rooms);
				meetingTimes.add(meetingTime);
			}
			Schedule schedule = new Schedule(StartDate, FinishDate, meetingTimes);
			schedules.add(schedule);
		}
	}

	private void createInstructorObjects(ArrayList<Instructors> instructors, JSONArray Instructors) {
		for (int j = 0; j < Instructors.size(); j++) {
			String name = (String) ((JSONObject) Instructors.get(j)).get("Name");
			String surname = (String) ((JSONObject) Instructors.get(j)).get("Surname");
			Boolean isPrimary = (Boolean) ((JSONObject) Instructors.get(j)).get("IsPrimary");
			Instructors instructor = new Instructors(name, surname, isPrimary);
			instructors.add(instructor);
		}
	}
	
	private void generateRoomList(ArrayList<Course> courses, int i) {
		if (courses.get(i).schedules.size() > 0) {
			for (int k = 0; k < courses.get(i).schedules.get(0).MeetingTimes.size(); k++) {
				if (courses.get(i).schedules.get(0).MeetingTimes.get(k).rooms.size() > 0) {
					for(int l=0; l < courses.get(i).schedules.get(0).MeetingTimes.get(k).rooms.size(); l++){
						String roomCode = courses.get(i).schedules.get(0).MeetingTimes.get(k).rooms.get(l).RooomCode;
						if (!alreadyHas(rooms, roomCode))
							rooms.add(roomCode);
					}
				}
			}
		}
	}

	private void generateSubjectNameList(ArrayList<Course> courses, int i) {
		String subjectName = courses.get(i).getSubjectName();
		if (!alreadyHas(subjectNames , subjectName))
			subjectNames.add(subjectName);
	}

	private void generateCourseNoList(ArrayList<Course> courses, int i) {
		String courseNo = courses.get(i).getCourseNo();
		if (!alreadyHas(courseNos, courseNo))
			courseNos.add(courseNo);
	}

	private void generateInstructorList(ArrayList<Course> courses, int i) {
		if (courses.get(i).instructors.size() > 0) {
			for (int j = 0; j < courses.get(i).instructors.size(); j++) {
				String instructorNameAndSurnames = courses.get(i).instructors.get(j).Name + " "
						+ courses.get(i).instructors.get(j).Surname;
				if (!alreadyHas(instructorNames, instructorNameAndSurnames))
					instructorNames.add(instructorNameAndSurnames);
			}
		}
	}
	
	private boolean alreadyHas(ArrayList<String> variables, String key){
		for(int i=0; i < variables.size(); i++){
			if(variables.get(i).equals(key))
				return true;
		}
		return false;
	}
}
