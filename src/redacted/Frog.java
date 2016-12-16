package redacted;
import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Frog extends Entity {

//	private static final String imagePath = "redacted/resources/frog_animation_x3.png";
	private static final String imagePath = "redacted/resources/frog_animation_redcape_x3.png";
//	private static final String battleImagePath = "redacted/resources/frog_animation_battle_x4.png";
	private static final String battleImagePath = "redacted/resources/frog_animation_battle_redcape_x4.png";

	private static Image battleImage;
	private static ImageView battleImageView;
	private static SpriteAnimation battleAnimation;

	// frog_animation_x3.png variables
	private static final int COUNT    =  6;
	private static final int COLUMNS  =   7;
    private static final int OFFSET_X =  0;
    private static final int OFFSET_Y =  0;
    private static final int WIDTH    = 72;
    private static final int HEIGHT   = 78;

    // frog_animation_battle_x4.png variables for battleAnimation
    private static final int BCOUNT = 4;
    private static final int BCOLS = 4;
    private static final int BOFF_X = 0;
    private static final int BOFF_Y = 0;
    private static final int BWIDTH = 156;
    private static final int BHEIGHT = 180;

    private Rectangle2D battleIdle = new Rectangle2D(BWIDTH*3, BOFF_Y, BWIDTH, BHEIGHT);

    private Rectangle2D frogIdleFront = new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
    private Rectangle2D frogIdleBack = new Rectangle2D(OFFSET_X, HEIGHT, WIDTH, HEIGHT);
    private Rectangle2D frogIdleLeft = new Rectangle2D(OFFSET_X, HEIGHT*2, WIDTH, HEIGHT);
    private Rectangle2D frogIdleRight = new Rectangle2D(OFFSET_X, HEIGHT*3, WIDTH, HEIGHT);

    private long invFrame = 0;

//	private int level;
//	private int health;
//	private int power;
//	private int exp;
//
//	private StatusBar powerBar;
//	private StatusBar expBar;

	private ArrayList<Ability> abilities;

	public Frog() {
		super();
		this.image = new Image(imagePath);
		this.imageView = new ImageView(image);
		this.animation = new SpriteAnimation(
				imageView,
				Duration.millis(1000),
				COUNT, COLUMNS,
				OFFSET_X, OFFSET_Y,
				WIDTH, HEIGHT);

		battleImage = new Image(battleImagePath);
		battleImageView = new ImageView(battleImage);
		battleAnimation = new SpriteAnimation(
				battleImageView,
				Duration.millis(1000),
				BCOUNT, BCOLS,
				BOFF_X, BOFF_Y,
				BWIDTH, BHEIGHT);

//		this.level = 1;
//		this.health = 100;
//		this.power = 100;
//		this.exp = 0;
		healthBar = new HealthBar();
//		powerBar = new PowerBar();
//		expBar = new ExpBar();

		// Default list of abilities.
		// TODO: Set customizable from intro menu?
		abilities = new ArrayList<Ability>();
		abilities.add(new BasicAttack());
		abilities.add(new AdvancedAttack());
		abilities.add(new FireAttack());
		abilities.add(new BasicHeal());
		abilities.add(new RunAbility());

		resetHitBox();
	}

	// This method will most likely need to be in Entity.
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void animateUp() {
		animation.animateUp(WIDTH, HEIGHT);
	}

	public void animateDown() {
    	animation.animateDown(WIDTH, OFFSET_Y);
	}

	public void animateLeft() {
		animation.animateLeft(WIDTH, HEIGHT*2);
	}

	public void animateRight() {
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

	// Battle Animations

	public void animateEnter() {
		battleAnimation.animate(BOFF_X, BOFF_Y);
	}

	public void animateIdle() {
		battleAnimation.animate(BOFF_X, BHEIGHT);
	}

	public void animateBasicAttack() {
		battleAnimation.animate(BOFF_X, 2*BHEIGHT);
	}

	public void animateAdvancedAttack() {
		battleAnimation.animate(BOFF_X, 3*BHEIGHT);
	}

	public void animateFireAttack() {
		battleAnimation.animate(BOFF_X, 4*BHEIGHT);
	}

	public void animateRun() {
		battleAnimation.animate(BOFF_X, 5*BHEIGHT);
	}

	public void animateHeal() {
		battleAnimation.animate(BOFF_X, 6*BHEIGHT);
	}

	public void animateDeath() {
		battleAnimation.animate(BOFF_X, 7*BHEIGHT);
	}

	public ImageView getBattleImageView() {
		return battleImageView;
	}

	public Rectangle2D getBattleIdle() {
		return battleIdle;
	}

	public SpriteAnimation getBattleAnimation() {
		return battleAnimation;
	}

	public int getBattleWidth() {
		return BWIDTH;
	}

	public int getBattleHeight() {
		return BHEIGHT;
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

				moved = true;
				if(now <= invFrame) {
					hitBox = new Rectangle2D(0,0,0,0);
				} else {
					updateHitBox();
					invFrame = 0;
				}
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

	public void setInvincibilityFrames(long currFrame) {
		invFrame = currFrame + 900000000;
	}

}
