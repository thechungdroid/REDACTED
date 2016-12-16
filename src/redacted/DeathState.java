package redacted;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DeathState {

	private static final String deathMusicFilePath = "resources/FFVI - Opening Theme.m4a";

	private static Pane deathPane;
	private static Scene deathScene;
	private static Text deathText;
	private static Text restartText;
	private static Polygon selectPointer;

	private static MediaPlayer mediaPlayer;

	static {
		// Death music
//		Media media = new Media(new File(deathMusicFilePath).toURI().toString());
		Media media = new Media(GameApplication.class.getResource(deathMusicFilePath).toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();

		deathPane = new Pane();
		deathPane.setMinWidth(GameApplication.APP_WIDTH);
		deathPane.setMinHeight(GameApplication.APP_HEIGHT);
		deathPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		deathPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(48, 12, 48, 12))));

		deathText = new Text("Y O U D I E D !");
		deathText.setFont(new Font(24));
		deathText.setX(GameApplication.APP_WIDTH/2 - deathText.getBoundsInLocal().getWidth()/2);
		deathText.setY(GameApplication.APP_HEIGHT/2 - deathText.getBoundsInLocal().getHeight()/2);

		restartText = new Text("R E S T A R T");
		restartText.setX(GameApplication.APP_WIDTH/2 - restartText.getBoundsInLocal().getWidth()/2);
		restartText.setY(GameApplication.APP_HEIGHT/2 + 60);

		selectPointer = new Polygon();
		selectPointer.getPoints().addAll(new Double[] {
				0.0, 0.0,
				10.0, 5.0,
				0.0, 10.0,
		});
		selectPointer.setLayoutX(GameApplication.APP_WIDTH/2 - 65 - selectPointer.getLayoutBounds().getMinX());
		selectPointer.setLayoutY(restartText.getY() - 9.6 - selectPointer.getLayoutBounds().getMinY());

		deathPane.getChildren().addAll(deathText, restartText, selectPointer);

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
