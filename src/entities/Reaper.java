package entities;
import animations.SpriteAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ui.HealthBar;

public class Reaper extends Entity {

	private static final String imagePath = "redacted/resources/reaper_animation_x3.png";
	//	private static final String imagePath = "redacted/resources/reaper_animation_x4.png";

	// reaper_animation_x3.png constants
	private static final int COUNT = 4;
	private static final int COLUMNS = 4;
	private static final int OFFSET_X = 0;
	private static final int OFFSET_Y = 0;
	private static final int WIDTH = 108;
	private static final int HEIGHT = 108;

	// reaper_animation_x4.png constants
//	private static final int COUNT = 4;
//	private static final int COLUMNS = 4;
//	private static final int OFFSET_X = 0;
//	private static final int OFFSET_Y = 0;
//	private static final int WIDTH = 144;
//	private static final int HEIGHT = 144;

	private Rectangle2D reaperIdleLeft = new Rectangle2D(OFFSET_X, HEIGHT, WIDTH, HEIGHT);
	private Rectangle2D reaperIdleRight = new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);


	public Reaper() {
		image = new Image(imagePath);
		imageView = new ImageView(image);;
		animation = new SpriteAnimation(
				imageView,
				Duration.millis(1000),
				COUNT, COLUMNS,
				OFFSET_X, OFFSET_Y,
				WIDTH, HEIGHT);

		healthBar = new HealthBar(100);
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public void animateUp() {
		getAnimation().animateUp(OFFSET_X, HEIGHT);
		getAnimation().isAnimateUp = true;
		getAnimation().play();
	}

	public void animateDown() {
		getAnimation().animateDown(OFFSET_X, OFFSET_Y);
		getAnimation().isAnimateDown = true;
		getAnimation().play();
	}

	public void animateLeft() {
		getAnimation().setOffsets(OFFSET_X, HEIGHT);
		getAnimation().isAnimateLeft = true;
		getAnimation().play();
	}

	public void animateRight() {
		getAnimation().setOffsets(OFFSET_X, OFFSET_Y);
		getAnimation().isAnimateRight = true;
		getAnimation().play();
	}

	public void stopAnimationLeft() {
		getAnimation().stop();
		getAnimation().isAnimateLeft = false;
		if(!getAnimation().isAnimateUp
				&& !getAnimation().isAnimateDown
				&& !getAnimation().isAnimateRight) {
			getImageView().setViewport(getIdleLeft());
		}
		checkAnimation();
	}

	public void stopAnimationRight() {
		getAnimation().stop();
		getAnimation().isAnimateRight = false;
		if(!getAnimation().isAnimateUp
				&& !getAnimation().isAnimateDown
				&& !getAnimation().isAnimateLeft) {
			getImageView().setViewport(getIdleRight());
		}
		checkAnimation();
	}

	// Edge case wherein user releases a directional key while moving in a different direction.
	// Insures animation continues in the correct direction of movement.
	public void checkAnimation() {
		if(getAnimation().isAnimateLeft) {
			animateLeft();
		}
		if(getAnimation().isAnimateRight){
			animateRight();
		}
	}

	public Rectangle2D getIdleLeft() {
		return reaperIdleLeft;
	}

	public Rectangle2D getIdleRight() {
		return reaperIdleRight;
	}

	public void updateHitBox() {
		// TODO: update the hitbox to better represent the sprite image.
		hitBox = new Rectangle2D(imageView.getBoundsInParent().getMinX()+8, imageView.getBoundsInParent().getMinY()+(HEIGHT/2), WIDTH-8, HEIGHT/2-8);
	}

	public Rectangle2D getHitBox() {
		return hitBox;
	}

}
