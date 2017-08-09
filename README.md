# Maze Solution in Java

The idea here is to write a program to solve simple mazes. The mazes are given in 
a file and the program must read in the file, solve the maze and output the solution.
If no solution is possible the output should indicate this somehow. The program 
should be written to the following specification:
  
  - Arbitrary sized mazes should be handled
  - Valid moves are N, S, E, W (not diagonally)
  - All input will be clean, no validation is necessary
  - Any suitable language can be used although one of Java, C#, Python is preferred
  - The maze file format is described below with an example
  - The program should be tested on the sample mazes provided
  - Output should be written to the Standard Output/Console

#### Maze file format
================

The input is a maze description file in plain text.  
 1 - denotes walls
 0 - traversable passage way

INPUT:
<WIDTH> <HEIGHT><CR>
<START_X> <START_Y><CR>		(x,y) location of the start. (0,0) is upper left and (width-1,height-1) is lower right
<END_X> <END_Y><CR>		(x,y) location of the end
<HEIGHT> rows where each row has <WIDTH> {0,1} integers space delimited

OUTPUT:
 the maze with a path from start to end
 walls marked by '#', passages marked by ' ', path marked by 'X', start/end marked by 'S'/'E'

####Compilation

mvn clean install

####Execution

####for console solution, go to target folder at command line and execute:

java -jar mazesolution.jar

or

java -jar mazesolution.jar input.txt large_input.txt medium_input.txt small.txt sparse_medium.txt

or

java -jar mazesolution.jar yourFolder/yourMazeFile.txt


####for window gui solution, go to target folder at command line and execute:

java -cp mazesolution.jar com.demo.mazesolution.MazeMainWin

or 

java -cp mazesolution.jar com.demo.mazesolution.MazeMainWin  input.txt large_input.txt medium_input.txt small.txt sparse_medium.txt
