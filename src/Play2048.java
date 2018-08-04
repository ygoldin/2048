import java.awt.EventQueue;

import javax.swing.JFrame;

public class Play2048 {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame play2048 = new Frame_2048();
				play2048.pack();
				play2048.setVisible(true);
			}
		});
	}

}
