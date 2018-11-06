import java.util.ArrayList;

public class Process {
	String name;
	ArrayList<Resource> used;
	ArrayList<Resource> desired;
	
	public Process(String pName) {
		name = pName;
		used = new ArrayList<>();
		desired = new ArrayList<>();
		
	}
	
	public int releaseResource(Resource resource) {
		for(int i = 0; i < used.size(); i++) {
			if(used.get(i) == resource) {
				used.get(i).freeResource();
				used.remove(i);
				System.out.println("Released resource " + resource.getName());
				return 1;
			}
		}
		return 0;
	}
	
	public int addDesired(Resource resource) {
		desired.add(resource);
		return 1;
	}
	
	public int useResources() {
		int newUses = 0;
		for(int i = 0; i < desired.size(); i++) {
			int success = desired.get(i).requestResource(this);
			if(success > 0) {
				used.add(desired.get(i));
				System.out.println(name + " is now using resource " + desired.get(i).getName());
				desired.remove(i);
				newUses +=1;
			}
		}
		//System.out.println(name + " using " + newUses + "more resources");
		return newUses;
	}
	
	
	public int getDesiredSize() {
		return desired.size();
	}
	
	public int getUsedSize() {
		return used.size();
	}
	
	public String getName() {
		return name;
	}



}
