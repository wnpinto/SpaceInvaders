import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/*
 * An entity which represents an aliens. This class is responsible 
 * for creating the alien, setting its movement, and enabling its powers.
 */
public class Alien extends Entity implements Runnable {

	private Thread thread;
	private AlienProjectile apj = null;
	private boolean shotFired;
	private int alienSpeed = 20; // ??
	private boolean isStopped;
	private Ellipse2D.Double alien;
	private boolean isAlive;

	public Alien(Game game, double xPos, double yPos, double shapeWidth,
			double shapeHeight, double xSpeed, double ySpeed) {
		super(game, xPos, yPos, shapeWidth, shapeHeight, 1, ySpeed);
		isStopped = false;
		isAlive = true;

		alien = new Ellipse2D.Double((int) xPos, (int) yPos,
				(int) getBoundingBoxWidth(), (int) getBoundingBoxHeight());

		thread = new Thread(this);
		thread.start();
	}
/*
 * Method that moves the alien and update its bounding box. Method also checks for boundaries.
 * (non-Javadoc)
 * @see Entity#autoMove()
 */
	@Override
	public void autoMove() {
		super.checkOvershoot(getXPos(), getYPos());
													
		double x = getXPos() + getxSpeed();
		double y = getYPos();

		alien.setFrame(x, y, getBoundingBoxWidth(), getBoundingBoxHeight());
		updateBoundingBox(x, getYPos());
	}

	@Override
	public void playerMove(double x, double y) {
	}

	/*
	 * Check if Alien collides with a ship Projectile
	 * (non-Javadoc)
	 * @see Entity#collidesWith(Entity)
	 */
	@Override
	public boolean collidesWith(Entity other) {
		if (other instanceof ShipProjectile
				&& this.intersectsBoundingBox(other.getBoundingBox())) {
			System.out.println("Alien collided with a ShipProjectile");
			isAlive = false;
			return true;
		} else
			return false;
	}
/*
 * Method that shoot alien projectiles. The method also checks to see if the alien already fired a shot
 */
	public void shoot() {
		double projHeight = 10;
		double projWidth = 10;
		double ySpeed = 5;

		if (shotFired == false) {
			apj = new AlienProjectile(getGame(), getXPos()
					+ (getBoundingBoxWidth() / 2), getYPos() + projHeight,
					projWidth, projHeight, 0, ySpeed);
		}

		shotFired = true;
	}
/*
 * Draws the alien character.
 * (non-Javadoc)
 * @see Entity#draw(java.awt.Graphics2D)
 */
	public void draw(final Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.draw(alien);
		g2.fill(alien);

		if (apj != null && isAlive && shotFired == true)
			apj.draw(g2);

		if (apj != null && apj.getYPos() > getGame().getEnvironmentHeight()) {
			shotFired = false;
		}
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

					int random = (int) (Math.random() * 30) + 1;

					if (random == 5) {
						shoot();
					}

					if (apj != null
							&& apj.getYPos() + apj.getBoundingBoxHeight() > game
									.getEnvironmentHeight()) {
						shotFired = false;
						apj.setStop(true);
						apj = null;
					}
					Thread.sleep(alienSpeed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

	/*
	 * Stop the thread running the alien
	 */
	public void setStop(Boolean x) {
		isStopped = x;
	}

}
