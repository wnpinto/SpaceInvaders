import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/*PoweUp class is responsible for creating and declareing a movement for an entity known as the powerup.  
 * This class contains solutions for what happens when powerup gets in contact with the player(ship). */

public class PowerUp extends Entity implements Runnable {

	private Ellipse2D.Double powerBall;
	private boolean isStopped;
	private Thread thread;

	public PowerUp(Game game, double xPos, double yPos, double shapeWidth,
			double shapeHeight, double xSpeed, double ySpeed) {
		super(game, xPos, yPos, shapeWidth, shapeHeight, xSpeed, ySpeed);
		powerBall = new Ellipse2D.Double((int) getXPos(), (int) getYPos(),
				(int) getBoundingBoxWidth(), (int) getBoundingBoxHeight());
		isStopped = false;

		thread = new Thread(this);
		thread.start();
	}

/*
 * Method that checks for collisions with powerup and ship(player). If there is a
 * collision, which condition is satisfied.
 * (non-Javadoc)
 * @see Entity#collidesWith(Entity)
 */
	public boolean collidesWith(Entity other) {
		if (other instanceof ShipProjectile
				&& this.intersectsBoundingBox(other.getBoundingBox())) {
			System.out.println("Power up hit Ship Projectile");
			return true;
		} else
			return false;
	}
	
	/*
	 * Method that moves the powerup entity and update 
	 * its bounding box. Method also checks for boundaries.
	 * (non-Javadoc)
	 * @see Entity#autoMove()
	 */
	public void autoMove() {
		double x = getXPos();
		double y = getYPos();
		super.checkOvershoot(x, y);
		x += getxSpeed();
		y += getySpeed();

		powerBall.setFrame(x, y, getBoundingBoxWidth(), getBoundingBoxHeight());
		updateBoundingBox(x, y);
	}
/*
 * Draws the powerup images. Lables each one with one of the 4 powers.
 * (non-Javadoc)
 * @see Entity#draw(java.awt.Graphics2D)
 */
	public void draw(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		g2.fill(powerBall);
		g2.draw(powerBall);
	}

	/*
	 * Set the powerUp thread to stopped or not stopped
	 */
	public void setStop(Boolean x) {
		isStopped = x;
	}

	/*
	 * If projectile is/was hit return true
	 */
	public boolean isHit() {
		return isStopped; // Currently stopped and isHit are one and the same
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

					for (ShipProjectile pr : game.getShip()
							.getShipProjectiles()) {
						if (this.collidesWith(pr)) {
							game.getShip().setProjSpeed(15);
							isStopped = true;
						}
					}

					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void playerMove(double x, double y) {
	}

}
