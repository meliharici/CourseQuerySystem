import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Adder {
	private Parser parser;
	private CourseQuerySystem cs;
	private ArrayList<Course> addedCourses;
	private ArrayList<Student> addedStudents;
	private String startDate, finishDate;
	private ArrayList<Student> takers;
	private static final String COURSE_SYMBOL = "C";
	private static final String STUDENT_SYMBOL = "S";
	private static final String TAKEN_COURSES_SYMBOL = "T";
	private static final String WEEKLY_PLAN_SYMBOL = "P";
	private static final String ADD_COURSE_SYMBOL = "A";
	
	public Adder() throws Exception{
		parser = new Parser();
		cs = new CourseQuerySystem();
		addedCourses = new ArrayList<Course>();
		addedStudents = new ArrayList<Student>();
	}
	
	protected void add(String type, Scanner scanner){
		if(type.equals(COURSE_SYMBOL)){
			Course newCourse = addedCourse(scanner);
			parser.courses.add(newCourse);
			addedCourses.add(newCourse);
			sendStudentListToSystem();
		}
		else if(type.equals(STUDENT_SYMBOL)){
			Student newStudent = addedStudent(scanner);
			addedStudents.add(newStudent);
		}
		else{
			System.out.println("Cannot add such element.");
		}
	}
	
	protected void sendStudentListToSystem(){
		for(int i=0; i < addedStudents.size(); i++){
			Student std = addedStudents.get(i);
			cs.giveStudentList(i,std,addedStudents.size());
		}
	}
	
	protected void printStudentListTakes(Course c){
		takers = new ArrayList<Student>();
		for (int i = 0; i < addedStudents.size() ; i++) {
			Student student = addedStudents.get(i);
			for (int j = 0; j < student.coursesTaken.size(); j++) {
				Course course = student.coursesTaken.get(j);
				String subjName = course.getSubjectName();
				String coursNo  = course.getCourseNo();
				if(subjName.equals(c.getSubjectName()) && coursNo.equals(c.getCourseNo())){
					takers.add(student);
				}
			}			
		}
		for(int k = 0; k < takers.size(); k++)
			System.out.println(takers.get(k).getStudentID());
	}
	

	private Student addedStudent(Scanner scanner){
		String fullName, id;
		int size = 0;
		System.out.println("Enter student's full name:");
		scanner.nextLine();
		fullName = scanner.nextLine();
		System.out.println("Enter student ID:");
		id = scanner.nextLine();
		Student student = null;
		try {
			student = new Student(fullName, id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		size = numberOfCourse(scanner, size);
		if(size > 0){
			for (int i = 0; i < size; i++) 
				addCourseToStudent(scanner, student, i);
		}
		return student;
	}

	private int numberOfCourse(Scanner scanner, int size) {
		try {
			System.out.println("How many courses do you want to add ?");
			size = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Plese enter an integer.");
			addedCourse(scanner);
		}
		return size;
	}

	private void addCourseToStudent(Scanner scanner, Student student, int i) {
		System.out.println("Enter subject name for course  #" + (i+1));
		scanner.nextLine();
		String subjectName  = scanner.next();
		subjectName = subjectName.toUpperCase();
		System.out.println("Enter course no for course  #" + (i+1));
		String courseNo = scanner.next();
		student.listDesiredCourses(subjectName, courseNo);
		if(student.isCourseValid){
			String section = section(scanner);
			student.addSelectedCourse(subjectName, courseNo, section);
			if(!student.isCourseAdded){
				System.out.println("Course cannot added. Try again.");
				addCourseToStudent(scanner, student, i);
			}
		}
		else{
			System.out.println("Course is not valid. Try Again");
			addCourseToStudent(scanner, student, i);
		}
	}

	private String section(Scanner scanner) {
		System.out.println("Choose a section for this course");
		scanner.nextLine();
		String section = scanner.next();
		section = section.toUpperCase();
		return section;
	}
	
	protected void giveInfoAboutNewCourse(String subjectName, String courseNo) {
		for(int i=0; i < addedCourses.size(); i++){
			if(addedCourses.get(i).getSubjectName().equals(subjectName) && addedCourses.get(i).getCourseNo().equals(courseNo)){
				printStudentListTakes(addedCourses.get(i));
				cs.getSpecificInfoAboutCourse(parser.courses, subjectName, courseNo);
			}
		}
	}

	private Course addedCourse(Scanner scanner){
		ArrayList<Instructors> instructors = new ArrayList<Instructors>();
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		System.out.println("Enter subject name:");
		scanner.nextLine();
		String subjectName = scanner.nextLine();
		parser.subjectNames.add(subjectName);
		System.out.println("Enter course no:");
		String courseNo = scanner.nextLine();
		parser.courseNos.add(courseNo);
		System.out.println("Enter a section no:");
		String sectionNo = scanner.nextLine();
		int size = 0;
		size = instructorSize(scanner, size);
		createInstructorList(size, scanner, instructors);
		createSchedule(scanner, schedules);
		Course course = new Course(subjectName, courseNo, sectionNo, schedules, instructors);
		return course;
	}

	private int instructorSize(Scanner scanner, int size) {
		try {
			System.out.println("How many instructors does this course have ?");
			size = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Plese enter an integer.");
			addedCourse(scanner);
		}
		return size;
	}
	
	private void createInstructorList(int size, Scanner scanner, ArrayList<Instructors> instructors){
		if(size > 0){
			Instructors instructor;
			for(int i=0; i < size ; i++){
				System.out.println("Enter name for instructor  #" + (i+1));
				scanner.nextLine();
				String name  = scanner.nextLine();
				name = name.toUpperCase();
				System.out.println("Enter surname for instructor  #" + (i+1));
				String surname = scanner.next();
				surname = surname.toUpperCase();
				instructor = new Instructors(name,surname,true);
				instructors.add(instructor);
			}
		}
		else{
			System.out.println("Number of instructor must be greater than zero.");
		}
	}
	
	private boolean isDateValid(String input){
		boolean isValid = false;
	    if(input.length()==10){
			if(input.charAt(2) == '/' && input.charAt(5) == '/' ){
				for(int i=0; i < 10; i++){
					if(i != 2 && i != 5) {
						if(input.charAt(i) >= '0' && input.charAt(i) <= '9'){
							isValid = true;
						}	
					}
				}
			}
	    }
	    return isValid;
	}

	private void inputDate(Scanner scanner , String type, ArrayList<Schedule> schedules){
		System.out.println("Enter a " + type + " date for this course: (Format: dd/MM/yyyy)");
		String input = "";
		scanner.nextLine();
		input = scanner.next();
		String inputDate = input;
		if(isDateValid(inputDate)){
			if(type.equals("start date"))
				startDate = inputDate;
			else
				finishDate = inputDate;
		}
		else{
			System.out.println("Wrong date input.");
			inputDate(scanner, type, schedules);
		}
	}

	private void createSchedule(Scanner scanner , ArrayList<Schedule> schedules){	
		int size = 0;
		Schedule schedule = null;
		ArrayList<MeetingTime> times = new ArrayList();
		inputDate(scanner, "start date" ,schedules);
		inputDate(scanner, "finish date", schedules);
		try {
			System.out.println("How many meeting time you consider for this course ?");
			size = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Plese enter an integer.");
			createSchedule(scanner, schedules);
		}
		if(size > 0){
			for(int i=0; i < size; i++){
				MeetingTime time = createMeetingTimes(scanner, i);
				times.add(time);
			}
		}
		else{
			System.out.println("Number of meeting time must be greater than zero.");
		}
		schedule = new Schedule(startDate, finishDate, times);
		schedules.add(schedule);
	}

	private MeetingTime createMeetingTimes(Scanner scanner, int i) {
		System.out.println("Enter a day for meeting time_" + (i+1));
		scanner.nextLine();
		String day = scanner.nextLine();
		System.out.println("Enter a start hour:");
		String startHour = scanner.nextLine();
		System.out.println("Enter a finish hour:");
		String finishHour = scanner.nextLine();
		System.out.println("Enter a room code:");
		String roomCode = scanner.nextLine();
		ArrayList<Room> rooms = new ArrayList();
		Room room = new Room(roomCode);
		rooms.add(room);
		MeetingTime time = new MeetingTime(day, startHour, finishHour, rooms);
		return time;
	}
	
	protected void printNewCourseSubjects(){
		for(int i=0; i < addedCourses.size(); i++){
			System.out.println(addedCourses.get(i).getSubjectName());
		}
	}
	
	protected void printNewCourseNos(){
		for(int i=0; i < addedCourses.size(); i++){
			System.out.println(addedCourses.get(i).getCourseNo());
		}
	}
	
	protected void show(String type, Scanner scanner){
		boolean isSuchStudent = false;
		Student student = null;
		String id;
		System.out.println("Enter ID of student that you want to see taken courses.");
		id = scanner.next();
		for(int j=0; j < addedStudents.size(); j++){
			if(addedStudents.get(j).getStudentID().equals(id)){
				System.out.println("Student is found.");
				isSuchStudent = true;
				student = addedStudents.get(j);
			}
		}
		if(isSuchStudent){
			if(type.equals(TAKEN_COURSES_SYMBOL))
				printTakenCoursesOfStudent(student, id);
			else if(type.equals(WEEKLY_PLAN_SYMBOL))
				student.createWeeklyPlan();
			else if(type.equals(ADD_COURSE_SYMBOL))
				addCourseToStudent(scanner, student, 0);
		}else{
			System.out.println("Sorry, system doesn't have this student.");
		}
	}

	private void printTakenCoursesOfStudent(Student student, String id) {
		System.out.println("Taken courses for " + id + " :");
		for(int i=0; i < student.coursesTaken.size(); i++){
			System.out.println(student.coursesTaken.get(i).getSubjectName() + " " + student.coursesTaken.get(i).getCourseNo() + " " + 
							   student.coursesTaken.get(i).getSectionNo());
		}
	}
}