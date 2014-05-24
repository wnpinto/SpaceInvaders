import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.Timer;

public class Projectile extends Entity {
	private Rectangle bullet;

	public Projectile(Game game, double xPos, double yPos, double shapeWidth,
			double shapeHeight, double xSpeed, double ySpeed) {
		super(game, xPos, yPos, shapeWidth, shapeHeight, xSpeed, ySpeed);

		bullet = new Rectangle((int) getXPos(), (int) getYPos(),
				(int) getBoundingBoxWidth(), (int) getBoundingBoxHeight());
	}

	@Override
	public void autoMove() {
		// TODO Auto-generated method stub
		double x = getXPos();
		double y = getYPos() + getySpeed();

		bullet.setFrame(x, y, getBoundingBoxWidth(), getBoundingBoxHeight());
		updateBoundingBox(x, y);
	}
/*
 * Method that checks for collisions with this class and 
 * other object. If there is a collision, end the game.
 * (non-Javadoc)
 * @see Entity#collidesWith(Entity)
 */
	@Override
	public boolean collidesWith(Entity other) {
		if (other instanceof Alien
				&& this.intersectsBoundingBox(other.getBoundingBox())) {
			return true;
		} else
			return false;
	}
/*
 * Draws laser charge.
 * (non-Javadoc)
 * @see Entity#draw(java.awt.Graphics2D)
 */
	public void draw(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.draw(bullet);
	}

	@Override
	public void playerMove(double x, double y) {
	}

}
