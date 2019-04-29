import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class Frame_2048 extends JFrame {
	
	private Grid grid2048;
	private GridSpot[][] tiles;
	private KeyListener keyListener;
	private boolean playPast2048;

	public Frame_2048() {
		grid2048 = new Grid();
		playPast2048 = false;
		
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
				if(grid2048.isGameOver() && !playPast2048) {
					gameOverActions();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
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
	
	private void gameOverActions() {
		String message;
		if(grid2048.reached2048()) {
			message = "Victory!";
		} else {
			message = "Game Over";
		}
		
		if(JOptionPane.showConfirmDialog(this, "Play again?", message, JOptionPane.YES_NO_OPTION)
				== JOptionPane.YES_OPTION) { //play again
			grid2048 = new Grid();
			for(int r = 0; r < Grid.SIZE; r++) {
				for(int c = 0; c < Grid.SIZE; c++) {
					tiles[r][c] = new GridSpot(r, c);
				}
			}
			repaint();
		} else if(grid2048.reached2048()) {
			playPast2048 = true;
		}
	}
	
	//this class represents one of the buttons on the 2048 grid
	private class GridSpot extends JButton {
		private static final int FONT_SIZE = 80;
		private static final String FONT_NAME = "Arial";
		private int row;
		private int col;
		private final Color defaultColor = new Color(224, 224, 224);
		private final Color[] colors = {new Color(255, 153, 255), new Color(204, 153, 255),
				new Color(153, 153, 255), new Color(153, 204, 255), new Color(153, 255, 255),
				new Color(153, 255, 204), new Color(153, 255, 153), new Color(204, 255, 153),
				new Color(255, 255, 153), new Color(255, 204, 153), new Color(255, 153, 153)};
		
		public GridSpot(int row, int col) {
			super();
			setOpaque(true);
			setRolloverEnabled(false);
			setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
			//setForeground(Color.WHITE);
			this.row = row;
			this.col = col;
			addKeyListener(keyListener);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int value = grid2048.valueAtLocation(row, col);
			if(value != 0) {
				setText("" + value);
				setBackground(colors[powerOfTwo(value) - 1]);
			} else {
				setText(null);
				setBackground(defaultColor);
			}
		}
		
		private int powerOfTwo(int value) {
			int power = 1;
			while(value > 2) {
				value = value/2;
				power++;
			}
			return power;
		}
	}
}
