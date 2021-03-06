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
	private boolean moveMade;
	private boolean reached2048;
	
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
	
	public boolean isGameOver() {
		if(reached2048) {
			return true;
		}
		if(!openSpots.isEmpty()) {
			return false;
		}
		
		for(int r = 0; r < SIZE - 1; r++) { //check down
			for(int c = 0; c < SIZE; c++) {
				if(board[r][c] == board[r + 1][c]) {
					return false;
				}
			}
		}
		for(int c = 0; c < SIZE - 1; c++) { //check down
			for(int r = 0; r < SIZE; r++) {
				if(board[r][c] == board[r][c + 1]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean reached2048() {
		return reached2048;
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
			if(!finishedMoveWithZeroOrOneValuesInRow(columns, row, SIZE - 1)) {
				for(int i = columns.size() - 1; i >= 0; i--) {
					int col1 = columns.get(i);
					int val1 = board[row][col1];
					int newCol = furthestRightOpenColumn(row);
					if(newCol < col1) {
						newCol = col1;
					}
					if(i > 0) { //could potentially combine two spots
						int col2 = columns.get(i - 1);
						int val2 = board[row][col2];
						if(val1 == val2) {
							combineTwoSpots(row, col1, row, col2, row, newCol, val1 + val2);
							moveMade = true;
							i--; //skip looking at the left-moved column again
						} else { 
							moveValueOver(row, col1, row, newCol, val1);
							moveMade = moveMade || col1 != newCol;
						}
					} else {
						moveValueOver(row, col1, row, newCol, val1); //can't combine with other spot
						moveMade = moveMade || col1 != newCol;
					}
				}
			}
		}
		endOfMove();
	}
	
	public void moveLeft() {
		for(int row = 0; row < SIZE; row++) {
			List<Integer> columns = columnsOfValuesInRow(row);
			if(!finishedMoveWithZeroOrOneValuesInRow(columns, row, 0)) {
				for(int i = 0; i < columns.size(); i++) {
					int col1 = columns.get(i);
					int val1 = board[row][col1];
					int newCol = furthestLeftOpenColumn(row);
					if(newCol > col1 || newCol < 0) { //open col is to the right or all col are full
						newCol = col1;
					}
					if(i < columns.size() - 1) { //could potentially combine two spots
						int col2 = columns.get(i + 1);
						int val2 = board[row][col2];
						if(val1 == val2) {
							combineTwoSpots(row, col1, row, col2, row, newCol, val1 + val2);
							moveMade = true;
							i++; //skip looking at the right-moved column again
						} else { 
							moveValueOver(row, col1, row, newCol, val1);
							moveMade = moveMade || col1 != newCol;
						}
					} else {
						moveValueOver(row, col1, row, newCol, val1); //can't combine with other spot
						moveMade = moveMade || col1 != newCol;
					}
				}
			}
		}
		endOfMove();
	}
	
	public void moveDown() {
		for(int col = 0; col < SIZE; col++) {
			List<Integer> rows = rowsOfValuesInColumn(col);
			if(!finishedMoveWithZeroOrOneValuesInCol(rows, col, SIZE - 1)) {
				for(int i = rows.size() - 1; i >= 0; i--) {
					int row1 = rows.get(i);
					int val1 = board[row1][col];
					int newRow = furthestDownOpenRow(col);
					if(newRow < row1) { //open row is up or all rows are full
						newRow = row1;
					}
					if(i > 0) { //could potentially combine two spots
						int row2 = rows.get(i - 1);
						int val2 = board[row2][col];
						if(val1 == val2) {
							combineTwoSpots(row1, col, row2, col, newRow, col, val1 + val2);
							moveMade = true;
							i--; //skip looking at the bottom-moved row again
						} else { 
							moveValueOver(row1, col, newRow, col, val1);
							moveMade = moveMade || row1 != newRow;
						}
					} else {
						moveValueOver(row1, col, newRow, col, val1); //can't combine with other spot
						moveMade = moveMade || row1 != newRow;
					}
				}
			}
		}
		endOfMove();
	}
	
	public void moveUp() {
		for(int col = 0; col < SIZE; col++) {
			List<Integer> rows = rowsOfValuesInColumn(col);
			if(!finishedMoveWithZeroOrOneValuesInCol(rows, col, 0)) {
				for(int i = 0; i < rows.size(); i++) {
					int row1 = rows.get(i);
					int val1 = board[row1][col];
					int newRow = furthestUpOpenRow(col);
					if(newRow > row1 || newRow < 0) { //open row is down or all rows are full
						newRow = row1;
					}
					if(i < rows.size() - 1) { //could potentially combine two spots
						int row2 = rows.get(i + 1);
						int val2 = board[row2][col];
						if(val1 == val2) {
							combineTwoSpots(row1, col, row2, col, newRow, col, val1 + val2);
							moveMade = true;
							i++; //skip looking at the bottom-moved row again
						} else { 
							moveValueOver(row1, col, newRow, col, val1);
							moveMade = moveMade || row1 != newRow;
						}
					} else {
						moveValueOver(row1, col, newRow, col, val1); //can't combine with other spot
						moveMade = moveMade || row1 != newRow;
					}
				}
			}
		}
		endOfMove();
	}
	
	private void endOfMove() {
		if(moveMade) {
			placeNewValueRandomly();
		}
		moveMade = false;
	}
	
	private boolean finishedMoveWithZeroOrOneValuesInRow(List<Integer> columns, int row,
			int colOnIntendedSide) {
		if(columns.isEmpty()) {
			return true;
		}
		if(columns.size() == 1) {
			int col = columns.get(0);
			if(col != colOnIntendedSide) {
				moveValueOver(row, col, row, colOnIntendedSide, board[row][col]);
				moveMade = true;
			}
			return true;
		}
		return false;
	}
	
	private boolean finishedMoveWithZeroOrOneValuesInCol(List<Integer> rows, int col,
			int rowOnIntendedSide) {
		if(rows.isEmpty()) {
			return true;
		}
		if(rows.size() == 1) {
			int row = rows.get(0);
			if(row != rowOnIntendedSide) {
				moveValueOver(row, col, rowOnIntendedSide, col, board[row][col]);
				moveMade = true;
			}
			return true;
		}
		return false;
	}
	
	private void moveValueOver(int startRow, int startCol, int endRow, int endCol, int val) {
		openSpot(startRow, startCol);
		closeSpot(endRow, endCol, val);
	}
	
	private void combineTwoSpots(int row1, int col1, int row2, int col2, int newRow, int newCol, int newVal) {
		openSpot(row1, col1);
		openSpot(row2, col2);
		closeSpot(newRow, newCol, newVal);
		if(newVal == 2048) {
			reached2048 = true;
		}
	}
	
	private void openSpot(int row, int col) {
		openSpots.add(setValueFromLocation(row, col));
		board[row][col] = 0;
	}
	
	private void closeSpot(int row, int col, int value) {
		openSpots.remove(setValueFromLocation(row, col));
		board[row][col] = value;
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
	
	//returns a list of all of the occupied rows in the column
	private List<Integer> rowsOfValuesInColumn(int col) {
		List<Integer> rows = new ArrayList<>();
		for(int r = 0; r < SIZE; r++) {
			if(board[r][col] != 0) {
				rows.add(r);
			}
		}
		return rows;
	}
	
	private int furthestRightOpenColumn(int row) {
		for(int c = SIZE - 1; c >= 0; c--) {
			if(board[row][c] == 0) {
				return c;
			}
		}
		return -1;
	}
	
	private int furthestLeftOpenColumn(int row) {
		for(int c = 0; c < SIZE; c++) {
			if(board[row][c] == 0) {
				return c;
			}
		}
		return -1;
	}
	
	private int furthestDownOpenRow(int col) {
		for(int r = SIZE - 1; r >= 0; r--) {
			if(board[r][col] == 0) {
				return r;
			}
		}
		return -1;
	}
	
	private int furthestUpOpenRow(int col) {
		for(int r = 0; r < SIZE; r++) {
			if(board[r][col] == 0) {
				return r;
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
