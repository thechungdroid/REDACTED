package redacted;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class IntroState {

	private static final String titleMusicFilePath = "resources/Travis Scott - Mamacita (Instrumental).mp3";

	private static Pane introPane;
	private static Scene introScene;
	private static Text titleText;
	private static Timeline titleTimeline;
	private static Text startText;
	private static Text optionsText;
	private static Text helpText;
	private static Timeline selectionsTimeline;
	private static Polygon selectPointer;
	public static double position1;
	public static double position2;
	public static double position3;
	private static Timeline pointerTimeline;
	private static MediaPlayer mediaPlayer;

	private static boolean inMenu = false;
	private static boolean inModeSelect = false;

	private static Pane optionsPane;
	private static final String optionsImageFilePath = "redacted/resources/options.png";
	private static Image optionsImage;
	private static ImageView optionsImageView;

	private static Text hardText;
	private static Text normalText;
	private static Text easyText;
	private static final double modeX = 330;
	private static final double modeY = 387.5;

	private static Polygon optionPointer;
	private static final double optionPointerXPos = 33;
	private static final double optionPointerPos1 = 34;
	private static final double optionPointerPos2 = 131;
	private static final double optionPointerPos3 = 260;

	private static EventHandler<KeyEvent> modeSelectEventHandler;

	private static Pane instructionPane;
	private static final String instructionImageFilePath = "redacted/resources/instructions.png";
	private static Image instructionImage;
	private static ImageView instructionImageView;

	static {
		setupIntroPane();

		// Instruction pane
		instructionPane = new Pane();
		instructionImage = new Image(instructionImageFilePath);
		instructionImageView = new ImageView(instructionImage);

		instructionPane.getChildren().addAll(instructionImageView);

		setupOptionPane();

		// Title music
//		Media media = new Media(new File(titleMusicFilePath).toURI().toString());
		Media media = new Media(GameApplication.class.getResource(titleMusicFilePath).toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();

		introScene = new Scene(introPane);
	}

	private static void setupIntroPane() {
		// Intro pane
		introPane = new Pane();
		introPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

		// Title
		titleText = new Text("R  E  D  A  C  T  E  D");
		titleText.setFont(new Font(24));
		titleText.setX(GameApplication.APP_WIDTH/2 - titleText.getBoundsInLocal().getWidth()/2);
		titleText.setY(GameApplication.APP_HEIGHT/2 - titleText.getBoundsInLocal().getHeight()/2);
		titleText.setOpacity(0);

		// Title timeline
		titleTimeline = new Timeline();
		titleTimeline.setDelay(Duration.millis(4400));
		titleTimeline.getKeyFrames().add(new KeyFrame(Duration.ONE, new KeyValue(IntroState.getTitle().opacityProperty(), 1)));

		// Selections
		startText = new Text("S T A R T");
		startText.setX(GameApplication.APP_WIDTH/2 - startText.getBoundsInLocal().getWidth()/2);
		startText.setY(GameApplication.APP_HEIGHT/2 + 80);
		startText.setOpacity(0);

		optionsText = new Text("O P T I O N S");
		optionsText.setX(GameApplication.APP_WIDTH/2 - optionsText.getBoundsInLocal().getWidth()/2);
		optionsText.setY(GameApplication.APP_HEIGHT/2 + 120);
		optionsText.setOpacity(0);

		helpText = new Text("H E L P");
		helpText.setX(GameApplication.APP_WIDTH/2 - helpText.getBoundsInLocal().getWidth()/2);
		helpText.setY(GameApplication.APP_HEIGHT/2 + 160);
		helpText.setOpacity(0);

		// Selections timeline
		selectionsTimeline = new Timeline();
		selectionsTimeline.setDelay(Duration.millis(8600));
		KeyValue startTextKeyValue = new KeyValue(startText.opacityProperty(), 1);
		KeyValue optionsTextKeyValue = new KeyValue(optionsText.opacityProperty(), 1);
		KeyValue helpTextKeyValue = new KeyValue(helpText.opacityProperty(), 1);
		KeyFrame textKeyFrame = new KeyFrame(Duration.millis(4200), startTextKeyValue, optionsTextKeyValue, helpTextKeyValue);

		selectionsTimeline.getKeyFrames().add(textKeyFrame);

		setupSelectPointer();

		introPane.getChildren().addAll(titleText, startText, optionsText, helpText, selectPointer);
	}

	public static void setupSelectPointer() {
		// Select pointer
		selectPointer = new Polygon();
		selectPointer.getPoints().addAll(new Double[] {
				0.0, 0.0,
				10.0, 5.0,
				0.0, 10.0,
		});

		position1 = startText.getY() - 9.6 - selectPointer.getLayoutBounds().getMinY();
		position2 = optionsText.getY() - 9.6 - selectPointer.getLayoutBounds().getMinY();
		position3 = helpText.getY() - 9.6 - selectPointer.getLayoutBounds().getMinY();

		selectPointer.setLayoutX(GameApplication.APP_WIDTH/2 - 65 - selectPointer.getLayoutBounds().getMinX());
		//				selectPointer.setLayoutX(GameApplication.APP_WIDTH/2 - 55 - selectPointer.getLayoutBounds().getMinX());
		selectPointer.setLayoutY(position1);
		selectPointer.setOpacity(0);

		// Pointer timeline
		pointerTimeline = new Timeline();
		KeyValue pointerKeyValue = new KeyValue(selectPointer.opacityProperty(), 1);
		KeyFrame pointerKeyFrame = new KeyFrame(Duration.ONE, pointerKeyValue);

		pointerTimeline.getKeyFrames().add(pointerKeyFrame);
	}

	public static void setupOptionPane() {
		// Options pane
		optionsPane = new Pane();
		optionsImage = new Image(optionsImageFilePath);
		optionsImageView = new ImageView(optionsImage);

		// Options pointer
		optionPointer = new Polygon();
		optionPointer.getPoints().addAll(new Double[] {
				0.0, 0.0,
				20.0, 10.0,
				0.0, 20.0,
		});
		optionPointer.setFill(Color.WHITE);

		optionPointer.setLayoutX(optionPointerXPos);
		optionPointer.setLayoutY(optionPointerPos1);

		hardText = new Text("HARD");
		hardText.setFont(new Font(34));
		hardText.setFill(Color.CRIMSON);
		hardText.setX(modeX);
		hardText.setY(modeY);
		hardText.setOpacity(0);

		normalText = new Text("NORMAL");
		normalText.setFont(new Font(34));
		normalText.setFill(Color.WHITE);
		normalText.setX(modeX);
		normalText.setY(modeY);
		normalText.setOpacity(1);

		easyText = new Text("EASY");
		easyText.setFont(new Font(34));
		easyText.setFill(Color.AQUAMARINE);
		easyText.setX(modeX);
		easyText.setY(modeY);
		easyText.setOpacity(0);

		optionsPane.getChildren().addAll(optionsImageView, optionPointer, hardText, normalText, easyText);

		setupModeEventHandler();
	}

	private static void setupModeEventHandler() {
		modeSelectEventHandler = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case ENTER:
					// Hard Mode
					if(optionPointer.getLayoutY() == optionPointerPos1) {
						hardText.setOpacity(1);
						normalText.setOpacity(0);
						easyText.setOpacity(0);
						GameApplication.setMode(2);
					}
					// Normal Mode
					if (optionPointer.getLayoutY() == optionPointerPos2) {
						hardText.setOpacity(0);
						normalText.setOpacity(1);
						easyText.setOpacity(0);
						GameApplication.setMode(1);
					}
					// Easy Mode
					if (optionPointer.getLayoutY() == optionPointerPos3) {
						hardText.setOpacity(0);
						normalText.setOpacity(0);
						easyText.setOpacity(1);
						GameApplication.setMode(0);
					}
					break;
				case BACK_SPACE:
					changePane(selectPointer.getLayoutY());
					break;
				case UP:
					if(optionPointer.getLayoutY() == optionPointerPos1) {
						optionPointer.setLayoutY(optionPointerPos3);
					} else if (optionPointer.getLayoutY() == optionPointerPos2) {
						optionPointer.setLayoutY(optionPointerPos1);
					} else if (optionPointer.getLayoutY() == optionPointerPos3) {
						optionPointer.setLayoutY(optionPointerPos2);
					}
					break;
				case DOWN:
					if(optionPointer.getLayoutY() == optionPointerPos1) {
						optionPointer.setLayoutY(optionPointerPos2);
					} else if (optionPointer.getLayoutY() == optionPointerPos2) {
						optionPointer.setLayoutY(optionPointerPos3);
					} else if (optionPointer.getLayoutY() == optionPointerPos3) {
						optionPointer.setLayoutY(optionPointerPos1);
					}
					break;
				default:
					break;
				}
			}
		};
	}

	public static EventHandler<KeyEvent> getModeSelectEventHandler() {
		return modeSelectEventHandler;
	}

	public static Polygon getPointer() {
		return selectPointer;
	}

	public static Timeline getTitleTimeline() {
		return titleTimeline;
	}

	public static Timeline getStartTimeline() {
		return selectionsTimeline;
	}

	public static Timeline getPointerTimeline() {
		return pointerTimeline;
	}

	public static Scene getScene() {
		return introScene;
	}

	public static boolean inMenu() {
		return inMenu;
	}

	public static boolean inModeSelect() {
		return inModeSelect;
	}

	public static Text getTitle() {
		return titleText;
	}

	public static void playMediaPlayer() {
		mediaPlayer.play();
	}

	public static void stopMusic() {
		mediaPlayer.stop();
	}

	public static void changePane(double pointerPos) {
		if(inMenu) {
			// Exit menu
			introScene.removeEventHandler(KeyEvent.KEY_PRESSED, modeSelectEventHandler);
			introScene.setRoot(introPane);
			introScene.addEventHandler(KeyEvent.KEY_PRESSED, GameApplication.getIntroControlHandler());
			inMenu = false;
			inModeSelect = false;
		} else if(pointerPos == position2) {
			// Options
			introScene.removeEventHandler(KeyEvent.KEY_PRESSED, GameApplication.getIntroControlHandler());
			introScene.setRoot(optionsPane);
			introScene.addEventHandler(KeyEvent.KEY_PRESSED, modeSelectEventHandler);
			inMenu = true;
			inModeSelect = true;
		} else if(pointerPos == position3) {
			// Instructions
			introScene.setRoot(instructionPane);
			inMenu = true;
		}
	}
}
