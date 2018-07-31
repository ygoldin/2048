import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
		int firstPlacement = getRandomOpenSpot();
		openSpots.remove(firstPlacement);
		int secondPlacement = getRandomOpenSpot();
		openSpots.remove(secondPlacement);
		
		int[] firstLocation = locationFromSetValue(firstPlacement);
		int[] secondLocation = locationFromSetValue(secondPlacement);
		board[firstLocation[0]][firstLocation[1]] = SMALLEST_VALUE;
		board[secondLocation[0]][secondLocation[1]] = SMALLEST_VALUE;
	}
	
	public int valueAtLocation(int row, int col) {
		if(row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
			throw new IllegalArgumentException();
		}
		return board[row][col];
	}
	
	public void moveRight() {
		for(int row = 0; row < SIZE; row++) {
			Set<Integer> columns = columnsOfValuesInRow(row);
			if(columns.isEmpty()) {
				continue;
			}
			if(columns.size() == 1) {
				int col = columns.iterator().next();
				if(col == SIZE - 1) {
					continue;
				}
				int value = board[row][col];
				openSpots.add(setValueFromLocation(row, col)); //re-open the spot
				board[row][col] = 0;
				openSpots.remove(setValueFromLocation(row, SIZE - 1)); //close the new spot
				board[row][SIZE - 1] = value;
				continue;
			}
		}
	}
	
	//returns a set of all of the occupied columns in the row
	private Set<Integer> columnsOfValuesInRow(int row) {
		Set<Integer> columns = new HashSet<>();
		for(int c = 0; c < SIZE; c++) {
			if(board[row][c] != 0) {
				columns.add(c);
			}
		}
		return columns;
	}
	
	private int[] locationFromSetValue(int n) {
		return new int[] {n/SIZE, n%SIZE};
	}
	
	private int setValueFromLocation(int row, int col) {
		return row*SIZE + col;
	}
	
	private int getRandomOpenSpot() {
		int result = rand.nextInt(SIZE*SIZE);
		while(!openSpots.contains(result)) {
			result = rand.nextInt(SIZE*SIZE);
		}
		return result;
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
