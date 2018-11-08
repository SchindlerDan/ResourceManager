import java.util.ArrayList;

/**
 * 
 * This class is used to handle processes and their functions such as requesting or freeing resources.
 * 
 * 
 * @author Dan Schindler
 *
 */
public class Process {
	
	/**
	 * Name of the process
	 */
	String name;
	
	/**
	 * List of resources that this process owns
	 */
	ArrayList<Resource> used;
	
	/**
	 * List of resources this process wants
	 */
	ArrayList<Resource> desired;
	
	/**
	 * List of resources this process is done with but couldn't release. Should rarely, if ever, have any elements. 
	 */
	ArrayList<Resource> release;
	
	/**
	 * Constructor for processes
	 * @param pName Name of the process
	 */
	public Process(String pName) {
		name = pName;
		used = new ArrayList<>();
		desired = new ArrayList<>();
		release = new ArrayList<>();
		
	}
	
	/**
	 * Tells a process to release a certain resource. Adds resource to list of released resources if we can't release the resource now.
	 * @param resource the resource to be released
	 * @return returns an int for whether or not the release was successful.
	 */
	public int releaseResource(Resource resource) {
		//This loop checks each used resource to see if it is the one we must release
		for(int i = 0; i < used.size(); i++) {
			if(used.get(i) == resource) {
				used.get(i).freeResource();
				used.remove(i);
				System.out.println("Released resource " + resource.getName());
				return 1;
			}
		} 
		release.add(resource);
		return 0;
	}
	
	
	/**
	 * Adds a resource to the process' list of desired resources.
	 * @param resource the resource to add to the list.
	 * @return Returns an int on success
	 */
	public int addDesired(Resource resource) {
		desired.add(resource);
		return 1;
	}
	
	
	
	/**
	 * Attempt to use all desired resources
	 * @return returns an int based on number of successes.
	 */
	public int useResources() {
		int newUses = 0;
		//Checks each desired resource for availability
		for(int i = 0; i < desired.size(); i++) {
			int success = desired.get(i).requestResource(this);
			if(success > 0) {
				used.add(desired.get(i));
				System.out.println(name + " is now using resource " + desired.get(i).getName());
				desired.remove(i);
				newUses +=1;
			}
		}
		return newUses;
	}
	
	
	/**
	 * gets the size of the desired list
	 * @return size of desired list
	 */
	public int getDesiredSize() {
		return desired.size();
	}
	
	/**
	 * gets the size of the used list
	 * @return size of the used list
	 */
	public int getUsedSize() {
		return used.size();
	}
	
	/**
	 * gets the name of the process
	 * @return name of the process
	 */
	public String getName() {
		return name;
	}


	/**
	 * Allows processes to release resources after the simulation says to. Is never used in current version of program.
	 * @return int returns number of successful late releases
	 */
	public int lateRelease() {
		int totalSuccess = 0;
		//goes through list of resources to see if any must be released.
		for(Resource resource: release) {
			int success = 0;
			success = releaseResource(resource);
			if( 0 < success) {
				release.remove(resource);
				totalSuccess += success;
			}
		}
		return totalSuccess;
	}

	/**
	 * gets the list of used resources
	 * @return list of used resources
	 */
	public ArrayList<Resource> getUsed(){
		return used;
	}

	/**
	 * gets the list of desired resources
	 * @return list of desired resources
	 */
	public ArrayList<Resource> getDesired(){
		return desired;
	}
}
