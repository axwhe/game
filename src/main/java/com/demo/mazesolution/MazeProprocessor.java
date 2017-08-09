package com.demo.mazesolution;

import java.awt.Point;
import java.io.*;

public class MazeProprocessor {
	
	private Point GetMazeParameters(String input) throws InvalidInputException{
		Point pt = new Point();
		String[] values = input.split(" ");
		if(values.length!=2) throw new InvalidInputException("We expect two numbers.");
		try{
			pt.x = Integer.parseInt(values[0]);
			pt.y = Integer.parseInt(values[1]);
		}catch( NumberFormatException e){
			 throw new InvalidInputException("There was a wrong character in the maze data file. The characters must be digit from 0, 1, 2, to 9.");
		}
	    return pt;
	}

	private char[] GetMazeLineData(String input, int dimensionX) throws InvalidInputException{
		char [] values = new char[dimensionX];
		String[] inputs = input.split(" ");
		if(inputs.length!=dimensionX) {
			throw new InvalidInputException("Invalid number of columns in the row data!");
		}
		for(int i = 0; i<inputs.length; i++){
			char v = inputs[i].trim().charAt(0);
			if(v=='1'||v=='0')
				values[i] = v;
			else
				new InvalidInputException("There was a wrong character in the maze data file. The character must be 1, or 0.");
		}
		
	    return values;
	}

	public MazeGraph readMap(BufferedReader in) throws IOException, InvalidInputException, FileNotFoundException {
		try{
			
			Point dimension = GetMazeParameters(in.readLine());
			Point startPosition = GetMazeParameters(in.readLine());
			Point targetPosition = GetMazeParameters(in.readLine());
			
			MazeGraph graph = new MazeGraph(dimension.x,dimension.y);
			
			
			//i => x, j => y
			for(int j=0; j<dimension.y; j++){
				char [] line = GetMazeLineData(in.readLine(), dimension.x);
				for(int i=0; i<dimension.x; i++){
					char typeSymbol = line[i];
					if(typeSymbol == '0'){
						Point pt = new Point(i, j);
						
						if(pt.equals(startPosition)){
							Node n = new Node(i,j, Node.Type.START);
							graph.setMapCell(new Point(i,j), n);
							graph.setStartPosition(new Point(i,j));
						}						
						else if(pt.equals(targetPosition)){
							Node n = new Node(i,j, Node.Type.END);
							graph.setMapCell(new Point(i,j), n);
							graph.setTargetPosition(new Point(i,j));
						}else{
							Node n = new Node(i,j, Node.Type.NORMAL); //some will be updated to PATH type
							graph.setMapCell(new Point(i,j), n);
						}
					}
					else if(typeSymbol == '1'){
						Node n = new Node(i,j, Node.Type.OBSTACLE);
						graph.setMapCell(new Point(i,j), n);
					}
					else{
						throw new InvalidInputException("There was a wrong character in the maze data file. The character must be 1, or 0.");
					}
				}
			}
			return graph;
		}
		catch(IOException e){
			throw e;
		}
		finally{
			in.close();
		}
	}

	public MazeGraph readMazeMapFromFile(String filename) throws IOException, InvalidInputException, FileNotFoundException {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		return readMap(in);

	}
	
	public MazeGraph readMazeMapFromResource(InputStream is) throws IOException, InvalidInputException, FileNotFoundException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		return readMap(in);
	}
}
