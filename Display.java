import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This creates a simple graphical display
 * 
 * @author Dan Schindler
 *
 */
public class Display extends JPanel{
	
	/**
	 * resources from simulation
	 */
	ArrayList<Resource> resources;
	
	/**
	 * processes from simulation
	 */
	ArrayList<Process> processes;

	/**
	 * constructor for display
	 * @param resources
	 * @param processes
	 */
	public Display(ArrayList<Resource> resources, ArrayList<Process> processes) {
		this.resources = resources;
		this.processes = processes;
		
	}
	
	/**
	 * default constructor
	 */
	public Display() {
		resources = new ArrayList<>();
		processes = new ArrayList<>();
	}
	
	/**
	 * updates graphic's variables.
	 * @param resources
	 * @param processes
	 */
	public void update(ArrayList<Resource> resources, ArrayList<Process> processes) {
		this.resources = resources;
		this.processes = processes;
		
	}
	
	/**
	 * displays a new window with updated graph
	 */
	public void displayGraph() {
	JFrame f = new JFrame();
	f.setContentPane(new Display(resources, processes));
	f.setSize(600, 400);
	f.setVisible(true);
	}
	
	/**
	 * Draws the graph
	 */
	public void paintComponent(Graphics g){
		 int px = 100;
		 int py = 0;
		 int rx = 100;
		 int ry = 100;
		 int width = 50;
		 int height = 50;
		// this statement required
		super.paintComponent(g);
		setBackground(Color.white);
		g.setColor(Color.BLACK);
		int[] resourceX = new int[resources.size()];
		int[] processX = new int[processes.size()];
		/**
		 * Draw each resource 
		 */
		for(int i = 0; i < resources.size(); i++) {
			g.setColor(Color.BLACK);

			g.drawRect(rx, ry, width, height);
			resourceX[i] = rx;
			g.drawString(resources.get(i).getName(), rx + 25, ry + 25);

			rx +=50;
		}
		
		/**
		 * draw each process
		 */
		for(int i = 0; i < processes.size(); i++) {
			g.setColor(Color.BLACK);

			g.drawRect(px, py, width, height);
			processX[i] = px;
			g.drawString(processes.get(i).getName(), px + 25, py + 25);
			
			/**
			 * for each process' used resources, draw a black line to it.
			 */
			for(Resource res: processes.get(i).getUsed()) {
				int x2 = 0;
				
				g.setColor(Color.BLACK);
				
				/**
				 * find the resource on the graph
				 */
				for(int j = 0; j < resources.size(); j++) {
					if(res.getName().equals(resources.get(j).getName()))  {
						x2 = 125 + (50 * j);
						g.drawLine(px + 25, py + 50, x2, ry);
					}
				}
			
				
				
			}
			
			/**
			 * for each process' desired resources, draw a blue line to it.
			 */
			for(Resource res: processes.get(i).getDesired()) {
				int x2 = 0;
				
				g.setColor(Color.BLUE);
				
				/**
				 * find the resource on the graph
				 */
				for(int j = 0; j < resources.size(); j++) {
					if(res.getName().equals(resources.get(j).getName()))  {
						x2 = 125 + (50 * j);
						g.drawLine(px + 25, py + 50, x2, ry);
					}
				}
			
				
				
			}
			px += 50;
		}
		
		
		
		
	
		 
	}
}

