package com.demo.mazesolution;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class is the main class of solving maze
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
 * <p> The total maze process time in ms will be printed out as soon as all maze files processed. 
 * @author  Andrew X He
 * 
 * @since   1.0.0
 */
public class MazeMain {

    /**
     * Main method of the MazeMain console application
     *
     * @param  args  file names of maze files. Optional.
     * @throws IOException if the specified file not found. InvalidInputException if maze file is invalid.
     */
	public static void main(String[] args) throws IOException, InvalidInputException {
		try{
			processMazeWithConsole(args);
		}catch(IOException ioe){
			System.out.println("File was not fould!");
		}catch(InvalidInputException ie){
			System.out.println("Invalid file!");
		}
	}	

    /**
     * processMazeWithConsole static method called by main method 
     *
     * <p> This method is used to support two way to run the maze solution application:
     * 1) java -jar mazesolution.jar which pick up all demo maze files within the jar file and run it in one goal.
     *    The running time with Microsoft Surface Pro is less than 700 ms.
     * 2) java -jar mazesolution.jar input.txt large_input.txt medium_input.txt small.txt sparse_medium.txt  
     *    You can pass one or more maze files. 
     * @param  args  file names of maze files. Optional.
     * @throws IOException if the specified file not found. InvalidInputException if maze file is invalid.
     */
	public static void processMazeWithConsole(String[] args)throws IOException, InvalidInputException{
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
				    graph.output();
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
				    graph.output();
				}
			}
		}

		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Total maze process time is " + elapsedTime + " ms.");
	}
}
