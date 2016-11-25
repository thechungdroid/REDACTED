

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class StatusBar extends Group {

	private int maxValue;
	private int currValue;

	private Rectangle outline;
	private Rectangle background;
	private Rectangle status;

	private static final double X_OFFSET = 5;
	private static final double Y_OFFSET = 2.5;

	public StatusBar() {
		maxValue = 100;
		currValue = 100;

		outline = new Rectangle();
		outline.setWidth(200);
		outline.setHeight(10);
		outline.setArcWidth(10);
		outline.setArcHeight(10);

		background = new Rectangle();
		background.setLayoutX(outline.getX() + X_OFFSET);
		background.setLayoutY(outline.getY() + Y_OFFSET);
		background.setWidth(190);
		background.setHeight(5);
		background.setArcWidth(5);
		background.setArcHeight(5);
		background.setFill(Color.WHITE);

		status = new Rectangle();
		status.setLayoutX(outline.getX() + X_OFFSET);
		status.setLayoutY(outline.getY() + Y_OFFSET);
		status.setWidth(190);
		status.setHeight(5);
		status.setArcWidth(5);
		status.setArcHeight(5);
		status.setFill(Color.WHITE);

		this.getChildren().addAll(outline, background, status);
	}

	public StatusBar(int maxValue) {
		this.maxValue = maxValue;
		currValue = maxValue;
	}

	public double getWidth() {
		return outline.getWidth();
	}

	public double getHeight() {
		return outline.getHeight();
	}

	protected void setFillColor(Color color) {
		status.setFill(color);
	}

	public void decreaseValue(int value) {
		// add animateBar();
		if(currValue - value <= 0) {
			currValue = 0;
		} else {
			currValue -= value;
		}
	}

	public void increaseCurrentValue(int value) {
		// add animateBar();
		if(currValue + value > maxValue) {
			currValue = maxValue;
		} else {
			currValue += value;
		}
	}

	// Takes both positive and negative values.
	public void animateBar(int value) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);

		KeyValue keyValueX = new KeyValue(status.widthProperty(), status.getWidth() + value);
		KeyValue keyValueY = new KeyValue(status.scaleYProperty(), 1);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), keyValueX, keyValueY);
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();

	}
}
