import java.io.File;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class IntroState {

	private static final String titleMusicFilePath = "resources/Travis Scott - Mamacita (Instrumental).mp3";

	private static StackPane introPane;
	private static Scene introScene;
	private static Label titleLabel;
	private static MediaPlayer mediaPlayer;

	static {
		introPane = new StackPane();
		introPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		titleLabel = new Label("R E D A C T E D");
		titleLabel.setOpacity(0);
//		Label startLabel = new Label("S T A R T");
//		startLabel.setOpacity(0);

		introPane.getChildren().addAll(titleLabel);

		// Title music
		Media media = new Media(new File(titleMusicFilePath).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();

		introScene = new Scene(introPane);
	}

	public static Scene getScene() {
		return introScene;
	}

	public static Label getTitle() {
		return titleLabel;
	}

	public static void playMediaPlayer() {
		mediaPlayer.play();
	}

	public static void stopMusic() {
		mediaPlayer.stop();
	}
}
