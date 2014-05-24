import java.awt.Graphics2D;

import java.awt.Rectangle;
/* Entity class is responsible for resolving collision and
 * movement based on a set of properties defined by subclass.*/

abstract public class Entity extends Shape2D {
	private double xSpeed;
	private double ySpeed;
	protected final double DEFAULT_XSPEED = 1.0;
	protected final double DEFAULT_YSPEED = 1.0;
	private Game game;


	/*
	 * Construct an entity
	 */
	public Entity(Game game, double xPos, double yPos, double shapeWidth,
			double shapeHeight, double xSpeed, double ySpeed) {
		super(xPos, yPos, shapeWidth, shapeHeight);
		setSpeed(xSpeed, ySpeed);
		this.game = game;
	}

/*
 *  Move the shape( alien, player or bullet) around the rectangular environment
 *  Do not allow the shape to go outside of the environment boundaries
 */
	public void checkOvershoot(double x, double y) {
		int environmentWidth = game.getEnvironmentWidth();
		int environmentHeight = game.getEnvironmentHeight();
		/*
		 * Reflect the object if it goes too far
		 */
		int overShoot = (int) (x + getBoundingBoxWidth() - environmentWidth);
		if (x <= 0) {
			x = 0;
			xSpeed = -xSpeed;
		} else if (overShoot > 0) {
			x = environmentWidth - getBoundingBoxWidth();
			xSpeed = -xSpeed;
		}
		/*
		 * Reflect the object if it goes too far
		 */
		overShoot = (int) (y + getBoundingBoxHeight() / 2 - environmentHeight);
		if (y <= 0) {
			y = 0;
			ySpeed = -ySpeed;
		} else if (y >= environmentWidth) {
			y = environmentHeight - getBoundingBoxHeight();
			ySpeed = -ySpeed;
		}
		// move the shape to the new position
		updateBoundingBox(x, y);
	}

	/*
	 * Move an objects coordinates automatically
	 */
	abstract public void autoMove();

	/*
	 * Move a player controlled object
	 */
	abstract public void playerMove(double x, double y);

	/*
	 * Set speed of object
	 */
	public void setSpeed(double xspeed, double yspeed) {
		this.xSpeed = xspeed;
		this.ySpeed = yspeed;
	}

	/*
	 * Get y speed of object
	 */
	public double getySpeed() {
		return ySpeed;
	}

	/*
	 * Get x speed of object
	 */
	public double getxSpeed() {
		return xSpeed;
	}

	/*
	 * Get the 'Game' JComponent the object belongs to
	 */
	public Game getGame() {
		return game;
	}

	/*
	 * Draw method
	 * (non-Javadoc)
	 * @see Shape2D#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		super.draw(g2);
	}

/*
 * Detect if entitys collided with another. Returns TRUE is a collision occurs , else FALSE.
 */
	abstract public boolean collidesWith(Entity other);
}
