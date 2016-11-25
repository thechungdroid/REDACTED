import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

public abstract class Ability {

	private String imageFilePath = "";

	private Image image;
	private ImageView imageView;

	private int cooldown;
	private Arc cooldownOverlay;

	public Ability(int cooldown) {

		this.cooldown = cooldown;
		cooldownOverlay = new Arc();
		//Bind to image.
		cooldownOverlay.setStartAngle(0.0);
		cooldownOverlay.setType(ArcType.ROUND);
		// set the radius to a circle
//		cooldownOverlay.setRadiusX(value);
//		cooldownOverlay.setRadiusY(value);
		// need to bind center x and y to the image view.
		cooldownOverlay.centerXProperty().bind(imageView.xProperty());
		cooldownOverlay.centerYProperty().bind(imageView.yProperty());
		cooldownOverlay.setFill(Color.ALICEBLUE);
		cooldownOverlay.setOpacity(20);
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public void animateCooldown() {
		Timeline cooldownTimeline = new Timeline();
		cooldownTimeline.setCycleCount(1);

		KeyValue keyValueRadius = new KeyValue(cooldownOverlay.startAngleProperty(), 180);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(cooldown*1000), keyValueRadius);
		cooldownTimeline.getKeyFrames().add(keyFrame);
		cooldownTimeline.play();
	}

	public abstract void effect();
}
