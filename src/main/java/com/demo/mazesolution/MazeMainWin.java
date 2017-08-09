package com.demo.mazesolution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * This class is the main window gui class of solving maze
 * 
 * <p>The mazes are given in a file and the program must read in the file, solve the maze and output the solution.
 * If no solution is possible the output is indicate by the message "There is no path to target".
 * Otherwise, the total number of moves from start position to the target position of the maze will be printed out
 * on the console window plus the maze pattern with path from start to end by characters pattern SXX...XXE as shown below
 * 
 * <p> 
 * ##########
 * #SXX     #
 * # #X######
 * # #XX    #
 * # ##X# ###
 * # # X# # #
 * # # XX   #
 * # ###X####
 * # #  XXXE#
 * ##########
 * 
 * <p>
 * We also show each maze solution for each maze problem with window GUI.
 * 
 * <p> The total maze process time in ms will be printed out as soon as all maze files processed including 
 * both displaying with console and window gui.
 * 	    
 * @author  Andrew X He
 * 
 * @since   1.0.0
 */
public class MazeMainWin {

    /**
     * Main method of the MazeMainWin console application
     *
     * @param  args  file names of maze files. Optional.
     * @throws IOException if the specified file not found. InvalidInputException if maze file is invalid.
     */
	public static void main(String[] args) throws IOException, InvalidInputException {
		try{
			processMazeWithDisplayPanel(args);
		}catch(IOException ioe){
			System.out.println("File was not fould!");
		}catch(InvalidInputException ie){
			System.out.println("Invalid file!");
		}
	}

    /**
     * processMazeWithDisplayPanel static method called by main method 
     *
     * <p> This method is used to support two way to run the maze solution application:
     * 1) java -cp mazesolution.jar com.demo.mazesolution.MazeMainWin which pick up all demo maze files within the jar file and run it in one goal.
     *    The running time with Microsoft Surface Pro is less than 1000 ms as it diplays both console and window GUI.
     * 2) java -cp mazesolution.jar com.demo.mazesolution.MazeMainWin input.txt large_input.txt medium_input.txt small.txt sparse_medium.txt  
     *    You can pass one or more maze files. 
     * @param  args  file names of maze files. Optional.
     * @throws IOException if the specified file not found. InvalidInputException if maze file is invalid.
     */
	public static void processMazeWithDisplayPanel(String[] args)throws IOException, InvalidInputException{
        long startTime = System.currentTimeMillis();

        if(args.length >= 1){
			for(String filename:args){

				filename = filename.trim();
				System.out.println("");
				System.out.println("The current maze file is "+ filename + ".");

				MazeProprocessor handler = new MazeProprocessor();
				MazeGraph graph = handler.readMazeMapFromFile(filename);
				
				ArrayList<Node> path = graph.execute(false);
				graph.setPath(path);
				
				if(path == null){
					System.out.println("There is no path to target");
				}
				else{
					System.out.println("The total number of moves from start to the target are : " + path.size());
					
					//to meet the specification
					graph.output();
					
					JFrame frame = new JFrame("Maze - "+filename);
					MazePanel panel = new MazePanel(graph); // Constructs the panel to hold the maze
					JScrollPane scrollPane = new JScrollPane(panel);
					
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setSize(1000, 800);
					frame.add(scrollPane, BorderLayout.CENTER);
					frame.setVisible(true);		
				}
			}
		}else{
			String [] filenames = {"input.txt", "large_input.txt", "medium_input.txt", "small.txt", "sparse_medium.txt"};
			
			for(String filename:filenames){
				filename = filename.trim();
				System.out.println("");
				System.out.println("The current maze file is "+ filename + ".");

				InputStream is = MazeMain.class.getClassLoader().getResourceAsStream(filename);
				MazeProprocessor mazeProprocessor = new MazeProprocessor();
				MazeGraph graph = mazeProprocessor.readMazeMapFromResource(is);
				
				ArrayList<Node> path = graph.execute(false);
				graph.setPath(path);
				
				if(path == null){
					System.out.println("There is no path to target");
				}
				else{
					System.out.println("The total number of moves from start to the target are : " + path.size());

					//to meet the specification 
					graph.output();
					
					JFrame frame = new JFrame("Maze - "+filename);
					MazePanel panel = new MazePanel(graph); // Constructs the panel to hold the maze
					JScrollPane scrollPane = new JScrollPane(panel);
					
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setSize(1000, 800);
					frame.add(scrollPane, BorderLayout.CENTER);
					frame.setVisible(true);		
				}
			}
		}


		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Total maze process time is " + elapsedTime + " ms.");
	}
}


class MazePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private MazeGraph maze; 

	public MazePanel(MazeGraph graph)
	{
		this.maze = graph;
	}

	public void paintComponent(Graphics page) 
	{
		super.paintComponent(page);
		
		setBackground(Color.white);
		
		this.setPreferredSize(maze.windowSize()); 
		
		maze.draw(page);
	}
}
