import java.awt.*;
import javax.swing.*;

public class Frame_2048 extends JFrame {
	
	private Grid grid2048;
	private GridSpot[][] tiles;

	public Frame_2048() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1024, 768));
		setTitle("2048");
		
		JPanel mainGridTilePanel = new JPanel();
		mainGridTilePanel.setLayout(new GridLayout(Grid.SIZE, Grid.SIZE));
		tiles = new GridSpot[Grid.SIZE][Grid.SIZE];
		for(int r = 0; r < Grid.SIZE; r++) {
			for(int c = 0; c < Grid.SIZE; c++) {
				tiles[r][c] = new GridSpot(r, c);
				mainGridTilePanel.add(tiles[r][c]);
			}
		}
		add(mainGridTilePanel);
	}
	
	//this class represents one of the buttons on the tic tac toe grid
	private class GridSpot extends JButton {
		private static final int FONT_SIZE = 80;
		private static final String FONT_NAME = "Arial";
		
		public GridSpot(int row, int col) {
			setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
			
		}
	}
}
