import java.util.ArrayList;

public class TextDisplay {

	
	public static void display(ArrayList<Resource> resources, ArrayList<Process> processes){
		System.out.println("-------------------------------------------");
		
		/**
		 * For each process, print out its name, what resources it has and what resources it wants.
		 */
		for(Process process: processes){
			System.out.println("I am " + process.getName());
			System.out.print("I own{ " );
			for(Resource res: process.getUsed()){
				System.out.print(res.getName() + " ");
			}
			System.out.println("}");
			System.out.print("I'm trying to get{ ");
			for(Resource res: process.getDesired()){
				System.out.print(res.getName() + " ");
			}
			System.out.println("}\n");
			
		}
		System.out.println("Free Resources are: ");
		/**
		 * Print each free resource
		 */
		for(Resource resource: resources){
			if(resource.getUser() == null){
				System.out.println(resource.getName());
			}
		}
		
		System.out.println("-------------------------------------------");
	}
}
