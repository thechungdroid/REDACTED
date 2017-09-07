package gamestates;
import java.util.ArrayList;

import abilities.Ability;
import abilities.AdvancedAttack;
import abilities.BasicAttack;
import abilities.BasicHeal;
import abilities.FireAttack;
import abilities.RunAbility;
import entities.Entity;
import entities.Frog;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import redacted.GameApplication;
import ui.HealthBar;

// This state is initialized when the player intersects with an enemy entity in the GameState.
// This will either transition into the DeathState if the player fails, or back to the GameSate if the player defeats the enemy.
public class BattleState {

	private Pane battlePane;
	private Scene battleScene;
	private AnimationTimer battleTimer;

	private Frog player;
	private Entity enemy;
	private BackgroundLayer currLayer;

	private double spawnX;
	private double spawnY;
	private double enemySpawnX;
	private double enemySpawnY;

	private ArrayList<Ability> abilities;
	// TODO: ENEMY ABILITIES
//	private ArrayList<Ability> enemyAbilities;

	private Ability enemyAttack;

	private EventHandler<KeyEvent> abilityEventHandler;

	private long currFrame;

	public BattleState(Frog player, Entity enemy, BackgroundLayer currLayer) {
		this.player = player;
		this.enemy = enemy;
		this.currLayer = currLayer;

		spawnX = player.getImageView().getLayoutX();
		spawnY = player.getImageView().getLayoutY();

		enemySpawnX = enemy.getImageView().getLayoutX();
		enemySpawnY = enemy.getImageView().getLayoutY();

		battlePane = new Pane();
		battlePane.setMinWidth(GameApplication.APP_WIDTH);
		battlePane.setMinHeight(GameApplication.APP_HEIGHT);
		battlePane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		battlePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(48, 12, 48, 12))));

		player.getBattleImageView().setX((GameApplication.APP_WIDTH/4 - player.getBattleWidth()/2));
		player.getBattleImageView().setY((GameApplication.APP_HEIGHT/2 - player.getBattleHeight()/2));
		player.getBattleImageView().setViewport(player.getBattleIdle());
		player.animateEnter();
		player.getBattleAnimation().setOnFinished(e -> {
			player.animateIdle();
		});

		abilities = player.getAbilities();
		// THE ENEMY HAS THE JUMP ON YOU, FOOL!
		for(Ability a : abilities) {
			a.animateGlobalCooldown();
		}

		HealthBar healthBar = player.getHealthBar();
		healthBar.layoutXProperty().bind(player.getBattleImageView().layoutXProperty());
		healthBar.layoutYProperty().bind(player.getBattleImageView().layoutYProperty());
		healthBar.setTranslateX(healthBar.getWidth()/2 - player.getBattleWidth()/2 + 75);
		healthBar.setTranslateY(player.getBattleImageView().getY() + 10);

		enemy.getImageView().setLayoutX((3*GameApplication.APP_WIDTH/4 - enemy.getWidth()/2)
				- enemy.getImageView().getLayoutBounds().getMinX());
		enemy.getImageView().setLayoutY((GameApplication.APP_HEIGHT/2 - enemy.getHeight()/2)
				- enemy.getImageView().getLayoutBounds().getMinY());
		// Possible TODO: Idle Animations?
		enemy.getAnimation().stop();
		enemy.getImageView().setViewport(enemy.getIdleLeft());

		HealthBar enemyHealthBar = enemy.getHealthBar();
		enemyHealthBar.layoutXProperty().bind(enemy.getImageView().layoutXProperty());
		enemyHealthBar.layoutYProperty().bind(enemy.getImageView().layoutYProperty());
		enemyHealthBar.setTranslateX(enemy.getImageView().getX() - enemyHealthBar.getWidth()/3);
		enemyHealthBar.setTranslateY(enemy.getImageView().getY() - 30);

		for(int i = 0; i < abilities.size(); i++) {
			Ability a = abilities.get(i);
			a.setLayoutX((i*50) + i*8 + 18);
			a.setLayoutY(375);
			battlePane.getChildren().add(a);
		}

		battlePane.getChildren().addAll(healthBar, player.getBattleImageView(), enemyHealthBar, enemy.getImageView());
		battleScene = new Scene(battlePane);

		setupKeyEvents();
		setupBattleLoop();
	}

	private void setupKeyEvents() {
		abilityEventHandler = new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				switch(event.getCode()) {
				case DIGIT1:
					if(!abilities.get(0).isAnimating()) {
						player.getBattleAnimation().stop();
						player.animateBasicAttack();
						abilities.get(0).effect(enemy);
						activateGlobalCooldowns(abilities.get(0));
					}
					break;
				case DIGIT2:
					if(!abilities.get(1).isAnimating()) {
						player.getBattleAnimation().stop();
						player.animateAdvancedAttack();
						abilities.get(1).effect(enemy);
						activateGlobalCooldowns(abilities.get(1));
					}
					break;
				case DIGIT3:
					if(!abilities.get(2).isAnimating()) {
						player.getBattleAnimation().stop();
						player.animateFireAttack();
						abilities.get(2).effect(enemy);
						activateGlobalCooldowns(abilities.get(2));
					}
					break;
				case DIGIT4:
					// HEAL
					// TODO: Animation
					if(!abilities.get(3).isAnimating()) {
						player.getBattleAnimation().stop();
						player.animateHeal();
						if(abilities.get(3) instanceof BasicHeal) {
							abilities.get(3).effect(player);
							activateGlobalCooldowns(abilities.get(3));
						}
					}
					break;
				case DIGIT5:
					// RUN
					if(!abilities.get(4).isAnimating()) {
						player.getBattleAnimation().stop();
						player.animateRun();
						abilities.get(4).effect(enemy);
						activateGlobalCooldowns(abilities.get(4));
						if(abilities.get(4) instanceof RunAbility) {
							RunAbility runAbility = (RunAbility)(abilities.get(4));
							switch (runAbility.getRunChance()) {
							case 0:
								break;
							case 1:
								// Should the player's health be restored if they escape?
								player.getBattleAnimation().setOnFinished(e -> {
									quitInstance(currFrame);
//									quitInstance();
									player.restoreHealth(player.getHealthBar().getMaxValue());
									player.getBattleAnimation().setOnFinished(null);
								});
								break;
							default:
								break;
							}
						}
					}
					break;
				default:
					break;
				}
			}

		};
		battleScene.setOnKeyPressed(abilityEventHandler);
	}

	private void setupBattleLoop() {
		// TODO: Placeholder ability
		/* Attacks change based on what game mode you are in.
		 */
		switch (GameApplication.getMode()) {
		case 0:
			enemyAttack = new BasicAttack();
			break;
		case 1:
			enemyAttack = new FireAttack();
			break;
		case 2:
			enemyAttack = new AdvancedAttack();
			break;
		default:
			break;
		}
		battleTimer = new AnimationTimer(){
			@Override
			public void handle(long now) {
				currFrame = now;
				// Check player animation.
				if(!player.getBattleAnimation().isAnimating()) {
					player.animateIdle();
				}
				// Enemy should attack periodically.
				if(now%100 == 0 && !enemyAttack.isAnimating()) {
					enemyAttack.effect(player);
				}
				// Enemy death
				// Bug: Sometimes death doesn't animate. Need to recreate.
				if(enemy.getHealthBar().getValue() == 0) {
					player.restoreHealth(player.getHealthBar().getMaxValue());
					currLayer.removeEnemy(enemy);
					quitInstance();
				}
				// Player death
				if(player.getHealthBar().getValue() == 0) {
					battleScene.removeEventHandler(KeyEvent.ANY, abilityEventHandler);
					GameState.getMediaPlayer().stop();
					player.animateDeath();
					EventHandler<ActionEvent> deathTransition = new EventHandler<ActionEvent>() {
						public void handle(ActionEvent event) {
							Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1750), e1 -> {
								GameApplication.setScene(DeathState.getScene());
								DeathState.playMediaPlayer();
								player.restoreHealth(player.getHealthBar().getMaxValue());
								enemy.restoreHealth(enemy.getHealthBar().getMaxValue());
								GameState.resetEnemy(enemy, enemySpawnX, enemySpawnY);
								player.getBattleAnimation().setOnFinished(null);
							}));
							timeline.play();
						}
					};
					player.getBattleAnimation().setOnFinished(deathTransition);
					stop();
				}
			}

		};
		battleTimer.start();
	}

	// quitInstance() without invincibility frames (enemy death)
	private void quitInstance() {
		resetCooldowns();
		GameState.resetBackground(currLayer, spawnX, spawnY);
		GameState.startAnimationTimer();
		GameApplication.setScene(GameState.getScene());
		battleTimer.stop();
	}

	// quitInstance() with invincibility frames (player run success)
	private void quitInstance(long currFrame) {
		resetCooldowns();
		GameState.resetEnemy(enemy, enemySpawnX, enemySpawnY);
		GameState.resetBackground(currLayer, spawnX, spawnY);
		GameState.startAnimationTimer();
		GameApplication.setScene(GameState.getScene());
		player.setInvincibilityFrames(currFrame);
		battleTimer.stop();
	}

	/* Activate global cooldowns for all other abilities other than
	 * the ability that is passed in the constructor.
	 * TODO: activate global cooldown if cooldown is within 2 seconds of finishing.
	 */
	private void activateGlobalCooldowns(Ability ability) {
		for(Ability a : abilities) {
			if(a != ability && !a.isAnimating()) {
				a.animateGlobalCooldown();
			}
		}
	}

	private void resetCooldowns() {
		for(Ability a : abilities) {
			a.stopCooldown();
		}
	}

	public Scene getScene() {
		return battleScene;
	}

}
