import java.io.File;
import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

// The main game state wherein the player traverses the world and engages enemies.
// This state will either transition into the BattleState.
public class GameState {

	private static final String gameMusicFilePath = "resources/Tyler, The Creator - Rusty (Instrumental).mp3";
	private static final String darkStation1FilePath = "resources/dark_station.png";
	private static final String darkStation2FilePath = "resources/dark_station2.png";
	private static final String darkStation2ForegroundFilePath = "resources/dark_station2_foreground.png";

	private static Pane gamePane;
	private static Scene gameScene;
	private static MediaPlayer mediaPlayer;
	private static AnimationTimer timer;

	public static BackgroundLayer darkStation1;
	public static BackgroundLayer darkStation2;
	public static BackgroundLayer currentLayer;

//	private static ImageView backgroundImageView;
//	private static Rectangle2D backgroundBounds;
	private static int spawnX;
	private static int spawnY;

	private static Frog player;
	private static Reaper enemy1;
	private static Reaper enemy2;
	private static Deceased enemy3;
	private static Deceased enemy4;
	private static Deceased enemy5;

	static {
		gamePane = new Pane();
		gamePane.setMinWidth(GameApplication.APP_WIDTH);
		gamePane.setMinHeight(GameApplication.APP_HEIGHT);
		gamePane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		gamePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		gameScene = new Scene(gamePane);

		// Game music
		Media media = new Media(new File(gameMusicFilePath).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();

		initBackgrounds();
		initEntities();

		setupKeyPresses();
		setupKeyReleases();
	}

	private static void initBackgrounds() {
		darkStation1 = new BackgroundLayer(darkStation1FilePath, null, 115, 305);
		darkStation1.addBound(new Rectangle2D(117, 330, 682, 70));
		darkStation1.setReturnLocation(730, 305);
		darkStation1.setExit(799);

		darkStation2 = new BackgroundLayer(darkStation2FilePath, null, 15, 305);
		darkStation2.addBound(new Rectangle2D(0, 330, 760, 70));
		darkStation2.addBound(new Rectangle2D(530, 350, 80, 130));
		darkStation2.setExit(0);
		// Foreground imaging not working.
		ImageView ds2Foreground = new ImageView(new Image("file:"+darkStation2ForegroundFilePath));
		darkStation2.getPane().getChildren().add(ds2Foreground);
		ds2Foreground.toFront();

		// Initialize the beginning scene
		resetBackground(darkStation1, false);
	}

	private static void initEntities() {

		player = new Frog();

		enemy1 = new Reaper();
		enemy1.getImageView().setX((gamePane.getMinWidth()/4) - enemy1.getWidth()/2);
		enemy1.getImageView().setY((gamePane.getMinHeight()/4) - enemy1.getHeight()/2);
		enemy1.getImageView().setViewport(enemy1.getIdleRight());
		enemy1.setMovement(-1, 0);

		enemy2 = new Reaper();
		enemy2.getImageView().setX(gamePane.getMinWidth() - gamePane.getMinWidth()/4);
		enemy2.getImageView().setY(gamePane.getMinHeight()/2);
		enemy2.getImageView().setViewport(enemy2.getIdleLeft());
		enemy2.setMovement(0, 1);

		HashSet<Entity> enemies = new HashSet<Entity>();
		enemies.add(enemy1);
		enemies.add(enemy2);
		darkStation1.setEnemies(enemies);

		enemy3 = new Deceased();
		enemy3.getImageView().setX(gamePane.getMinWidth() - gamePane.getMinWidth()/3);
		enemy3.getImageView().setY(gamePane.getMinHeight()/3);
		enemy3.setMovement(1,0);

		enemy4 = new Deceased();
		enemy4.getImageView().setX(gamePane.getMinWidth() - 3*gamePane.getMinWidth()/4);
		enemy4.getImageView().setY(gamePane.getMinHeight() - gamePane.getMinHeight()/4);
		enemy4.setMovement(1,0);

		enemy5 = new Deceased();
		enemy5.getImageView().setX(gamePane.getMinWidth() - gamePane.getMinWidth()/5);
		enemy5.getImageView().setY(gamePane.getMinHeight() - 3*gamePane.getMinWidth()/5);
		enemy5.setMovement(0,1);

		HashSet<Entity> enemies2 = new HashSet<Entity>();
		enemies2.add(enemy3);
		enemies2.add(enemy4);
		enemies2.add(enemy4);
		enemies2.add(enemy5);
		darkStation2.setEnemies(enemies2);

		setupMovement();
	}

	public static void resetBackground(BackgroundLayer background, boolean isReturn) {
		gamePane = background.getPane();
		currentLayer = background;
		spawnX = background.getSpawnX(isReturn);
		spawnY = background.getSpawnY(isReturn);
		// Avoid the instance in which we have not yet initialized the player, or we already have a player on screen.
		if(player != null) {
			if(!gamePane.getChildren().contains(player.getImageView())) {
				gamePane.getChildren().addAll(player.getImageView());
				player.getImageView().toBack();
				background.getImageView().toBack();
				resetPlayerPosition();
			}
		}
		gameScene.setRoot(gamePane);
	}

	public static void resetPlayerPosition() {
		player.resetHitBox();
		player.getImageView().setLayoutX(spawnX);
		player.getImageView().setLayoutY(spawnY);
		player.getImageView().setViewport(player.getIdleRight());
	}

	private static void setupMovement() {
		// Game loop
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				double dx = 0, dy = 0;

				if(player.getAnimation().isAnimateUp()) {
					dy -= 1.5;
				}
				if(player.getAnimation().isAnimateDown()) {
					dy += 1.5;
				}
				if(player.getAnimation().isAnimateLeft()) {
					dx -= 1.5;
				}
				if(player.getAnimation().isAnimateRight()) {
					dx += 1.5;
				}
				// Player movement
				player.updatePosition(now, currentLayer, dy, dx);


				// Enemy movement
				HashSet<Entity> enemies = currentLayer.getEnemies();
				for(Entity e : enemies) {
					e.updatePosition(now, gamePane.getWidth(), gamePane.getHeight(), enemies);

					// Collision for players with enemies.
					if(player.getHitBox().intersects(e.getHitBox())) {
						stopMovement();
						mediaPlayer.stop();
						DeathState.playMediaPlayer();
						GameApplication.setScene(DeathState.getScene());
						// Battle State transitions not functioning.
//						BattleState battle = new BattleState(player, e);
//						GameApplication.setScene(battle.getScene());
						stop();
					}
					// TODO: Collisions between enemies. Enemies having bounds.
				}
			}
		};
		timer.start();
	}

	private static void setupKeyPresses() {
		// Animations with directional keys.
		gameScene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP:
				player.animateUp();
				break;
			case DOWN:
				player.animateDown();
				break;
			case LEFT:
				player.animateLeft();
				break;
			case RIGHT:
				player.animateRight();
				break;
			default:
				break;
			}
		});
	}

	private static void setupKeyReleases() {
		// Sets animation idle on key release. Also, checks edge cases in checkAnimation()
		gameScene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case UP:
				player.stopAnimationUp();
				break;
			case DOWN:
				player.stopAnimationDown();
				break;
			case LEFT:
				player.stopAnimationLeft();
				break;
			case RIGHT:
				player.stopAnimationRight();
				break;
			default:
				break;
			}
		});
	}

	private static void stopMovement() {
		player.stopAnimationUp();
		player.stopAnimationDown();
		player.stopAnimationLeft();
		player.stopAnimationRight();
	}

	public static Scene getScene() {
		return gameScene;
	}

	public static void startAnimationTimer() {
		timer.start();
	}

	public static void playMediaPlayer() {
		mediaPlayer.play();
	}
}
