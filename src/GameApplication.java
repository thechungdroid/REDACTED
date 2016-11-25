import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameApplication extends Application {

	public static final int APP_WIDTH = 800;
	public static final int APP_HEIGHT = 500;

	private static Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;

		// The game will start with the IntroState
		primaryStage.setScene(IntroState.getScene());
		primaryStage.setWidth(APP_WIDTH);
		primaryStage.setHeight(APP_HEIGHT);

		// Set a delay for the title to coordinate with music. Limit action until title shows.
		// The game will progress to the GameState once the user hits ENTER.
		Timeline timeline = new Timeline();
		timeline.setDelay(Duration.millis(4700));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), new KeyValue(IntroState.getTitle().opacityProperty(), 1)));
		timeline.setOnFinished(e -> {
			IntroState.getScene().setOnKeyPressed(e2 -> {
				if(e2.getCode() == KeyCode.ENTER) {
					IntroState.stopMusic();
					GameState.resetBackground(GameState.currentLayer, false);
					GameState.resetPlayerPosition();
					GameState.startAnimationTimer();
					GameState.playMediaPlayer();
					primaryStage.setScene(GameState.getScene());
				}
			});
		});
		timeline.play();

		primaryStage.show();
	}

	public static void main(String args[]) {
		launch(args);
	}

	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}
}
