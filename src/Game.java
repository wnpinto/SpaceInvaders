import java.io.*;
import java.awt.*;
import javax.swing.*;
import sun.audio.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/* Game class is responsible for creating the characters, informed
 * when entities within the game are killed or when power up is added and 
 * will take appproiate game actions, and to display High score.*/

public class Game extends Screen {
	private final Ship ship;
	private ArrayList<Alien> aliens; // array of aliens
	private PowerUp powUp;

	private int highScore; // highscores
	private boolean isGameOver;
	private boolean playerWon;
	private ArrayList<Ellipse2D.Double> stars;


	public Game(int environmentWidth, int environmentHeight) {
		super(environmentWidth, environmentHeight);
		isGameOver = false;
		playerWon = false;
		aliens = new ArrayList<Alien>();
		int x = 10;

		int shipHeight = 15;
		this.ship = new Ship(this, 0, getEnvironmentHeight() - shipHeight, 45,
				shipHeight, 0, 0); // create player ship.

		for (int count = 0; count < 5; count++) {
			aliens.add(new Alien(this,
					(int) (Math.random() * 0.9 * getEnvironmentWidth()),
					(int) (Math.random() * 0.5 * getEnvironmentHeight()), 20,
					20, 10, 10)); // create aliens. Aliens y-position is
									// randomized in top half of game, aliens
									// x-pos in first 90%
		}

		int amountStars = (environmentWidth * environmentHeight) / 100;
		ArrayList<Ellipse2D.Double> tmp = new ArrayList<Ellipse2D.Double>();

		for (int count = 0; count < amountStars; count++)
			tmp.add(new Ellipse2D.Double(
					(int) (Math.random() * environmentWidth) + 0, (int) (Math
							.random() * environmentHeight) + 0, 1, 1));

		stars = tmp;

		Input input = new Input(this, ship);
	}

/*
 * The game loop this is the main loop that is running during game play. This method is responsible for checking how many aliens are still
 * alive on the field, if there are non then end of game. To check whether the user (ship) is alive or not.
 */
	public void gameLoop() {
		while (!isGameOver) {
			repaint();

			// Check powUp
			if (powUp != null && powUp.isHit()) {
				powUp = null;
			} else if (powUp == null && (int) (Math.random() * 50) == 1) {
				powUp = new PowerUp(this,
						(int) (Math.random() * getEnvironmentWidth()),
						(int) (Math.random() * getEnvironmentHeight()), 20, 20,
						10, 10);
			}


			if (aliens.size() <= 0) { // this if statement determines if there
										// arent any aliens on the field, then
										// end game.
				isGameOver = true;
				playerWon = true;
				repaint();

				for (Alien a : aliens) {
					a.setStop(true);
				}

				aliens.clear();
			}

			if (!ship.isAlive()) {
				for (Alien a : aliens) {
					a.setStop(true);
				}
				aliens.clear();

				powUp.setStop(true);
				isGameOver = true;
			}

		}

	}
/*
 * Draws ship, start, aliens, powerup, and also displays score through out game.
 * (non-Javadoc)
 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Draw (stars,score,ship,aliens, powUp)
		if (stars != null) {
			g2.setColor(Color.GRAY);
			Iterator<Ellipse2D.Double> itr = stars.iterator();
			while (itr.hasNext()) {
				Ellipse2D.Double star = itr.next();
				g2.draw(star);
			}

		}

		g2.drawString("SCORE: " + getHighScore(),
				(int) (0.85 * getEnvironmentWidth()), 25); // draw 'game over'
		if (ship != null)
			ship.draw(g2); // call draw method for the player ship.

		if (aliens != null)
			for (Alien a : aliens)
				a.draw(g2);

		if (powUp != null && !powUp.isHit())
			powUp.draw(g2);

		if (isGameOver) {
			if (playerWon) {
				g2.setColor(Color.GREEN);
				g2.drawString("YOU'RE THE BEST! YOU WIN!",
						(int) (getEnvironmentWidth() * 0.35),
						getEnvironmentHeight() / 2);
				g2.drawString("SCORE: " + getHighScore(),
						(int) (getEnvironmentWidth() * 0.45),
						30 + getEnvironmentHeight() / 2);
			} else {
				g2.setColor(Color.RED);
				g2.drawString("GAME OVER",
						(int) (getEnvironmentWidth() * 0.45),
						getEnvironmentHeight() / 2);
				g2.drawString("SCORE: " + getHighScore(),
						(int) (getEnvironmentWidth() * 0.45),
						30 + getEnvironmentHeight() / 2);
			}
			this.setCursorVisible();
		}
	}

/*
 * Method to set and display the highscore.
 */
	public void setHighScore(int score) {
		highScore += score;
		System.out.println("HighScore: " + highScore);
	}
/*
 * Get the Highscore
 */
	public int getHighScore() {
		return highScore;
	}

	/*
	 * Get the ship object belonging to the player
	 */
	public Ship getShip() {
		return ship;
	}

	/*
	 * Get the list of aliens
	 */
	public ArrayList<Alien> getAliens() {
		return aliens;
	}

	/*
	 * Check if game is over
	 */
	public Boolean isGameOver() {
		return isGameOver;
	}

}
