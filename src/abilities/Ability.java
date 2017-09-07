package abilities;
import entities.Entity;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

public abstract class Ability extends Group {

	protected String imageFilePath;

	private Image image;
	private ImageView imageView;

	private static final int globalCooldown = 2;
	protected int cooldown;
	private Arc cooldownOverlay;
	private Arc globalCooldownOverlay;

	private Timeline globalCooldownTimeline;
	private Timeline cooldownTimeline;

	private boolean isAnimating = false;

	public Ability(String imageFilePath) {

		this.imageFilePath = imageFilePath;

		image = new Image(imageFilePath);
		imageView = new ImageView(image);

		cooldownOverlay = new Arc();
		//Bind to image.
		cooldownOverlay.setLength(0);
		cooldownOverlay.setStartAngle(90);
		cooldownOverlay.setRadiusX(25);
		cooldownOverlay.setRadiusY(25);
		cooldownOverlay.setFill(Color.BLACK);
		cooldownOverlay.setType(ArcType.ROUND);
		cooldownOverlay.setOpacity(.7);
		// need to bind center x and y to the image view.
		cooldownOverlay.centerXProperty().bind(imageView.xProperty().add(image.getWidth()/2));
		cooldownOverlay.centerYProperty().bind(imageView.yProperty().add(image.getHeight()/2));

		setupCooldown();

		globalCooldownOverlay = new Arc();
		//Bind to image.
		globalCooldownOverlay.setLength(0);
		globalCooldownOverlay.setStartAngle(90);
		globalCooldownOverlay.setRadiusX(25);
		globalCooldownOverlay.setRadiusY(25);
		globalCooldownOverlay.setFill(Color.DARKGREY);
		globalCooldownOverlay.setType(ArcType.ROUND);
		globalCooldownOverlay.setOpacity(.7);
		// need to bind center x and y to the image view.
		globalCooldownOverlay.centerXProperty().bind(imageView.xProperty().add(image.getWidth()/2));
		globalCooldownOverlay.centerYProperty().bind(imageView.yProperty().add(image.getHeight()/2));

		setupGlobalCooldown();

		this.getChildren().addAll(imageView, cooldownOverlay, globalCooldownOverlay);
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setupGlobalCooldown() {
		globalCooldownTimeline = new Timeline();
		globalCooldownTimeline.setCycleCount(1);

		KeyValue keyValueRadius = new KeyValue(globalCooldownOverlay.lengthProperty(), 360);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(globalCooldown*1000), keyValueRadius);
		globalCooldownTimeline.getKeyFrames().add(keyFrame);
		globalCooldownTimeline.setOnFinished(e -> {
			globalCooldownOverlay.setLength(0);
			isAnimating = false;
		});
	}

	// Has to reinstantiate Timeline because
	public void animateGlobalCooldown() {
		isAnimating = true;
		cooldownTimeline = new Timeline();
		cooldownTimeline.setCycleCount(1);

		KeyValue keyValueRadius = new KeyValue(cooldownOverlay.lengthProperty(), 360);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(cooldown*1000), keyValueRadius);
		cooldownTimeline.getKeyFrames().add(keyFrame);
		cooldownTimeline.setOnFinished(e -> {
			cooldownOverlay.setLength(0);
			isAnimating = false;
		});
		globalCooldownTimeline.play();
	}

	public void setupCooldown() {
		cooldownTimeline = new Timeline();
		cooldownTimeline.setCycleCount(1);

		KeyValue keyValueRadius = new KeyValue(cooldownOverlay.lengthProperty(), 360);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(cooldown*1000), keyValueRadius);
		cooldownTimeline.getKeyFrames().add(keyFrame);
		cooldownTimeline.setOnFinished(e -> {
			cooldownOverlay.setLength(0);
			isAnimating = false;
		});
	}

	public void animateCooldown() {
		isAnimating = true;
		cooldownTimeline = new Timeline();
		cooldownTimeline.setCycleCount(1);

		KeyValue keyValueRadius = new KeyValue(cooldownOverlay.lengthProperty(), 360);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(cooldown*1000), keyValueRadius);
		cooldownTimeline.getKeyFrames().add(keyFrame);
		cooldownTimeline.setOnFinished(e -> {
			cooldownOverlay.setLength(0);
			isAnimating = false;
		});
		cooldownTimeline.play();
	}

	public void stopCooldown() {
		cooldownTimeline.jumpTo(Duration.ZERO);
		cooldownTimeline.stop();
	}

	public boolean isAnimating() {
		return isAnimating;
	}

	public abstract void effect(Entity other);

}
