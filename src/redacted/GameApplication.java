package redacted;
import gamestates.GameState;
import gamestates.IntroState;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GameApplication extends Application {

	public static final int APP_WIDTH = 800;
	public static final int APP_HEIGHT = 500;

	private static EventHandler<KeyEvent> introControlHandler;

	/* Integer indicating mode:
	 * 0 = Easy
	 * 1 = Normal
	 * 2 = Hard
	 */
	private static int mode = 1;

	private static Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;

		// The game will start with the IntroState
		primaryStage.setScene(IntroState.getScene());
		primaryStage.setWidth(APP_WIDTH);
		primaryStage.setHeight(APP_HEIGHT);
		primaryStage.setResizable(false);

		startIntroController();

		primaryStage.show();
	}

	/*
	 * Set a delay for the title and select options to coordinate with music.
	 * Limit action until select options shows.
	 * The game will progress to the GameState is the user selects START.
	 * The game will show the instruction manual if the user selects HELP.
	 */
	private static void startIntroController() {
		setupIntroControlHandler();
		// For testing purposes, add the key presses before the options show on screen.
//		IntroState.getScene().addEventHandler(KeyEvent.KEY_PRESSED, introControlHandler);
		IntroState.getStartTimeline().setOnFinished(e -> {
			IntroState.getPointerTimeline().play();
			IntroState.getScene().addEventHandler(KeyEvent.KEY_PRESSED, introControlHandler);
		});
		IntroState.getTitleTimeline().play();
		IntroState.getStartTimeline().play();
	}

	private static void setupIntroControlHandler() {
		introControlHandler = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case ENTER:
					if(IntroState.getPointer().getLayoutY() == IntroState.position1) {
						IntroState.stopMusic();
						GameState.resetBackground(GameState.currentLayer, false);
						GameState.resetPlayerPosition();
						GameState.startAnimationTimer();
						GameState.playMediaPlayer();
						stage.setScene(GameState.getScene()); //this inits entities
					}
					else if(!IntroState.inModeSelect()){
						IntroState.changePane(IntroState.getPointer().getLayoutY());
					}
					break;
				case BACK_SPACE:
					if(IntroState.inMenu()) {
						IntroState.changePane(IntroState.getPointer().getLayoutY());
					}
					break;
				case UP:
					// Check if the application is at the help screen
					if(!IntroState.inMenu()) {
						// TODO: Make pointer position an enum.
						if(IntroState.getPointer().getLayoutY() == IntroState.position1) {
							IntroState.getPointer().setLayoutY(IntroState.position3);
						}
						else if(IntroState.getPointer().getLayoutY() == IntroState.position2) {
							IntroState.getPointer().setLayoutY(IntroState.position1);
						}
						else if(IntroState.getPointer().getLayoutY() == IntroState.position3) {
							IntroState.getPointer().setLayoutY(IntroState.position2);
						}
					}
					break;
				case DOWN:
					// Check if the application is at the help screen.
					if(!IntroState.inMenu()) {
						// TODO: Make pointer position an enum.
						if(IntroState.getPointer().getLayoutY() == IntroState.position1) {
							IntroState.getPointer().setLayoutY(IntroState.position2);
						}
						else if(IntroState.getPointer().getLayoutY() == IntroState.position2) {
							IntroState.getPointer().setLayoutY(IntroState.position3);
						}
						else if(IntroState.getPointer().getLayoutY() == IntroState.position3) {
							IntroState.getPointer().setLayoutY(IntroState.position1);
						}
					}
					break;
				default:
					break;
				}
			}
		};
	}

	public static EventHandler<KeyEvent> getIntroControlHandler() {
		return introControlHandler;
	}

	public static int getMode() {
		return mode;
	}

	public static void setMode(int mode) {
		GameApplication.mode = mode;
	}

	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}

	public static void main(String args[]) {
		launch(args);
	}
}
