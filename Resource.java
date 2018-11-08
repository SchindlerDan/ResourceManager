/**
 * This class is used to handle resources and their functions such as being owned or freed.
 * 
 * 
 * @author Dan Schindler
 *
 */
public class Resource {
	
	/** 
	 * Tracks the resource's name
	 */
	private String name;
	
	/**
	 * Tracks the owner of the resource
	 */
	private Process user;
	
	/**
	 * Constructor for a resource.
	 * @param pName The name of the resource
	 */
	public Resource(String pName) {
		name = pName;
	}
	
	
	/**
	 * This method is called when a process it attempting to take ownership of a resource
	 * @param newUser The process calling passes itself as an argument, attempting to become the new owner.
	 * @return Returns 1 on success, 0 on failure. Used to determine if resource is already owned. 
	 */
	public int requestResource(Process newUser) {
		if(user == null) {
			user = newUser;
			return 1;
		}
		return 0;
		
	}
	
	
	/**
	 * Frees the resource when the owner is done.
	 * @return Returns 0 on failure, 1 on success. 
	 */
	public int freeResource() {
		if(user == null) {
			return 0;
		}else {
			user = null;
			return 1;
		}
		
	}
	
	/**
	 * Returns the owner of the resource
	 * @return owner of the resource
	 */
	public Process getUser() {
		return user;
	}
	
	
	/**
	 * Returns the name of the resource
	 * @return name of the resource
	 */
	public String getName() {
		return name;
		
	}
	
	
}
