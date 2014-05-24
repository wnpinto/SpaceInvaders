import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;


/*
 * ShipProjectile class is responsible for any collision that takes place between
 * ships(player) laser charge and alien or powerup
 */

public class ShipProjectile extends Projectile implements Runnable {

	private Rectangle2D.Double bullet;
	private Thread thread;
	private boolean isStopped;

	public ShipProjectile(Game game, double xPos, double yPos,
			double shapeWidth, double shapeHeight, double xSpeed, double ySpeed) {
		super(game, xPos, yPos, shapeWidth, shapeHeight, xSpeed, ySpeed);
		bullet = new Rectangle2D.Double((int) getXPos(), (int) getYPos(),
				(int) getBoundingBoxWidth(), (int) getBoundingBoxHeight());
		isStopped = false;

		thread = new Thread(this);
		thread.start();
	}
/*
 * Method that checks for collisions with this class and other object.
 * (non-Javadoc)
 * @see Projectile#collidesWith(Entity)
 */
	public boolean collidesWith(Entity other) {
		if (other instanceof Alien
				&& this.intersectsBoundingBox(other.getBoundingBox())) {
			System.out.println("Ship Projectile collided with an Alien");
			return true;
		} else
			return false;
	}
/*
 * Method that moves the bullet and update its bounding box. Method also checks for boundaries.
 * (non-Javadoc)
 * @see Projectile#autoMove()
 */
	public void autoMove() {
		double x = getXPos();
		double y = getYPos() - getySpeed();

		bullet.setFrame(x, y, getBoundingBoxWidth(), getBoundingBoxHeight());
		updateBoundingBox(x, y);
	}
/*
 * Method is called when thread is created.
 * (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
	@Override
	public void run() {
		synchronized (this) {
			while (!isStopped) {
				try {
					Game game = getGame();
					autoMove();

					// Check projectile is within bounds
					if (this.getBoundingBoxMaxY() < 0) {
						isStopped = true;
					}

					// Check projectile collision with aliens
					Iterator<Alien> alienItr = game.getAliens().iterator();
					while (alienItr.hasNext()) {
						Alien anAlien = alienItr.next();

						if (this.collidesWith(anAlien)) {
							anAlien.setStop(true);
							alienItr.remove();
							game.setHighScore(100);
							isStopped = true;
							break;
						}
					}

					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


/*
 * (non-Javadoc)
 * @see Projectile#draw(java.awt.Graphics2D)
 * Draws ships(players) laser charge.
 */
	public void draw(Graphics2D g2) {
		g2.draw(bullet);
		g2.setColor(Color.WHITE);
		g2.fill(bullet);
		// g2.draw(super.getBoundingBox());
	}

	/*
	 * Returns boolean true if projectile thread is stopped
	 */
	public Boolean isStopped() {
		return isStopped;
	}

}
