import java.util.Scanner;

public class Main {
	private static final String COMMAND_ICON = ">";
	
	public static void main(String[] args) throws Exception{
		CourseQuerySystem querySystem = new CourseQuerySystem();
		Parser parser = new Parser();
		Adder adder = new Adder();
		CLI interpreter = new CLI(querySystem, parser, adder);
		while(true){
			Scanner scanner = new Scanner(System.in);	
			System.out.println(COMMAND_ICON);
			interpreter.start(scanner);
		}
	}
}




