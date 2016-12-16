package redacted;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private int offsetX;
    private int offsetY;
    private final int width;
    private final int height;

    private int lastIndex;

    // Must set animation setOnFinished property to stop the animation.
    private boolean isAnimating = false;

    protected boolean isAnimateUp = false;
    protected boolean isAnimateDown = false;
    protected boolean isAnimateLeft = false;
    protected boolean isAnimateRight = false;

    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count,   int columns,
            int offsetX, int offsetY,
            int width,   int height) {
        this.imageView = imageView;
        this.count     = count;
        this.columns   = columns;
        this.offsetX   = offsetX;
        this.offsetY   = offsetY;
        this.width     = width;
        this.height    = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width  + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }

    protected void setOffsets(int offsetX, int offsetY) {
    	this.offsetX = offsetX;
    	this.offsetY = offsetY;
    }

    public void animate(int offsetX, int offsetY) {
    	setOffsets(offsetX, offsetY);
    	isAnimating = true;
    	play();
    }

    public void animateUp(int offsetX, int offsetY) {
    	setOffsets(offsetX, offsetY);
    	isAnimateUp = true;
    	play();
    }

    public void animateDown(int offsetX, int offsetY) {
    	setOffsets(offsetX, offsetY);
    	isAnimateDown = true;
    	play();
    }

    public void animateLeft(int offsetX, int offsetY) {
    	setOffsets(offsetX, offsetY);
    	isAnimateLeft = true;
    	play();
    }

    public void animateRight(int offsetX, int offsetY) {
    	setOffsets(offsetX, offsetY);
    	isAnimateRight = true;
    	play();
    }

    public boolean isAnimating() {
    	return isAnimating;
    }

	public boolean isAnimateUp() {
		return isAnimateUp;
	}

	public boolean isAnimateDown() {
		return isAnimateDown;
	}

	public boolean isAnimateLeft() {
		return isAnimateLeft;
	}

	public boolean isAnimateRight() {
		return isAnimateRight;
	}

	public void stop() {
		super.stop();
		isAnimating = false;
	}

}