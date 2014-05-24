import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

/* Input class is responsible for mouse movement which operates the player(ship)*/

public class Input {
	private Screen screen;
	private Ship ship;
	private Robot robot;
	private Boolean isMouseRepos;

	public Input(Screen screen) {
		this.ship = null;
		class Key implements KeyListener {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		}

		KeyListener key = new Key();
		this.screen.addKeyListener(key);
	}

/*
 * 	Method that operates based on the movement of the mouse. Each mouse movement leads to a action.
 */

	public Input(final Screen screen, final Ship ship) {
		this.ship = ship;
		isMouseRepos = false;

		try {
			robot = new Robot();
		} catch (Exception e) {
			// Handle excep
			System.out.println("Robot threw exception");
		}

		class MouseL implements MouseListener {
			@Override
			public void mouseClicked(MouseEvent e) {
				ship.shoot();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				ship.shoot();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setPrevX(e.getX());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (screen instanceof Game && !((Game) screen).isGameOver()) {
					Point copy = new Point((int) ship.getXPos(),
							screen.getEnvironmentHeight() / 2);
					SwingUtilities.convertPointToScreen(copy, screen);
					robot.mouseMove(copy.x, copy.y);
					setPrevX(ship.getXPos());
					isMouseRepos = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		}

		class MouseM implements MouseMotionListener {
			@Override
			public void mouseDragged(MouseEvent e) {
				moveShip(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (!isMouseRepos)
					moveShip(e);
				else
					isMouseRepos = false;
			}
		}

		class Key implements KeyListener {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		}

		MouseListener mouseListener = new MouseL();
		MouseMotionListener mouseMotion = new MouseM();
		KeyListener key = new Key();

		screen.addMouseListener(mouseListener);
		screen.addMouseMotionListener(mouseMotion);
		screen.addKeyListener(key);
	}

	/*
	 * Set the ships previous position so we can calculate displacement
	 */
	private void setPrevX(double x) {
		ship.setPrevMouseX(x);
	}

	/*
	 * Move ship based on displacement of current mouse position vs prev mouse position
	 */
	private void moveShip(MouseEvent e) {
		double x = (double) e.getX();
		double y = (double) e.getY();

		ship.playerMove(x, y);

	}
}
