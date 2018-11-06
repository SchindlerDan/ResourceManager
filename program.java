import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class program {
		public static void main(String args[]) {
			Scanner scn = new Scanner(System.in);
			
			int successfulOperations = 1;
			int totalDesired = 0;
			int totalUsed = 0;
			int numProcesses = 0;
			int numResources = 0;
			int totalRequests = 0;
			int totalReleases = 0;
			
			
			
			ArrayList<Process> processes = new ArrayList<>();
			ArrayList<Resource> resources = new ArrayList<>();
			String line = "";
			//ArrayList<Pair> requests = new ArrayList<>();
			//ArrayList<Pair> releases = new ArrayList<>();
			
			
			
			
			System.out.println("Please enter the name of the file");
			String file = scn.nextLine();
	
			try(BufferedReader br = new BufferedReader(new FileReader(file))){
				line = br.readLine();
				String[] parsedLine = line.split(" ");
				numProcesses = Integer.parseInt(parsedLine[0]);
				line = br.readLine();
				parsedLine = line.split(" ");
				numResources = Integer.parseInt(parsedLine[0]);
				
				
				
				
				for(int i = 0; i < numProcesses; i++) {
					String processName = "p" + i;
					System.out.println("Creating process: " + processName);
					processes.add(new Process(processName));
				}
				
				for(int i = 0; i < numResources; i++) {
					String resourceName = "r" + i;
					System.out.println("Creating Resource: " + resourceName);
					resources.add(new Resource(resourceName));
				}
				
				
				line = br.readLine();
				do{
					System.out.println("successful operations is: " + successfulOperations);
						
					if(successfulOperations < 1) {
						if(line == null) {
							System.out.println("End of log. Total requests: " + totalRequests + " Total releases: " + totalReleases);
						}else {
							System.out.println("Deadlock detected. Shutting down.");
						}
						scn.close();
						System.exit(1);
					}
					Process key = null;
					Resource value = null;
					successfulOperations = 0;
					
					
					if(line != null) {
						parsedLine = line.split(" ");
						for(Process process: processes) {
							if(process.getName().equals(parsedLine[0])) {
								key = process;
							}
						}
						
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
					for(Process process: processes) {
						successfulOperations += process.useResources();
						
						totalDesired += process.getDesiredSize();
						totalUsed += process.getUsedSize();
					}
					
					
				}while(((line = br.readLine()) != null) || totalDesired > 0 || totalUsed > 0); 
				
				
				
				
				
				
				
				
				
				
				/*
				//actual simulation starts here
				while(requests.size() + releases.size() < 0) {
					if(successfulOperations < 1) {
						System.out.println("Deadlock detected. Shutting down.");
						scn.close();
						System.exit(1);
					}
					successfulOperations = 0;
					Process key;
					Resource value;
					//start by updating requests
					key = requests.get(0).getKey();
					value = requests.get(0).getValue();
					key.addDesired(value);
					
					//next we have everyone try to grab resources
					for(Process process: processes) {
						successfulOperations += process.useResources();
					}
					
					//next we release resources if we can
					
					
				}
				*/
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	
	
			
			}
}
