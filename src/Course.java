import java.util.ArrayList;

public class Course {
	private String subjectName,courseNo, sectionNo;
	protected ArrayList<Schedule> schedules;
	protected ArrayList<Instructors> instructors;
	protected ArrayList<Student> students;
	private CourseQuerySystem cs;

	public Course (String subjectName, String courseNo, String sectionNo, ArrayList<Schedule> schedules, ArrayList<Instructors> instructors){
		this.subjectName = subjectName;
		this.courseNo = courseNo;
		this.sectionNo = sectionNo;
		this.schedules = schedules;
		this.instructors = instructors;
		cs = new CourseQuerySystem();
		students = new ArrayList<Student>();
	}
	
	public String getSubjectName(){
		return subjectName;
	}
	
	public String getCourseNo(){
		return courseNo;
	}

	public String getSectionNo(){
		return sectionNo;
	}
}
