
public class Resource {
	private String name;
	private Process user;
	
	public Resource(String pName) {
		name = pName;
	}
	
	public int requestResource(Process newUser) {
		if(user == null) {
			user = newUser;
			return 1;
		}
		return 0;
		
	}
	
	public int freeResource() {
		if(user == null) {
			return 0;
		}else {
			user = null;
			return 1;
		}
		
	}
	
	public Process getUser() {
		return user;
	}
	
	public String getName() {
		return name;
		
	}
	
}
