import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

/*
 * An entity which represents the player (ship). This class is responsible 
 * for creating the ship, setting its movement, and enabling its powers
 */

public class Ship extends Entity {

	private Rectangle2D.Double ship;
	private double prevMouseX;
	public boolean shotFired;
	private ArrayList<ShipProjectile> myProjectiles;
	private int projSpeed;
	private boolean isAlive;

	public Ship(Game game, double xPos, double yPos, double shapeWidth,
			double shapeHeight, double xSpeed, double ySpeed) {

		super(game, xPos, yPos, shapeWidth, shapeHeight, xSpeed, ySpeed);

		isAlive = true;
		this.ship = new Rectangle2D.Double(xPos, yPos, shapeWidth, shapeHeight);
		myProjectiles = new ArrayList<ShipProjectile>();
		projSpeed = 8; // Default projectile speed
	}
/*
 * Method that shoot ship projectile. The method also checks 
 * to see if the ship already fired a shot b/c ship can only shot one laser at a time.
 */
	public void shoot() {
		double projHeight = 10; // Dummy
		double projWidth = 10; // Dummy
		// projSpeed = 5;
		if (shotFired == false) {
			ShipProjectile p = new ShipProjectile(getGame(), getXPos()
					+ (int) (getBoundingBoxWidth() / 2),
					getYPos() - projHeight, projWidth, projHeight, 0, projSpeed);
			myProjectiles.add(p);

			shotFired = true;
		}
	}

	public void autoMove() {
	}
/*
 * Movement of the player (ship).
 * (non-Javadoc)
 * @see Entity#playerMove(double, double)
 */
	public void playerMove(double x, double y) {
		// TODO Auto-generated method stub
		double shipX = getXPos();
		double shipY = getYPos();
		double shipWidth = getBoundingBoxWidth();
		double shipHeight = getBoundingBoxHeight();

		double floatsensitivity = 0.5f;
		int displacement = (int) x - (int) prevMouseX;
		displacement = (int) (floatsensitivity * displacement);
		shipX = prevMouseX + displacement;

		if (this.getBoundingBoxMaxX() > getGame().getEnvironmentWidth())// Check
																		// right
																		// bounds
			shipX = getGame().getEnvironmentWidth() - getBoundingBoxWidth();

		if (this.getBoundingBoxMinX() < 0)
			shipX = 0;

		ship.setFrame(shipX, shipY, shipWidth, shipHeight);
		updateBoundingBox(shipX, shipY);
	}

	public void setPrevMouseX(double x) {
		prevMouseX = x;
	}
/*
 *  Draws the ship or players character.
 * (non-Javadoc)
 * @see Entity#draw(java.awt.Graphics2D)
 */
	@SuppressWarnings("deprecation")
	public void draw(final Graphics2D g2) {
		g2.setColor(Color.GREEN);
		g2.draw(ship);
		g2.fill(ship);

		if (shotFired == true) {
			Iterator<ShipProjectile> shipItr = myProjectiles.iterator();
			while (shipItr.hasNext()) {
				ShipProjectile aProj = shipItr.next();
				if (aProj.isStopped()) {
					// System.out.println("Ship Projectile Collided");
					shotFired = false;
					shipItr.remove();
				} else
					aProj.draw(g2);
			}
		}
	}

	/*
	 * Check if ship collides with AlienProjectile
	 * (non-Javadoc)
	 * @see Entity#collidesWith(Entity)
	 */
	@Override
	public boolean collidesWith(Entity other) {
		if (other instanceof AlienProjectile
				&& this.intersectsBoundingBox(other.getBoundingBox()))
			return true;
		else
			return false;
	}

	/*
	 * Set speed of the ships projectile
	 */
	public void setProjSpeed(int x) {
		projSpeed = x;
	}

	/*
	 * Get the speed of the ships projectile
	 */
	public int getProjSpeed() {
		return projSpeed;
	}

	/*
	 * Get list of shipsProjectile
	 */
	public ArrayList<ShipProjectile> getShipProjectiles() {
		return myProjectiles;
	}

	/*
	 * Set if the ship is alive
	 */
	public void setAlive(boolean x) {
		isAlive = x;
	}

	/*
	 * Get if the ship is alive
	 */
	public boolean isAlive() {
		return isAlive;
	}
}
