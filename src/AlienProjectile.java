import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/* Alien Projectile class is responsible for any collision that takes place between
- aliens bullet and player*/

public class AlienProjectile extends Projectile implements Runnable {
	private Ellipse2D bullet;
	private int bulletSpeed = 20;
	private Thread thread;
	private boolean isStopped;

	public AlienProjectile(Game game, double xPos, double yPos,
			double shapeWidth, double shapeHeight, double xSpeed, double ySpeed) {
		super(game, xPos, yPos, shapeWidth, shapeHeight, xSpeed, ySpeed);
		thread = new Thread(this);
		isStopped = false;

		bullet = new Ellipse2D.Double(getXPos(), getYPos(),
				getBoundingBoxWidth(), getBoundingBoxHeight());
		thread.start();
	}
	
/*
 *  Method that checks for collisions with this class and other object. If there is a collision, end the game.
 * (non-Javadoc)
 * @see Projectile#collidesWith(Entity)
 */

	public boolean collidesWith(Entity other) {
		if (other instanceof Ship
				&& this.intersectsBoundingBox(other.getBoundingBox()))
			return true;
		else
			return false;
	}
/*
 * Method that moves the alien projectile and update its bounding box. Method also checks for boundaries.
 * (non-Javadoc)
 * @see Projectile#autoMove()
 */
	public void autoMove() {
		double x = getXPos();
		double y = getYPos() + getySpeed();

		bullet.setFrame(x, y, getBoundingBoxWidth(), getBoundingBoxHeight());
		updateBoundingBox(x, y);
	}
/*
 * Draws aliens laser charge.
 * (non-Javadoc)
 * @see Projectile#draw(java.awt.Graphics2D)
 */
	public void draw(Graphics2D g2) {
		if (!isStopped) {
			g2.draw(bullet);
			g2.setColor(Color.RED);
			g2.fill(bullet);
		}
	}

	/*
	 * Get the state of the AlienProjectile thread
	 */
	public boolean getStop() {
		return isStopped;
	}
	
	/*
	 * Set the AlienProjectile thread to stopped or not stopped
	 */
	public void setStop(Boolean x){
		isStopped = x;
	}
/*
 * Method is called when thread is created.
 * (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
	public void run() {

		synchronized (this) {
			while (!isStopped) {
				try {
					Game game = getGame();
					autoMove();

					if (this.collidesWith(game.getShip())) {
						game.getShip().setAlive(false);
						isStopped = true;
					}

					if (this.getYPos() > game.getEnvironmentHeight()) {
						isStopped = true;
					}
					Thread.sleep(bulletSpeed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
