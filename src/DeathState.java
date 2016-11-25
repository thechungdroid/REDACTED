import java.io.File;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class DeathState {

	private static final String deathMusicFilePath = "resources/FFVI - Opening Theme.m4a";

	private static StackPane deathPane;
	private static Scene deathScene;
	private static Label deathLabel;

	private static MediaPlayer mediaPlayer;

	static {
		// Death music
		Media media = new Media(new File(deathMusicFilePath).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();

		deathPane = new StackPane();
		deathPane.setMinWidth(GameApplication.APP_WIDTH);
		deathPane.setMinHeight(GameApplication.APP_HEIGHT);
		deathPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		deathPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(48, 12, 48, 12))));
		deathLabel = new Label("Y O U D I E D !");

		deathPane.getChildren().add(deathLabel);

		deathScene = new Scene(deathPane);

		deathScene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				mediaPlayer.stop();
				IntroState.playMediaPlayer();
				GameApplication.setScene(IntroState.getScene());
			}
		});

	}

	public static Scene getScene() {
		return deathScene;
	}

	public static void playMediaPlayer() {
		mediaPlayer.play();
	}

}
