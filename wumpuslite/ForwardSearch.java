import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

class ForwardSearch {
    private int[] possibleEnv = {1, 2, 0};
	private int[] moves = {Action.NO_OP, Action.TURN_LEFT, Action.TURN_RIGHT, Action.GO_FORWARD, Action.SHOOT, Action.GRAB};
    private int bestMove = Action.NO_OP;
    private int maxUtility, numberOfMoves;
    

    public ForwardSearch(int[][] probabilityOfPit, int[][] newWorld, int x, int y, char dir, boolean arrowUsed) {
        maxUtility = 0;
        numberOfMoves = 99999;
        dfs(new State(probabilityOfPit, newWorld, dir, x, y, arrowUsed, false), 0, 0, new ArrayList<>());
    }

    public int nextMove() {
        System.out.println("Max utility " + maxUtility + "Number of Moves " + numberOfMoves);
        return bestMove;
    }

    private boolean isValid(int x, int y) {
		if (x >= 0 && x < 4 && y >= 0 && y < 4)
			return true;
		return false;
	}

    private char turnLeft(char dir) {
        switch (dir) {
            case 'N': dir = 'W'; break;
            case 'W': dir = 'S'; break;
            case 'S': dir = 'E'; break;
            case 'E': dir = 'N'; break;
        }
        return dir;
    }

    private char turnRight(char dir) {
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
        return dir;
    }

	
    private boolean checkForwardMove(State currState) {
        int x = currState.x;
        int y = currState.y;
        int direction = currState.direction;
		int new_x = x;
		int new_y = y;
		if (direction == 'E') {
			new_y += 1;
		} else if (direction == 'N') {
			new_x += 1;
		} else if (direction == 'W') {
			new_y -= 1;
		} else {
			new_x -= 1;
		}
        
		if (isValid(new_x, new_y)) {
			return true;
		}
        return false;
    }

    private State shootDone(State currState) {
        State newState = new State(currState.knowledgeOfPit, currState.newWorld, currState.direction, currState.x, currState.y, true, currState.grabPerformed);
        int x = currState.x;
        int y = currState.y;
        int direction = currState.direction;
		int new_x = x;
		int new_y = y;
		if (direction == 'E') {
			new_y += 1;
		} else if (direction == 'N') {
			new_x += 1;
		} else if (direction == 'W') {
			new_y -= 1;
		} else {
			new_x -= 1;
		}
        if (isValid(new_x, new_y)) {
            if (newState.newWorld[new_x][new_y] == 2)
                newState.newWorld[new_x][new_y] = 0;
        }
            
        return newState;
    }

    private int[] goForward(State currState) {
        int x = currState.x;
        int y = currState.y;
        char direction = currState.direction;
		int new_x = x;
		int new_y = y;
		if (direction == 'E') {
			new_y += 1;
		} else if (direction == 'N') {
			new_x += 1;
		} else if (direction == 'W') {
			new_y -= 1;
		} else {
			new_x -= 1;
		}
        return new int[]{new_x, new_y};
    }

    private List<State> generatePossibleStates(State currState, int x, int y) {
        List<State> possibleStates = new ArrayList<State>();
        if (currState.newWorld[x][y] == 0) {
            for (int i = 0; i < possibleEnv.length; i++) {
                State newState = new State(currState.knowledgeOfPit, currState.newWorld, currState.direction, x, y, currState.arrowUsed, currState.grabPerformed);
                newState.newWorld[x][y] = possibleEnv[i];
                possibleStates.add(newState);
               
            }
        }
        if (currState.newWorld[x][y] == 50 || currState.newWorld[x][y] == 0) {
            possibleStates.add(new State(currState.knowledgeOfPit, currState.newWorld, currState.direction, x, y, currState.arrowUsed, currState.grabPerformed));
        }
        return possibleStates;
    }

    private void dfs(State currState, int depth, int utility, List<Integer> listOfMoves) {
        if (utility >= maxUtility) {
            if (listOfMoves.size() != 0 && listOfMoves.size() < numberOfMoves)
                bestMove = listOfMoves.get(0);
            numberOfMoves = listOfMoves.size();
            maxUtility = utility;
        }
        currState.print();
        try {
		   
        } catch (Exception e) {
	    	System.out.println("An exception was thrown: " + e);
	    }
        if (depth == 10) {
            return;
        }
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == Action.GO_FORWARD) {
                if (checkForwardMove(currState)) {
                    int new_x = goForward(currState)[0];
                    int new_y =  goForward(currState)[1];
                    listOfMoves.add(Action.GO_FORWARD);
                    List<State> listOfStates = generatePossibleStates(currState, new_x, new_y);
                    for (int j = 0; j < listOfStates.size(); j++) {
                        dfs(listOfStates.get(j), depth + 1, listOfStates.get(j).getUtility(new_x, new_y) - (depth + 1), listOfMoves);
                    }
                    listOfMoves.remove(listOfMoves.size() - 1);
                }
            }
            if (moves[i] == Action.TURN_LEFT) {
                listOfMoves.add(Action.TURN_LEFT);
                dfs(new State(currState.knowledgeOfPit, currState.newWorld, turnLeft(currState.direction), currState.x, currState.y, currState.arrowUsed, false), depth + 1, currState.getUtility(currState.x, currState.y) - (depth + 1), listOfMoves);
                listOfMoves.remove(listOfMoves.size() - 1);
            }
            if (moves[i] == Action.TURN_RIGHT) {
                listOfMoves.add(Action.TURN_RIGHT);
                dfs(new State(currState.knowledgeOfPit, currState.newWorld, turnRight(currState.direction), currState.x, currState.y, currState.arrowUsed, false), depth + 1, currState.getUtility(currState.x, currState.y) - (depth + 1), listOfMoves);
                listOfMoves.remove(listOfMoves.size() - 1);
            }
           
            if (moves[i] == Action.SHOOT && currState.arrowUsed == false) {
                listOfMoves.add(Action.SHOOT);
                State newState = shootDone(currState);
                dfs(newState, depth + 1, newState.getUtility(currState.x, currState.y) - 10, listOfMoves);
                listOfMoves.remove(listOfMoves.size() - 1);
            }
            if (moves[i] == Action.GRAB) {
                listOfMoves.add(Action.GRAB);
                State newState = new State(currState.knowledgeOfPit, currState.newWorld, currState.direction, currState.x, currState.y, currState.arrowUsed, true);
                dfs(newState, depth + 1, currState.getUtility(currState.x, currState.y) - 1000, listOfMoves);
                listOfMoves.remove(listOfMoves.size() - 1);
            }
        }
    }

}