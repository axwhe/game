package com.demo.mazesolution;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MazeGraph {

	public static final int NODE_WIDTH = 15; // maze square size
	public static final int MARGIN = 50; // buffer between window edge and maze
	
	private Node[][] map;
	private Point startPosition;
	private Point targetPosition;
	private Heap<Node> openNodes;
	private Set<Node> closedNodes;
	private int dimensionX;
	private int dimensionY;

	private Node startNode;
	private Node endNode;
	
	
	private ArrayList<Node> mazePath;
	
	public MazeGraph(int mapDimension){
		dimensionX = mapDimension;
		dimensionY = mapDimension;
		map = new Node[dimensionX][dimensionY];
		startPosition = new Point();
		targetPosition = new Point();
		openNodes = new Heap<Node>();
		closedNodes = new HashSet<Node>();
		mazePath = new ArrayList<Node>();
	}
	
	public MazeGraph(int mapDimensionX, int mapDimensionY){
		dimensionX = mapDimensionX;
		dimensionY = mapDimensionY;
		map = new Node[mapDimensionX][mapDimensionY];
		startPosition = new Point();
		targetPosition = new Point();
		openNodes = new Heap<Node>();
		closedNodes = new HashSet<Node>();
		mazePath = new ArrayList<Node>();
	}
	

	public Node getMapCell(Point coord){
		return map[(int)coord.getX()][(int)coord.getY()];
	}
	
	public void setMapCell(Point coord, Node n){
		map[(int)coord.getX()][(int)coord.getY()] = n;
	}
	
	public Point getStartPosition(){
		return startPosition;
	}
	
	public Point getTargetPosition(){
		return targetPosition;
	}
	
	public void setStartPosition(Point coord){
		startPosition.setLocation(coord);
	}
	
	public void setTargetPosition(Point coord){
		targetPosition.setLocation(coord);
	}
	
	public int getDimension(){
		return map.length;
	}
	
	public void addToOpenNodes(Node n){
		n.setOpen();
		openNodes.add(n);
	}
	
	public Node popBestOpenNode(){
		return openNodes.remove();
	}
	
	public void addToClosedNodes(Node n){
		n.setClosed();
		closedNodes.add(n);
	}
	
	public boolean isInsideMap(Point p){
		return ( (p.getX() >= 0) && (p.getX() < getDimension())  && (p.getY() >= 0) && (p.getY() < getDimension()) );
	}
	
	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Set<Node> getNeighbours(Node n, boolean diagonal){
		Set<Node> neighbours = new HashSet<Node>();
		for(int j=-1; j<=1; j++){
			for(int i=-1; i<=1; i++){
				if( !(i==0 && j==0) )
					if(isInsideMap(new Point(n.getX() + i,n.getY() + j))){
						Node temp = getMapCell(new Point(n.getX() + i,n.getY() +  j));
						if(!temp.isObstacle()){
							//deal with W, SW, S, SE, E, NE, N, NW neighbours
							if(diagonal)
							  neighbours.add(temp);
							else{
								//deal with W, S, E, N neighbours only
								if((i==0 && j==-1) 
									||(i==-1 && j==0)
									||(i==0 && j== 1)
									||(i==1 && j== 0)){
									neighbours.add(temp);
								}									
							}
						}
					}
					
			}
		}
		return neighbours;
	}
	
	
	public Set<Node> getNeighbours(Node n){
		return getNeighbours(n, false);
	}
	
	static double calculateDistance(Point from, Point to){
		return Math.pow(Math.pow(from.getX()-to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2) , 0.5);
	}
	
	public ArrayList<Node> reconstructPath(Node target){
		ArrayList<Node> path = new ArrayList<Node>();
		Node current = target;
		path.add(current);
		while(current.getParent() != null){
			path.add(current.getParent());
			current = current.getParent();
		}
		Collections.reverse(path);
		
		return path;
	}

	public void setPath(ArrayList<Node> mazePath){
		if(mazePath.size()<2) return;
		
		this.mazePath.add(mazePath.get(0));

		 //exclude startPosition (printed out with S in console) and endPosition (with E) 
		for(int i=1; i<mazePath.size()-1; i++){
			Node node = mazePath.get(i);
			node.setType(Node.Type.PATH); //printed out with X
			setMapCell(node.getPosition(), node);
			this.mazePath.add(node);
		}
		
		this.mazePath.add(mazePath.get(mazePath.size()-1));
		
	}
	
	public ArrayList<Node> getPath(){
		return mazePath;
	}
	
	public void printPath(){
		for(int i=0; i<this.mazePath.size(); i++){
			Node node = this.mazePath.get(i);
			System.out.println(i+ " node : (" + node.getX() + "," + node.getY() + ")");
		}
	}
	

	
	public ArrayList<Node> execute(boolean diagonal){
		Node start = getMapCell(getStartPosition());
		Node target = getMapCell(getTargetPosition());
		addToOpenNodes(start);
		
		start.setCostFromStart(0);
		start.setTotalCost( start.getCostFromStart() + calculateDistance(start.getPosition(), target.getPosition()) );
		
		while(!openNodes.isEmpty()){
			Node current = popBestOpenNode();
			if(current.equals(target)){
				return reconstructPath(target);
			}
			
			addToClosedNodes(current);
			Set<Node> neighbours = getNeighbours(current, diagonal);
			for(Node neighbour : neighbours){
				if(!neighbour.isClosed()){
					double tentativeCost = current.getCostFromStart() + calculateDistance(current.getPosition(), neighbour.getPosition());
					
					if( (!neighbour.isOpen()) || (tentativeCost < neighbour.getCostFromStart()) ){
						neighbour.setParent(current);
						neighbour.setCostFromStart(tentativeCost);
						neighbour.setTotalCost(neighbour.getCostFromStart() + calculateDistance(neighbour.getPosition(), start.getPosition()));
						if(!neighbour.isOpen())
							addToOpenNodes(neighbour);
					}
				}
					
			}
		}
		
		return null;
	}
	
	public Dimension windowSize() // returns the ideal size of the window (for JScrollPanes)
	{
		return new Dimension(dimensionX * NODE_WIDTH + MARGIN * 2, dimensionY * NODE_WIDTH + MARGIN	* 2);
	}	

	public void output() // print out a maze and its solution
	{
		System.out.println("");
		
		for (int j = 0; j < dimensionY; j++)
		{
	
			for (int i = 0; i < dimensionX; i++)
			{
				if(map[i][j].isObstacle()){
					System.out.print("#");
				}else if(map[i][j].isPassage()){
					System.out.print(" ");
				}
				else if(map[i][j].isStart()){
					System.out.print("S");
				}
				else if(map[i][j].isTarget()){
					System.out.print("E");
				}
				else if(map[i][j].isPath())
				{
					System.out.print("X");
				}				
			}
			System.out.println("");
		}
	
	}	
	
			
	public void draw(Graphics g) // draws a maze and its solution
	{
		for (int j = 0; j < dimensionY; j++)
		{
	
			for (int i = 0; i < dimensionX; i++)
			{
				
				if(map[i][j].isObstacle()){
					g.setColor(Color.BLUE);
					g.drawString("#", i*NODE_WIDTH +MARGIN, j*NODE_WIDTH + MARGIN);
				}
				else if(map[i][j].isPassage()){
					g.setColor(Color.BLACK);
					g.drawString(" ", i*NODE_WIDTH +MARGIN, j*NODE_WIDTH + MARGIN);
				}				
				else if(map[i][j].isStart()){
					g.setColor(Color.RED);
					g.drawString("S", i*NODE_WIDTH +MARGIN, j*NODE_WIDTH + MARGIN);
				}
				else if(map[i][j].isTarget()){
					g.setColor(Color.RED);
					g.drawString("E", i*NODE_WIDTH +MARGIN, j*NODE_WIDTH + MARGIN);
				}
				else if(map[i][j].isPath()){
					g.setColor(Color.RED);
					g.drawString("X", i*NODE_WIDTH +MARGIN, j*NODE_WIDTH + MARGIN);
				}
			}
		}
	
	}	
}
