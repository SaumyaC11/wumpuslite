/*
 * Class that defines the agent.
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 * 
 * Last modified 3/5/07
 * 
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 * 
 */

class Agent {
	
	private double forwardProbability;
	
	private int[] location;
	private char direction;
	private char agentIcon;
	
	private int numArrows = 1;
	private int worldSize;
	
	private boolean isDead;
	private boolean hasGold;
	
	private Environment wumpusWorld;
	private TransferPercept percept;
	private AgentFunction agentFunction;
	
	public Agent(Environment world, TransferPercept perceptTrans, double forwardProbability) {
				
		// set deterministic/non-deterministic
		this.forwardProbability = forwardProbability;
		
		// initial conditions
		isDead = false;
		hasGold = false;
		
		wumpusWorld = world;
		agentFunction = new AgentFunction();
		percept = perceptTrans;
		
		worldSize = wumpusWorld.getWorldSize();
		
		// initial location
		location = wumpusWorld.getAgentLocation();
		direction = wumpusWorld.getAgentDirection();
		setDirection(direction);
	}
	
	public void setIsDead(boolean dead) {
		isDead = dead;
	}
	
	public boolean getIsDead() {
		return isDead;
	}
	
	public void setHasGold(boolean possessGold) {
		hasGold = possessGold;
	}
	
	public boolean getHasGold() {
		return hasGold;
	}
	
	public String getName() {
		return agentFunction.getAgentName();
	}
	
	public int chooseAction() {
		return agentFunction.process(percept);
	}
	
	public char getAgentIcon() {
		return agentIcon;
	}
	
	public void goForward() {
		
		if (forwardProbability == 1) {
		
			if (direction == 'N') {
				if (location[0]+1 < worldSize) location[0] += 1;
				else wumpusWorld.setBump(true);
			}
			else if (direction == 'E') {
				if (location[1]+1 < worldSize) location[1] += 1;
				else wumpusWorld.setBump(true);
			}
			else if (direction == 'S') {
				if (location[0]-1 >= 0) location[0] -= 1;
				else wumpusWorld.setBump(true);
			}
			else if (direction == 'W') {
				if (location[1]-1 >= 0) location[1] -= 1;
				else wumpusWorld.setBump(true);
			}
		}
		else {
			
			char moveDirection = nonDeterministicMove();
			
			if (direction == 'N') {
				
				if (moveDirection == 'F') {
					if (location[0]+1 < worldSize) location[0] += 1;
					else wumpusWorld.setBump(true);					
				}
				else if (moveDirection == 'L') {
					if (location[1]-1 >= 0) location[1] -= 1;
					else wumpusWorld.setBump(true);
				}
				else if (moveDirection == 'R') {
					if (location[1]+1 < worldSize) location[1] += 1;
					else wumpusWorld.setBump(true);
				}
			}
			else if (direction == 'E') {
				
				if (moveDirection == 'F') {
					if (location[1]+1 < worldSize) location[1] += 1;
					else wumpusWorld.setBump(true);	
				}
				else if (moveDirection == 'L') {
					if (location[0]+1 < worldSize) location[0] += 1;
					else wumpusWorld.setBump(true);
				}
				else if (moveDirection == 'R') {
					if (location[0]-1 >= 0) location[0] -= 1;
					else wumpusWorld.setBump(true);
				}
			}
			else if (direction == 'S') {
				
				if (moveDirection == 'F') {
					if (location[0]-1 >= 0) location[0] -= 1;
					else wumpusWorld.setBump(true);					
				}
				else if (moveDirection == 'L') {
					if (location[1]+1 < worldSize) location[1] += 1;
					else wumpusWorld.setBump(true);
				}
				else if (moveDirection == 'R') {
					if (location[1]-1 >= 0) location[1] -= 1;
					else wumpusWorld.setBump(true);
				}
			}
			else if (direction == 'W') {
				
				if (moveDirection == 'F') {
					if (location[1]-1 >= 0) location[1] -= 1;
					else wumpusWorld.setBump(true);					
				}
				else if (moveDirection == 'L') {
					if (location[0]-1 >= 0) location[0] -= 1;
					else wumpusWorld.setBump(true);
				}
				else if (moveDirection == 'R') {
					if (location[0]+1 < worldSize) location[0] += 1;
					else wumpusWorld.setBump(true);
				}
			}
		}
	}
	
	private char nonDeterministicMove() {
		double halfRemainder = (1 - forwardProbability)/2D;
		double rand = Math.random();

		if(rand < forwardProbability)
			return 'F';
		else if (rand >= forwardProbability && rand < forwardProbability + halfRemainder)
			return 'L';
		else
			return 'R';
	}
	
	public boolean shootArrow() {
		
		if (numArrows == 1) {
			numArrows -= 1;
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public void turnRight() {
		
		if (direction == 'N') setDirection('E');
		else if (direction == 'E') setDirection('S');
		else if (direction == 'S') setDirection('W');
		else if (direction == 'W') setDirection('N');
		
	}
	
	public void turnLeft() {
		
		if (direction == 'N') setDirection('W');
		else if (direction == 'E') setDirection('N');
		else if (direction == 'S') setDirection('E');
		else if (direction == 'W') setDirection('S');
		
	}
	
	public void setDirection(char newDirection) {
		direction = newDirection;
		
		if (direction == 'N') agentIcon = 'A';
		if (direction == 'E') agentIcon = '>';
		if (direction == 'S') agentIcon = 'V';
		if (direction == 'W') agentIcon = '<';
		
	}
	
	public char getDirection() {
		return direction;
	}
	
	public void setLocation(int[] newLocation) {
		location[0] = newLocation[0];
		location[1] = newLocation[1];
	}
	
	public int[] getLocation() {
		return location;
	}
	
}