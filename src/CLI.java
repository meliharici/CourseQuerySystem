import java.util.InputMismatchException;
import java.util.Scanner;

public class CLI {
	private static final String MAIN_INPUT_ERROR = "Enter 1,2,3,4,5 or 6.";
	private static final String LISTING_INPUT_ERROR = "Enter 1,2,3 or 4";
	private static final String QUERY_INPUT_ERROR = "Enter 1,2,3,4,5 or 6";
	private static final String GENERAL_WRONG_INPUT_ERROR = "Wrong input";
	private static final String COURSE_SYMBOL = "C";
	private static final String STUDENT_SYMBOL = "S";
	private static final String TAKEN_COURSES_SYMBOL = "T";
	private static final String WEEKLY_PLAN_SYMBOL = "P";
	private static final String ADD_COURSE_SYMBOL = "A";
	private CourseQuerySystem querySystem;
	private Parser parser;
	private Adder adder;
	
	public CLI(CourseQuerySystem system, Parser parser, Adder adder){
		this.adder = adder;
		this.parser = parser;
		this.querySystem = system;
		printMainHelpMenu();
	}
	
	protected void start(Scanner scanner){
		int input = 0;
		try{
			input = scanner.nextInt();
			if(!isValidMainInput(input))
				System.out.println(MAIN_INPUT_ERROR);
		}catch(InputMismatchException e){
			System.out.println(GENERAL_WRONG_INPUT_ERROR);
		}
		run(scanner, input);
	}
	
	private void run(Scanner scanner, int input){
		if(input==1){
			getCourseInfo(scanner);
		}else if(input==2){
			list(scanner);
		}else if(input==3){			
			printQueryHelpMenu();
			try{
				int queryInput = scanner.nextInt();
				queryCourse(scanner, queryInput);
			}catch(InputMismatchException e){
				System.out.println(GENERAL_WRONG_INPUT_ERROR);
			}
		}else if(input==4){
			addCourseOrStudent(scanner);
		}else if(input==5){
			seeInfoAboutStudent(scanner);
		}else if(input==6){
			System.exit(0);
		}
	}

	private void seeInfoAboutStudent(Scanner scanner) {
		System.out.println("Enter T to see taken courses, P to see weekly plan, A to add a new course for a student.");
		String userInput = scanner.next();
		userInput = userInput.toUpperCase();
		if(userInput.equals(TAKEN_COURSES_SYMBOL) || userInput.equals(WEEKLY_PLAN_SYMBOL) || userInput.equals(ADD_COURSE_SYMBOL))
			adder.show(userInput, scanner);
		else
			System.out.println(GENERAL_WRONG_INPUT_ERROR);
		printMainHelpMenu();
	}

	private void addCourseOrStudent(Scanner scanner) {
		System.out.println("Enter C to add a new course. Enter S to add a new student.");
		String adderInput = scanner.next();
		adderInput = adderInput.toUpperCase();
		if(adderInput.equals(COURSE_SYMBOL) || adderInput.equals(STUDENT_SYMBOL))
			adder.add(adderInput, scanner);
		else
			System.out.println(GENERAL_WRONG_INPUT_ERROR);
		printMainHelpMenu();
	}

	private void queryCourse(Scanner scanner, int queryInput) {
		if(!isValidQueryInput(queryInput)){
			System.out.println(QUERY_INPUT_ERROR);
			run(scanner, 3);
		}else if(queryInput == 1){
			findCourseByRoom(scanner);
		}else if(queryInput == 2){
			findCourseByDay(scanner);
		}else if(queryInput == 3){
			findCourseByInstructor(scanner);
		}else if(queryInput == 4){
			findCourseByCourseNo(scanner);
		}else if(queryInput == 5){
			findCourseBySubjectName(scanner);
		}else if(queryInput == 6){
			findCourseByStartingHour(scanner);
		}
	}

	private void findCourseByStartingHour(Scanner scanner) {
		System.out.println("Enter a starting hour:");
		scanner.nextLine();
		String startingHour = scanner.nextLine();
		if(startingHour.equals("8:40"))
			startingHour = " 8:40";
		querySystem.queryByType(parser.courses, "hour", startingHour , null);
		System.out.println();
		printMainHelpMenu();
	}

	private void findCourseBySubjectName(Scanner scanner) {
		System.out.println("Enter a subject name:");
		scanner.nextLine();
		String subjectName = scanner.nextLine();
		subjectName = subjectName.toUpperCase();
		querySystem.queryByType(parser.courses, "subjectName", subjectName , null);
		System.out.println();
		printMainHelpMenu();
	}

	private void findCourseByCourseNo(Scanner scanner) {
		System.out.println("Enter course no:");
		scanner.nextLine();
		String courseNo = scanner.nextLine();
		querySystem.queryByType(parser.courses, "courseNo", courseNo , null);
		System.out.println();
		printMainHelpMenu();
	}

