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
			 * if deadlock is detected, continue with simulation anyway
			 */
			int ignorePotentialIssues;
			
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

			
			System.out.println("Please enter '1' or '0' for if we should force simulation to continue despite potential deadlock"
					+ "\nPlease note that the simulation will still end if no successful operations can be completed");
			ignorePotentialIssues = scn.nextInt();
			
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
							System.out.println("Total Deadlock detected. Shutting down.");
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
							totalRequests +=1;
						
						
							//requests.add(new Pair(key, value));
						}else if(parsedLine[1].equals("releases")) {
							System.out.println(key.getName() + " releasing " + value.getName());
							successfulOperations += key.releaseResource(value);
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
						for(Process process2: processes){
							boolean potentialFlagOne = false;
							boolean potentialFlagTwo = false;
							
							for(Resource res: process.getUsed()){
								if(process2.getDesired().contains(res) && process != process2){
									potentialFlagOne = true;
								}
							}
							
							for(Resource res: process.getDesired()){
								if(process2.getUsed().contains(res) && process != process2){
									potentialFlagTwo = true;
								}
							}
							if(potentialFlagOne && potentialFlagTwo && ignorePotentialIssues == 1){
								System.out.println("WARNING! POTENTIAL DEADLOCK DISCOVERED!");
								System.out.println("Conflict of interest between processes: " + process.getName() + " and " + process2.getName());
								System.out.println("Continuing simulation...");
							}else if(potentialFlagOne && potentialFlagTwo){
								System.out.println("WARNING! POTENTIAL DEADLOCK DISCOVERED!");
								System.out.println("Conflict of interest between processes: " + process.getName() + " and " + process2.getName());
								System.out.println("Closing simulation...");
								scn.close();
								System.exit(1);
							}
						
						}
						
						
						successfulOperations += process.useResources();
						successfulOperations += process.lateRelease();
						totalDesired += process.getDesiredSize();
						totalUsed += process.getUsedSize();
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
}
