package com.demo.mazesolution;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;

/**
 * Unit test for Maze Solution App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Test maze path existence :-)
     * @throws InvalidInputException 
     */
    public void testApp() throws IOException, InvalidInputException
    {
    	
    	String filename = "src/main/resources/input.txt";
		MazeProprocessor handler = new MazeProprocessor();
		MazeGraph graph = handler.readMazeMapFromFile(filename);
		
		ArrayList<Node> path = graph.execute(false);
    	
        assertTrue( path!= null );
    }
}
