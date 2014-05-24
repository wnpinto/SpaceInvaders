
import java.awt.Color;

import javax.swing.JFrame;

public class SpaceInavderViewer {

	/**
	 * Create a new game which will create a Jframe and the objects the game requires
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game g = new Game(700, 700);

		g.gameLoop();
	}

}
