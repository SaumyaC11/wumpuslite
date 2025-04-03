/*
 * Class that defines the agent function.
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 * 
 * Last modified 2/19/07 
 * 
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 * 
 */

 import java.io.BufferedWriter;
 import java.io.FileWriter;
 import java.util.ArrayList;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Set;
 
 class AgentFunction {
	 
	 // string to store the agent's name
	 // do not remove this variable
	 private String agentName = "Agent Smith";
	 String outFilename = "test.txt";
	 int timestamp = 0;
	 
	 // all of these variables are created and used
	 // for illustration purposes; you may delete them
	 // when implementing your own intelligent agent
	 private Set<IntPair> pitLocations = new HashSet<>();
	 private boolean pitsDiscovered = false;
	 private boolean arrowUsed = false;
	 private boolean bump;
	 private boolean glitter;
	 private boolean breeze;
	 private boolean stench;
	 private boolean scream;
	 private int[][] newWorld = new int[4][4];
	 private int[][] probabilityOfPit = new int[4][4];
	 private int lastMove = Action.NO_OP;
	 private int currX = 0;
	 private int currY = 0;
	 private char dir = 'E';
	 private int[] dir_row = {1, -1, 0, 0};
	 private int[] dir_col = {0, 0, 1, -1};
	 private boolean firstTimeStench = true;
 
	 private boolean isValid(int x, int y) {
		 if (x >= 0 && x < 4 && y >= 0 && y < 4)
			 return true;
		 return false;
	 }
 
	 private void markPit() {
		 if (pitsDiscovered == true) {
			 for (int i = 0; i < 4; i++) {
				 int new_x = currX + dir_row[i];
				 int new_y = currY + dir_col[i];
				 if (isValid(new_x, new_y) && newWorld[new_x][new_y] == -1) {
					 newWorld[new_x][new_y] = 0;
					 probabilityOfPit[new_x][new_y] = 0;
				 }
			 }
			 return;
		 }
		 for (int i = 0; i < 4; i++) {
			 int new_x = currX + dir_row[i];
			 int new_y = currY + dir_col[i];
			 if (isValid(new_x, new_y) && (newWorld[new_x][new_y] == -1 || newWorld[new_x][new_y] == 2)) {
				 newWorld[new_x][new_y] = 1;
				 pitLocations.add(new IntPair(new_x, new_y));
				 if (lastMove == Action.GO_FORWARD) {
					 probabilityOfPit[new_x][new_y] = probabilityOfPit[new_x][new_y] * 2 + 1;
				 }
			 }
		 }
	 }
 
	 private void removeWumpus() {
		 for (int i = 0; i < 4; i++) {
			 for (int j = 0; j < 4; j++) {
				 if (newWorld[i][j] == 2) {
					 newWorld[i][j] = 0;
				 }
			 }
		 }
	 }
 
	 private void markGold() {
		 newWorld[currX][currY] = 99;
	 }
 
	 private void markSafe() {
		 for (int i = 0; i < 4; i++) {
			 int new_x = currX + dir_row[i];
			 int new_y = currY + dir_col[i];
			 if (isValid(new_x, new_y)) {
				 if (pitLocations.contains(new IntPair(new_x, new_y))) {
					 pitLocations.remove(new IntPair(new_x, new_y));
				 }
				 if (newWorld[new_x][new_y] != 50) {
					 newWorld[new_x][new_y] = 0;
					 probabilityOfPit[new_x][new_y] = 0;
				 }
			 }
		 }
	 }
 
	 private void markWumpus(boolean alsoBreeze) {
		 if (firstTimeStench) {
			 for (int i = 0; i < 4; i++) {
				 int new_x = currX + dir_row[i];
				 int new_y = currY + dir_col[i];
				 if (isValid(new_x, new_y) && newWorld[new_x][new_y] == -1) {
					 newWorld[new_x][new_y] = 2;
				 }
			 }
			 firstTimeStench = false;
		 } else {
			 for (int i = 0; i < 4; i++) {
				 int new_x = currX + dir_row[i];
				 int new_y = currY + dir_col[i];
				 if (isValid(new_x, new_y) && newWorld[new_x][new_y] == -1 && alsoBreeze == false) {
					 newWorld[new_x][new_y] = 0;
				 }
			 }
		 }
 
	 }
 
	 public AgentFunction()
	 {
		 try {
			 BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outFilename));
			 outputWriter.write("\n");
			 outputWriter.close();
		 } catch (Exception e) {
			 System.out.println("An exception was thrown: " + e);
		 }
		 
		 for (int i = 0; i < 4; i++) {
			 for (int j = 0; j < 4; j++) {
				 newWorld[i][j] = -1;
				 probabilityOfPit[i][j] = 0;
			 }
		 }
	 
		 newWorld[0][0] = 50;
	 }
 
	 private void turnLeft() {
		 switch (dir) {
			 case 'N': dir = 'W'; break;
			 case 'W': dir = 'S'; break;
			 case 'S': dir = 'E'; break;
			 case 'E': dir = 'N'; break;
		 }
		 
	 }
 
	 private void turnRight() {
		 switch (dir) {
			 case 'N': 
				 dir = 'E'; 
				 break;
			 case 'E': 
				 dir = 'S'; 
				 break;
			 case 'S': 
				 dir = 'W';
				 break;
			 case 'W':
				 dir = 'N';
				 break;
		 }
		 
	 }
 
 
 
	 private void goForward() {
		 if (dir == 'E') {
			 currY += 1;
		 } else if (dir == 'N') {
			 currX += 1;
		 } else if (dir == 'W') {
			 currY -= 1;
		 } else {
			 currX -= 1;
		 }
		 newWorld[currX][currY] = 50;
	 }
 
 
	 private void updateAgent(int nextMove) {
		 if (nextMove == Action.TURN_LEFT) {
			 turnLeft();
		 }
		 if (nextMove == Action.TURN_RIGHT) {
			 turnRight();
		 }
		 if (nextMove == Action.GO_FORWARD) {
			 goForward();
		 }
		 
		 if (nextMove == Action.SHOOT) {
			 arrowUsed = true;
			 int new_x = currX;
			 int new_y = currY;
			 if (dir == 'E') {
				 new_y += 1;
			 } else if (dir == 'N') {
				 new_x += 1;
			 } else if (dir == 'W') {
				 new_y -= 1;
			 } else {
				 new_x -= 1;
			 }
			 if (isValid(new_x, new_y))
			 newWorld[new_x][new_y] = 0;
		 }
	 }
 
	 private void countPits() {
		 if (pitLocations.size() >= 2) {
			 for (IntPair x : pitLocations) {
				 boolean flag = false;
				 for (IntPair y: pitLocations) {
					 if (!x.equals(y)) {
						 int distance = Math.abs(x.first - y.first) + Math.abs(x.second - y.second);
						 if (distance <= 2)
							 flag = true;
					 }
				 }
				 if (flag == false) {
					 pitsDiscovered = true;
					 return;
				 }
			 }
		 }
	 }
 
 
	 public int process(TransferPercept tp)
	 {
		 // To build your own intelligent agent, replace
		 // all code below this comment block. You have
		 // access to all percepts through the object
		 // 'tp' as illustrated here:
		 // read in the current percepts
		 bump = tp.getBump();
		 glitter = tp.getGlitter();
		 breeze = tp.getBreeze();
		 stench = tp.getStench();
		 scream = tp.getScream();
		 timestamp++;
 
		 countPits();
 
		 if (stench) {
			 boolean alsoBreeze = false;
			 if (breeze)
				 alsoBreeze = true;
			 markWumpus(alsoBreeze);
		 }
		 if (breeze) {
			 markPit();
		 }
		 if (glitter) {
			 markGold();
		 }
		 if (!breeze && !stench) {
			 markSafe();
		 }
		 if (scream) {
			 removeWumpus();
		 }
		 try {
			 BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outFilename, true));
			 outputWriter.write("Search at timestamp"+timestamp+"\n");
			 outputWriter.close();
		 } catch (Exception e) {
			 System.out.println("An exception was thrown: " + e);
		 }
		 int nextMove = new ForwardSearch(probabilityOfPit, newWorld, currX, currY, dir, arrowUsed).nextMove();
		 updateAgent(nextMove);
 
		 
		 lastMove = nextMove;
		 return nextMove;
	 }
	 
	 // public method to return the agent's name
	 // do not remove this method
	 public String getAgentName() {
		 return agentName;
	 }
 }