package gamestates;
import java.util.HashSet;

import abilities.BackgroundLayer;
import animations.Deceased;
import entities.Entity;
import entities.Frog;
import entities.Reaper;
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
import redacted.GameApplication;

// The main game state wherein the player traverses the world and engages enemies.
// This state will either transition into the BattleState.
public class GameState {

	private static final String gameMusicFilePath = "resources/Tyler, The Creator - Rusty (Instrumental).mp3";
	private static final String darkStation1FilePath = "redacted/resources/dark_station.png";
	private static final String darkStation2FilePath = "redacted/resources/dark_station2.png";
	private static final String darkStation2ForegroundFilePath = "redacted/resources/dark_station2_foreground.png";

	private static Pane gamePane;
	private static Scene gameScene;
	private static MediaPlayer mediaPlayer;
	private static AnimationTimer timer;

	public static BackgroundLayer darkStation1;
	public static BackgroundLayer darkStation2;
	public static BackgroundLayer currentLayer;

//	private static ImageView backgroundImageView;
//	private static Rectangle2D backgroundBounds;
	private static double spawnX;
	private static double spawnY;

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
//		Media media = new Media(new File(gameMusicFilePath).toURI().toString());
		Media media = new Media(GameApplication.class.getResource(gameMusicFilePath).toString());
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
		ImageView ds2Foreground = new ImageView(new Image(darkStation2ForegroundFilePath));
		darkStation2.getPane().getChildren().add(ds2Foreground);
		ds2Foreground.toFront();

		// Initialize the beginning scene
		resetBackground(darkStation1, false);
	}

	public static void initEntities() {

		player = new Frog();

		// Level 1 enemies
		HashSet<Entity> enemies = new HashSet<Entity>();

		enemy1 = new Reaper();
		enemy1.getImageView().setX(gamePane.getMinWidth() - gamePane.getMinWidth()/4);
		enemy1.getImageView().setY(gamePane.getMinHeight()/2);
		enemy1.getImageView().setViewport(enemy1.getIdleLeft());
		enemy1.setMovement(0, 1);
		enemies.add(enemy1);

		// Normal mode
		if(GameApplication.getMode() > 0) {
			enemy2 = new Reaper();
			enemy2.getImageView().setX((gamePane.getMinWidth()/4) - enemy2.getWidth()/2);
			enemy2.getImageView().setY((gamePane.getMinHeight()/4) - enemy2.getHeight()/2);
			enemy2.getImageView().setViewport(enemy2.getIdleRight());
			enemy2.setMovement(-1, 0);
			enemies.add(enemy2);
		}
		// Hard mode
		if(GameApplication.getMode() > 1) {
			Entity hardEnemy = new Deceased();
			hardEnemy.getImageView().setX((gamePane.getMinWidth()/3));
			hardEnemy.getImageView().setY(gamePane.getMinHeight()/3);
			hardEnemy.getImageView().setViewport(hardEnemy.getIdleLeft());
			hardEnemy.setMovement(0, 1);
			enemies.add(hardEnemy);
		}

		darkStation1.setEnemies(enemies);

		// Level 2 enemies
		HashSet<Entity> enemies2 = new HashSet<Entity>();

		enemy3 = new Deceased();
		enemy3.getImageView().setX(gamePane.getMinWidth() - gamePane.getMinWidth()/3);
		enemy3.getImageView().setY(gamePane.getMinHeight() - gamePane.getMinHeight()/4);
		enemy3.getImageView().setViewport(enemy3.getIdleFront());
		enemy3.setMovement(1,0);
		enemies2.add(enemy3);

		// Normal mode
		if(GameApplication.getMode() > 0) {
			enemy4 = new Deceased();
			enemy4.getImageView().setX(gamePane.getMinWidth() - 3*gamePane.getMinWidth()/4);
			enemy4.getImageView().setY(gamePane.getMinHeight() - gamePane.getMinHeight()/2 + 40);
			enemy4.getImageView().setViewport(enemy4.getIdleFront());
			enemy4.setMovement(1,0);
			enemies2.add(enemy4);
		}

		// Hard mode
		if(GameApplication.getMode() > 1) {
			enemy5 = new Deceased();
			enemy5.getImageView().setX(gamePane.getMinWidth()/2 - 60);
			enemy5.getImageView().setY(gamePane.getMinHeight() - gamePane.getMinWidth()/2);
			enemy5.getImageView().setViewport(enemy5.getIdleFront());
			enemy5.setMovement(0,1);
			enemies2.add(enemy5);
		}

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

	public static void resetBackground(BackgroundLayer background, double spawnX, double spawnY) {
		gamePane = background.getPane();
		currentLayer = background;
		GameState.spawnX = spawnX;
		GameState.spawnY = spawnY;
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

	public static void resetEnemy(Entity enemy, double enemySpawnX, double enemySpawnY) {
		enemy.getImageView().setLayoutX(enemySpawnX);
		enemy.getImageView().setLayoutY(enemySpawnY);
		gamePane.getChildren().add(enemy.getImageView());
	}

	private static void setupMovement() {
		// Game loop
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				double dx = 0, dy = 0;

				// Animate again in case we have released the key in a different direction and are holding the key in the same direction.
				if(player.getAnimation().isAnimateUp()) {
					player.animateUp();
					dy -= 1.5;
				}
				if(player.getAnimation().isAnimateDown()) {
					player.animateDown();
					dy += 1.5;
				}
				if(player.getAnimation().isAnimateLeft()) {
					player.animateLeft();
					dx -= 1.5;
				}
				if(player.getAnimation().isAnimateRight()) {
					player.animateRight();
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
						BattleState battle = new BattleState(player, e, currentLayer);
						GameApplication.setScene(battle.getScene());
						stop();
					}
				}
			}
		};
		timer.start();
	}

	// Animations with directional keys.
	private static void setupKeyPresses() {
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

	// Sets animation idle on key release. Also, checks edge cases in checkAnimation()
	private static void setupKeyReleases() {
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

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
}
