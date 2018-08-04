import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Grid grid = new Grid();
		System.out.println(grid);
		Scanner input = new Scanner(System.in);
		while(!grid.isGameOver()) {
			System.out.print("Move in what direction: left(1), right(2), up(3), down(4)? ");
			int direction = input.nextInt();
			if(direction == 1) {
				grid.moveLeft();
			} else if(direction == 2) {
				grid.moveRight();
			} else if(direction == 3) {
				grid.moveUp();
			} else if(direction == 4) {
				grid.moveDown();
			}
			System.out.println(grid);
		}
		input.close();
	}
}
