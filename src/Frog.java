import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Frog extends Entity {

//	private static final String imagePath = "file:resources/frog_animation_x3.png";
//	private static final String imagePath = "file:resources/frog_animation_x4.png";
	private static final String imagePath = "file:resources/frog_animation_redcape_x3.png";

	// frog_animation_x4.png variables
	private static final int COUNT    =  6;
	private static final int COLUMNS  =   7;
    private static final int OFFSET_X =  0;
    private static final int OFFSET_Y =  0;
    private static final int WIDTH    = 72;
    private static final int HEIGHT   = 78;

	// frog_animation_x4.png variables
//	private static final int COUNT    =  6;
//	private static final int COLUMNS  =   7;
//    private static final int OFFSET_X =  0;
//    private static final int OFFSET_Y =  0;
//    private static final int WIDTH    = 96;
//    private static final int HEIGHT   = 104;

    private Rectangle2D frogIdleFront = new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
    private Rectangle2D frogIdleBack = new Rectangle2D(OFFSET_X, HEIGHT, WIDTH, HEIGHT);
    private Rectangle2D frogIdleLeft = new Rectangle2D(OFFSET_X, HEIGHT*2, WIDTH, HEIGHT);
    private Rectangle2D frogIdleRight = new Rectangle2D(OFFSET_X, HEIGHT*3, WIDTH, HEIGHT);

	private int level;
	private int health;
	private int power;
	private int exp;
	private StatusBar healthBar;
	private StatusBar powerBar;
	private StatusBar expBar;

	public Frog() {
		this.image = new Image(imagePath);
		this.imageView = new ImageView(image);
		this.animation = new SpriteAnimation(
				imageView,
				Duration.millis(1000),
				COUNT, COLUMNS,
				OFFSET_X, OFFSET_Y,
				WIDTH, HEIGHT);
		this.level = 1;
		this.health = 100;
		this.power = 100;
		this.exp = 0;
		healthBar = new HealthBar();
		powerBar = new PowerBar();
		expBar = new ExpBar();

//		hitBox = new Rectangle2D(imageView.getX(), imageView.getY(), WIDTH, HEIGHT);
		resetHitBox();
	}

	public void animateUp() {
//		getAnimation().setOffsets(WIDTH, HEIGHT);
//    	getAnimation().isAnimateUp = true;
//    	getAnimation().play();
		animation.animateUp(WIDTH, HEIGHT);
	}

	public void animateDown() {
//		getAnimation().setOffsets(WIDTH, 0);
//    	getAnimation().isAnimateDown = true;
//    	getAnimation().play();
    	animation.animateDown(WIDTH, OFFSET_Y);
	}

	public void animateLeft() {
//		getAnimation().setOffsets(WIDTH, HEIGHT*2);
//		getAnimation().isAnimateLeft = true;
//		getAnimation().play();
		animation.animateLeft(WIDTH, HEIGHT*2);
	}

	public void animateRight() {
//		getAnimation().setOffsets(WIDTH, HEIGHT*3);
//    	getAnimation().isAnimateRight = true;
//    	getAnimation().play();
    	animation.animateRight(WIDTH, HEIGHT*3);
	}

	public void stopAnimationUp() {
		getAnimation().stop();
		getAnimation().isAnimateUp = false;
		if(!getAnimation().isAnimateDown
				&& !getAnimation().isAnimateLeft
				&& !getAnimation().isAnimateRight) {
			getImageView().setViewport(getIdleBack());
		}
		checkAnimation();
	}

	public void stopAnimationDown() {
		getAnimation().stop();
		getAnimation().isAnimateDown = false;
		if(!getAnimation().isAnimateUp
				&& !getAnimation().isAnimateLeft
				&& !getAnimation().isAnimateRight) {
			getImageView().setViewport(getIdleFront());
		}
		checkAnimation();
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

	public Rectangle2D getIdleFront() {
		return frogIdleFront;
	}

	public Rectangle2D getIdleBack() {
		return frogIdleBack;
	}

	public Rectangle2D getIdleLeft() {
		return frogIdleLeft;
	}

	public Rectangle2D getIdleRight() {
		return frogIdleRight;
	}

	// Edge case wherein user releases a directional key while moving in a different direction.
	// Insures animation continues in the correct direction of movement.
	public void checkAnimation() {
		if(getAnimation().isAnimateUp) {
			animateUp();
		}
		if(getAnimation().isAnimateDown){
			animateDown();
		}
		if(getAnimation().isAnimateLeft) {
			animateLeft();
		}
		if(getAnimation().isAnimateRight){
			animateRight();
		}
	}

	// Overloaded entity method for player character.
	public void updatePosition(long now, BackgroundLayer layer, double dy, double dx) {
		// Handles bounds and collision for the player
		double top = getImageView().getBoundsInParent().getMinY()+HEIGHT/2;
		double bottom = getImageView().getBoundsInParent().getMaxY();
		double left = getImageView().getBoundsInParent().getMinX() + 4;
		double right = getImageView().getBoundsInParent().getMaxX() - 4;

		// Boolean used to ensure that relocate does not get called twice in a single frame.
		boolean moved = false;

		for(Rectangle2D b : layer.getBounds()) {
			double topBound = b.getMinY();
			double bottomBound = b.getMaxY();
			double leftBound = b.getMinX();
			double rightBound = b.getMaxX();

			// The player will relocate as long as the player model is within the bounds of the current layer.
			if(top+dy > topBound && bottom+dy < bottomBound
					&& left+dx > leftBound && right+dx < rightBound && !moved) {
				getImageView().relocate(getImageView().getBoundsInParent().getMinX()+dx, getImageView().getBoundsInParent().getMinY()+dy);
				updateHitBox();
				moved = true;
			}

			// Handles navigation through BackgroundLayers
			if(left+dx <= leftBound && layer.isExit(leftBound)) {
				GameState.resetBackground(GameState.darkStation1, true);
			}
			if(right+dx >= rightBound && layer.isExit(rightBound)) {
				GameState.resetBackground(GameState.darkStation2, false);
			}
		}
	}

	public void resetHitBox() {
		hitBox = new Rectangle2D(imageView.getX(), imageView.getY(), WIDTH, HEIGHT);
	}

	public void updateHitBox() {
		hitBox  = new Rectangle2D(getImageView().getBoundsInParent().getMinX(), getImageView().getBoundsInParent().getMinY(), WIDTH, HEIGHT);
	}

}
