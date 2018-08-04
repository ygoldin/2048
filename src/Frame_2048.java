import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class Frame_2048 extends JFrame {
	
	private Grid grid2048;
	private GridSpot[][] tiles;
	private KeyListener keyListener;

	public Frame_2048() {
		grid2048 = new Grid();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1024, 768));
		setTitle("2048");
		
		keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()== KeyEvent.VK_RIGHT)
		            grid2048.moveRight();
		        else if(e.getKeyCode()== KeyEvent.VK_LEFT)
		        	grid2048.moveLeft();
		        else if(e.getKeyCode()== KeyEvent.VK_DOWN)
		        	grid2048.moveDown();
		        else if(e.getKeyCode()== KeyEvent.VK_UP)
		        	grid2048.moveUp();
				
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		};
		
		JPanel mainGridTilePanel = new JPanel();
		mainGridTilePanel.setLayout(new GridLayout(Grid.SIZE, Grid.SIZE));
		tiles = new GridSpot[Grid.SIZE][Grid.SIZE];
		for(int r = 0; r < Grid.SIZE; r++) {
			for(int c = 0; c < Grid.SIZE; c++) {
				tiles[r][c] = new GridSpot(r, c);
				mainGridTilePanel.add(tiles[r][c]);
			}
		}
		mainGridTilePanel.addKeyListener(keyListener);
		add(mainGridTilePanel);
		addKeyListener(keyListener);
	}
	
	//this class represents one of the buttons on the 2048 grid
	private class GridSpot extends JButton {
		private static final int FONT_SIZE = 80;
		private static final String FONT_NAME = "Arial";
		private int row;
		private int col;
		
		public GridSpot(int row, int col) {
			super();
			setOpaque(true);
			setRolloverEnabled(false);
			setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
			this.row = row;
			this.col = col;
			addKeyListener(keyListener);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int value = grid2048.valueAtLocation(row, col);
			if(value != 0) {
				setText("" + value);
			} else {
				setText(null);
			}
		}
	}
}
