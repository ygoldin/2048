import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Grid {
	private int[][] board;
	public static final int SIZE = 4;
	private static final int SMALLEST_VALUE = 2;
	private Random rand;
	private Set<Integer> openSpots;
	
	public Grid() {
		board = new int[SIZE][SIZE];
		rand = new Random();
		openSpots = new HashSet<>();
		for(int i = 0; i < SIZE*SIZE; i++) {
			openSpots.add(i);
		}
		placeNewValueRandomly();
		placeNewValueRandomly();
	}
	
	public int valueAtLocation(int row, int col) {
		if(row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
			throw new IllegalArgumentException();
		}
		return board[row][col];
	}
	
	public void moveRight() {
		for(int row = 0; row < SIZE; row++) {
			List<Integer> columns = columnsOfValuesInRow(row);
			if(columns.isEmpty()) {
				continue;
			}
			if(columns.size() == 1) {
				int col = columns.get(0);
				if(col == SIZE - 1) {
					continue;
				}
				int value = board[row][col];
				openSpots.add(setValueFromLocation(row, col)); //re-open the spot
				board[row][col] = 0;
				openSpots.remove(setValueFromLocation(row, SIZE - 1)); //close the new spot
				board[row][SIZE - 1] = value;
			} else {
				for(int i = columns.size() - 1; i >= 0; i--) {
					int col1 = columns.get(i);
					int val1 = board[row][col1];
					if(i > 0) {
						int col2 = columns.get(i - 1);
						int val2 = board[row][col2];
						if(val1 == val2) {
							int newCol = furthestRightOpenColumn(row);
							if(newCol < col1) {
								newCol = col1;
							}
							openSpots.add(setValueFromLocation(row, col1)); //re-open the spot
							board[row][col1] = 0;
							openSpots.add(setValueFromLocation(row, col2)); //re-open the spot
							board[row][col2] = 0;
							openSpots.remove(setValueFromLocation(row, newCol)); //close the new spot
							board[row][newCol] = val1 + val2;
							i--; //skip looking at the left-moved column again
						}
					}
				}
			}
		}
		placeNewValueRandomly();
	}
	
	private void placeNewValueRandomly() {
		if(openSpots.isEmpty()) {
			throw new IllegalStateException("no empty spots");
		}
		int randomSpot = rand.nextInt(SIZE*SIZE);
		while(!openSpots.contains(randomSpot)) {
			randomSpot = rand.nextInt(SIZE*SIZE);
		}
		openSpots.remove(randomSpot);
		int[] location = locationFromSetValue(randomSpot);
		board[location[0]][location[1]] = SMALLEST_VALUE;
	}
	
	//returns a list of all of the occupied columns in the row
	private List<Integer> columnsOfValuesInRow(int row) {
		List<Integer> columns = new ArrayList<>();
		for(int c = 0; c < SIZE; c++) {
			if(board[row][c] != 0) {
				columns.add(c);
			}
		}
		return columns;
	}
	
	private int furthestRightOpenColumn(int row) {
		for(int c = SIZE - 1; c >= 0; c--) {
			if(board[row][c] == 0) {
				return c;
			}
		}
		return -1;
	}
	
	private int[] locationFromSetValue(int n) {
		return new int[] {n/SIZE, n%SIZE};
	}
	
	private int setValueFromLocation(int row, int col) {
		return row*SIZE + col;
	}
	
	public String toString() {
		String result = "";
		for(int r = 0; r < SIZE; r++) {
			result += "[" + spacedOutValue(board[r][0]);
			for(int c = 1; c < SIZE; c++) {
				result += ", " + spacedOutValue(board[r][c]);
			}
			result += "]\n";
		}
		return result;
	}
	
	private String spacedOutValue(int n) {
		if(n == 0) {
			return "    ";
		}
		String result = "" + n;
		if(n < 1000) {
			result += " ";
			if(n < 100) {
				result += " ";
				if(n < 10) {
					result += " ";
				}
			}
		}
		return result;
	}
}
