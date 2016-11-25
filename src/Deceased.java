import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Deceased extends Entity {
	private static final String imagePath = "file:resources/deceased_animation_x3.png";
//	private static final String imagePath = "file:resources/deceased_animation_x4.png";

	// deceased_animation_x3.png constants
	private static final int COUNT    =  4;
	private static final int COLUMNS  =   4;
    private static final int OFFSET_X =  0;
    private static final int OFFSET_Y =  0;
    private static final int WIDTH    = 108;
    private static final int HEIGHT   = 97;

    private static Rectangle2D deceasedIdleFront = new Rectangle2D(OFFSET_X, HEIGHT*2, WIDTH, HEIGHT);
//    private static Rectangle2D deceasedIdleBack = new Rectangle2D(WIDTH, HEIGHT, WIDTH, HEIGHT);
    private static Rectangle2D deceasedIdleLeft = new Rectangle2D(WIDTH, OFFSET_Y, WIDTH, HEIGHT);
    private static Rectangle2D deceasedIdleRight = new Rectangle2D(OFFSET_X, HEIGHT*2, WIDTH, HEIGHT);


    public Deceased() {
    	this.image = new Image(imagePath);
		this.imageView = new ImageView(image);
		this.animation = new SpriteAnimation(
				imageView,
				Duration.millis(1000),
				COUNT, COLUMNS,
				OFFSET_X, OFFSET_Y,
				WIDTH, HEIGHT);

		hitBox = new Rectangle2D(imageView.getBoundsInParent().getMinX(), imageView.getBoundsInParent().getMinY()+(HEIGHT/2), WIDTH, HEIGHT/2);
		getImageView().setViewport(deceasedIdleFront);
    }

    public void updateHitBox() {
    	hitBox = new Rectangle2D(imageView.getBoundsInParent().getMinX(), imageView.getBoundsInParent().getMinY()+(HEIGHT/2), WIDTH, HEIGHT/2);
    }

    public void animateUp() {
    	animation.animateUp(OFFSET_X, HEIGHT);
    }

    public void animateDown() {
    	animation.animateDown(OFFSET_X, OFFSET_Y);
    }

    public void animateLeft() {
    	animation.animateLeft(OFFSET_X, OFFSET_Y);
    }

    public void animateRight() {
    	animation.animateRight(OFFSET_X, HEIGHT*2);
    }

    public void updatePosition(long now, double width, double height) {
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
		// Change direction when reaching a boundary
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
	}

    public Rectangle2D getIdleLeft() {
    	return deceasedIdleLeft;
    }

    public Rectangle2D getIdleRight() {
    	return deceasedIdleRight;
    }

}
