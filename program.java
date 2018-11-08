import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * This class' main method reads a simulation input file and then runs a simulation based off of the file. 
 * 
 * @author Dan Schindler
 *
 */
public class program {
		public static void main(String args[]) {
			
			/**
			 * scanner for getting user input. User input used to get input file.
			 */
			Scanner scn = new Scanner(System.in);
			
			/**
			 * Amount of time simulation should wait between steps. Smaller number means faster simulation. 
			 */
			int simulationSpeed;
			
			/**
			 * successful operations per simulation step. Instantiated to 1 to get past first loop without issue.
			 */
			int successfulOperations = 1;
			
			/**
			 * Total desired resources. How many resources every process wants.
			 */
			int totalDesired = 0;
			
			/**
			 * Total resources used by each process
			 */
			int totalUsed = 0;
			
			/**
			 * Number of processes in the simulation
			 */
			int numProcesses = 0;
			
			/**
			 * number of resources in the simulation
			 */
			int numResources = 0;
			
			/**
			 * Total number of requests during course of simulation.
			 */
			int totalRequests = 0;
			
			/**
			 * Total number of releases during course of simulation.
			 */
			int totalReleases = 0;
			
			
			/**
			 * List of processes in simulation
			 */
			ArrayList<Process> processes = new ArrayList<>();
			
			/**
			 * List of resources in simulation
			 */
			ArrayList<Resource> resources = new ArrayList<>();
			
			/**
			 * Used to hold each line of input. 
			 */
			String line = "";

			System.out.println("Please enter the desired speed per simulation step in ms. Ex value is 1000");
			simulationSpeed = scn.nextInt();
			
			
			System.out.println("Please enter the name of the file");
			String file = scn.next();
	
			try(BufferedReader br = new BufferedReader(new FileReader(file))){
				line = br.readLine();
				String[] parsedLine = line.split(" ");
				numProcesses = Integer.parseInt(parsedLine[0]);
				line = br.readLine();
				parsedLine = line.split(" ");
				numResources = Integer.parseInt(parsedLine[0]);
				
				
				
				/**
				 * This loop creates all of the processes in the simulation and adds them to the list of processes.
				 */
				for(int i = 0; i < numProcesses; i++) {
					String processName = "p" + i;
					System.out.println("Creating process: " + processName);
					processes.add(new Process(processName));
				}
				
				/**
				 * This loop creates all of the resources in the simulation and adds them to the list of resources.
				 */
				for(int i = 0; i < numResources; i++) {
					String resourceName = "r" + i;
					System.out.println("Creating Resource: " + resourceName);
					resources.add(new Resource(resourceName));
				}
				TextDisplay.display(resources, processes);
				try {
					Thread.sleep(simulationSpeed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//read the first simulation line
				line = br.readLine();
				
				/**
				 * this do-while loop is the actual simulation. It continues as long as there are successful operations, 
				 * a process is using a resource, or a process wants a resource. Ends automatically if there are no
				 * successful operations. 
				 * No more successful operations means we either encountered deadlock or we've reached the end of the
				 * Simulation file. 
				 * 
				 * We know we're in deadlock if there's no successful operations
				 */
				do{
						
					if(successfulOperations < 1) {
						if(line == null) {
							System.out.println("End of log. Total requests: " + totalRequests + " Total releases: " + totalReleases);
						}else {
							System.out.println("No successful Operations detected. Shutting down.");
						}
						scn.close();
						System.exit(1);
					}
					Process key = null;
					Resource value = null;
					successfulOperations = 0;
					
					
					if(line != null) {
						parsedLine = line.split(" ");
						/**
						 * This loop finds the process we're doing something with
						 */
						for(Process process: processes) {
							if(process.getName().equals(parsedLine[0])) {
								key = process;
							}
						}
						
						/**
						 * This loop find the resource we're doing something with
						 */
						for(Resource resource: resources) {
							if(resource.getName().equals(parsedLine[2])) {
								value = resource;
							}
						}

						if(parsedLine[1].equals("requests")) {
							System.out.println(key.getName() + " Adding desired " + value.getName());
							successfulOperations += key.addDesired(value);
							successfulOperations += key.useResources();
							totalRequests +=1;
						
						
							//requests.add(new Pair(key, value));
						}else if(parsedLine[1].equals("releases")) {
							System.out.println(key.getName() + " releasing " + value.getName());
							int tmp = key.releaseResource(value);
							if(tmp < 1) {
								System.out.println("I couldn't release resource " + value.getName() + ". I don't own it.");
							}else {
								successfulOperations += tmp;
							}
							totalReleases += 1;
							//releases.add(new Pair(key, value));
						}else {
							System.out.println("ERROR: Could not parse line: " + line);
						}
					}
					
					
					totalUsed = 0;
					totalDesired = 0;
					
					/**
					 * For each process, check and see if you're deadlocked. Then try to use all resources you wanted.
					 * Then release any resources you couldn't
					 * release earlier for some reason. 
					 */
					for(Process process: processes) {
						
						
						
						successfulOperations += process.useResources();
						totalDesired += process.getDesiredSize();
						totalUsed += process.getUsedSize();
						
						if(process.getDesiredSize() > 0) {
							ArrayList<Process> deadLockPath = new ArrayList<Process>();
							deadLockPath = checkDeadlock(new ArrayList<Process>(), process, numProcesses);
							if(deadLockPath.size() > 0) {
								System.out.println("Deadlock detected due to following processes:");
								for(Process proc: deadLockPath) {
									System.out.println(proc.getName());
								}
								System.out.println("Closing simulation");
								System.exit(0);
							}
						}
					}
					
					TextDisplay.display(resources, processes);
					try {
						Thread.sleep(simulationSpeed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(((line = br.readLine()) != null) || totalDesired > 0 || totalUsed > 0); 
				
			
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	
	
			
		}
		
		
		
		public static ArrayList<Process> checkDeadlock(ArrayList<Process> path, Process current, int totalProcesses){
			ArrayList<Process> emptyPath = new ArrayList<>();
			ArrayList<Process> tempPath = new ArrayList<>();
			
			//Following print statements intentionally left commented out. Put them back in for diagnostic information on the deadlock check
			
			System.out.println("initialized emptyPath, See: " + emptyPath);
			System.out.println("initialized tempPath, See: " + tempPath);
			System.out.print("path is currently: " );
			
			for(Process proc: path) {
				System.out.print(proc.getName() + " ");
			}
			
			System.out.println("current is: " + current.getName());
			
			if( path.size() > 1 && path.get(0) == current) {
				return path;
			}else if((path.size() >= totalProcesses) || (current.getDesiredSize() < 1)) {
				return emptyPath;
			}
			path.add(current);
			for(Resource res: current.getDesired()) {
				System.out.println("resource is: " + res.getName());
				System.out.println("owner of resource is: " + res.getUser().getName());
				tempPath = checkDeadlock(path, res.getUser(), totalProcesses);
				if(tempPath.size() > 0 ) {
					return tempPath;
				}
			}
			return emptyPath;
			
			
		}
}