	private void findCourseByInstructor(Scanner scanner) {
		System.out.println("Enter instructor name:");
		scanner.nextLine();
		String name = scanner.nextLine();
		name = name.toUpperCase();
		System.out.println("Enter instructor surname:");
		String surname = scanner.nextLine();
		surname = surname.toUpperCase();
		querySystem.queryByType(parser.courses, "instructor", name , surname);
		System.out.println();
		printMainHelpMenu();
	}

	private void findCourseByDay(Scanner scanner) {
		System.out.println("Enter a day: ");
		scanner.nextLine();
		String day = scanner.nextLine();
		day = day.substring(0,1).toUpperCase() + day.substring(1);
		querySystem.queryByType(parser.courses, "day", day, null);
		System.out.println();
		printMainHelpMenu();
	}

	private void findCourseByRoom(Scanner scanner) {
		System.out.println("Enter room code: ");
		scanner.nextLine();
		String roomCode = scanner.nextLine();
		roomCode = roomCode.toUpperCase();
		querySystem.queryByType(parser.courses, "room", roomCode , null);
		System.out.println();
		printMainHelpMenu();
	}

	private void list(Scanner scanner) {
		printListMenu();
		try{
			int listInput = scanner.nextInt();
			listObjects(listInput,scanner);
		}catch(InputMismatchException e){
			System.out.println(GENERAL_WRONG_INPUT_ERROR);
		}
	}

	private void listObjects(int listInput, Scanner scanner) {
		if(!isValidListInput(listInput)){
			System.out.println(LISTING_INPUT_ERROR);
			run(scanner,2);
		}else if(listInput == 1){
			printInstructors();
		}else if(listInput == 2){
			printRooms();
		}else if(listInput == 3){
			printSubjectNames();
		}else if(listInput == 4){
			printCourseNos();
		}
	}

	protected void printCourseNos() {
		for(int i=0; i < parser.courseNos.size(); i++)
			System.out.println(parser.courseNos.get(i));
		adder.printNewCourseNos();
		System.out.println();
		printMainHelpMenu();
	}

	private void printSubjectNames() {
		for(int i=0; i < parser.subjectNames.size(); i++)
			System.out.println(parser.subjectNames.get(i));
		adder.printNewCourseSubjects();
		System.out.println();
		printMainHelpMenu();
	}

	private void printRooms() {
		for(int i=0; i < parser.rooms.size(); i++)
			System.out.println(parser.rooms.get(i));
		System.out.println();
		printMainHelpMenu();
	}

	private void printInstructors() {
		for(int i=0; i < parser.instructorNames.size(); i++)
			System.out.println(parser.instructorNames.get(i));
		System.out.println();
		printMainHelpMenu();
	}

	private void getCourseInfo(Scanner scanner) {
		System.out.println("Please enter course subject name:");
		String subjectName = scanner.next();
		subjectName = subjectName.toUpperCase();
		System.out.println("Please enter course no for " + subjectName);
		String courseNo = scanner.next();
		querySystem.getSpecificInfoAboutCourse(parser.courses, subjectName, courseNo);
		adder.giveInfoAboutNewCourse(subjectName, courseNo);
		System.out.println();
		printMainHelpMenu();
	}
	
	private boolean isValidListInput(int input){
		if(input==1 || input==2 || input==3 || input==4)
			return true;
		return false;
	}
	
	private boolean isValidMainInput(int input){
		if(input==1 || input==2 || input==3 || input==4 || input==5 || input==6)
			return true;
		return false;
	}
	
	private boolean isValidQueryInput(int input){
		if(input==1 || input==2 || input==3 || input==4 || input==5 || input==6)
			return true;
		return false;
	}
	
	private void printMainHelpMenu(){
		String endline = "\n";
		String helpMenu = "Enter (1) to get specific information about a course" 				 + endline + 
						  "Enter (2) to list courses' subjects/Nos/rooms/instructors" 			 + endline +
						  "Enter (3) to query courses." 										 + endline +
						  "Enter (4) to add course or student."									 + endline +
						  "Enter (5) to see taken courses or weekly plan of a student or add a new course to a student."	+ endline +
						  "Enter (6) to exit.";
		System.out.println(helpMenu);
	}
	
	private void printListMenu(){
		String endline = "\n";
		String listMenu = "Enter (1) to list Instructors"  + endline +
						  "Enter (2) to list Rooms"		   + endline +
						  "Enter (3) to list Subject Names"+ endline +
						  "Enter (4) to list Course Nos" ;
		System.out.println(listMenu);
	}
	
	private void printQueryHelpMenu(){
		String endline = "\n";
		String queryHelp =  "Enter (1) to query courses specific to a room" 		+ endline + 
							"Enter (2) to query courses specific to a day"  		+ endline +
							"Enter (3) to query courses specific to an instructor"  + endline +
							"Enter (4) to query courses specific to a course no"	+ endline +
							"Enter (5) to query courses specific to a subject name" + endline +
							"Enter (6) to query courses specific to an hour" 		;
		System.out.println(queryHelp);
	}
}
