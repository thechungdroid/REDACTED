package entities;
import java.util.HashSet;

import animations.SpriteAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.HealthBar;

public abstract class Entity {

//	private final String imageFilePath = "";

	protected Image image;
	protected ImageView imageView;
	protected SpriteAnimation animation;

	/* If hitBox is not instantiated, there may be NullPointerExceptions when handling collisions.
	 * This occurs if an enemy is spawned outside the bounds of the frame.
	 * (possibly also when it is intersecting with another object?)
	 */
	protected Rectangle2D hitBox = new Rectangle2D(0,0,0,0);

//	private static int COUNT;
//	private static int COLUMNS;
//    private static int OFFSET_X;
//    private static int OFFSET_Y;
    private static int WIDTH;
    private static int HEIGHT;

    protected double dx;
    protected double dy;

    protected HealthBar healthBar;

	public ImageView getImageView() {
		return imageView;
	}

	public void updateHitBox() {
		hitBox  = new Rectangle2D(getImageView().getBoundsInParent().getMinX(), getImageView().getBoundsInParent().getMinY(), WIDTH, HEIGHT);
	}

	public SpriteAnimation getAnimation() {
		return animation;
	}

	public abstract void animateUp();

	public abstract void animateDown();

	public abstract void animateLeft();

	public abstract void animateRight();

	public Rectangle2D getHitBox() {
		return hitBox;
	}

	public void setMovement(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setMovementUp() {
		dx = 0;
		dy = -1;
	}

	public void setMovementDown() {
		dx = 0;
		dy = 1;
	}

	public void setMovementLeft() {
		dx = -1;
		dy = 0;
	}

	public void setMovementRight() {
		dx = 1;
		dy = 0;
	}

	// Method for enemies.
	public void updatePosition(long now, double width, double height, HashSet<Entity> enemies) {
		double top = getImageView().getBoundsInParent().getMinY();
		double bottom = getImageView().getBoundsInParent().getMaxY();
		double left = getImageView().getBoundsInParent().getMinX() + 4;
		double right = getImageView().getBoundsInParent().getMaxX() - 4;

		if(left+dx > 0 && right+dx < width
				&& top+dy > 0 && bottom+dy < height) {
			animate();
			getImageView().relocate(getImageView().getBoundsInParent().getMinX()+dx, getImageView().getBoundsInParent().getMinY()+dy);
			updateHitBox();
		}
		// Change direction when reaching a boundary or colliding with another object
		if(top+dy <= 0) {
			dy = 1;
		}
		if(bottom+dy >= height) {
			dy = -1;
		}
		if(left+dx <= 0) {
			dx = 1;
		}
		if(right+dx >= width) {
			dx = -1;
		}
		// Changes direction of movement if the instance collides with another enemy.
		for(Entity e : enemies) {
			if(this != e) {
				if(getHitBox().intersects(e.getHitBox())) {
					if(dy == 1) {
						dy = -1;
					} else if(dy == -1) {
						dy = 1;
					} else if(dx == 1) {
						dx = -1;
					} else if(dx == -1) {
						dx = 1;
					}
				}
			}
		}
	}

	public void animate() {
		if(dy < 0) {
			animateRight();
		}
		if(dy > 0) {
			animateLeft();
		}
		if(dx < 0) {
			animateLeft();
		}
		if(dx > 0) {
			animateRight();
		}
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public abstract Rectangle2D getIdleLeft();

	public abstract Rectangle2D getIdleRight();

	public HealthBar getHealthBar() {
		return healthBar;
	}

	public void subtractHealth(int hitPoints) {
		healthBar.decreaseValue(hitPoints);
	}

	public void restoreHealth(int healthPoints) {
		healthBar.increaseCurrentValue(healthPoints);
	}
}
