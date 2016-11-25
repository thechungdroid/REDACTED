import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

// This state is initialized when the player intersects with an enemy entity in the GameState.
// This will either transition into the DeathState if the player fails, or back to the GameSate if the player defeats the enemy.
public class BattleState {

	private Pane battlePane;
	private Scene battleScene;

//	static {
//		battlePane = new Pane();
//		battlePane.setMinWidth(GameApplication.APP_WIDTH);
//		battlePane.setMinHeight(GameApplication.APP_HEIGHT);
//		battlePane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
//		battlePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(48, 12, 48, 12))));
//		battleScene = new Scene(battlePane);
//	}

	public BattleState(Frog player, Entity enemy) {
		battlePane = new Pane();
		battlePane.setMinWidth(GameApplication.APP_WIDTH);
		battlePane.setMinHeight(GameApplication.APP_HEIGHT);
		battlePane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		battlePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(48, 12, 48, 12))));

		player.getImageView().relocate(GameApplication.APP_WIDTH/4 - player.getWidth()/2, GameApplication.APP_HEIGHT/2 - player.getHeight()/2);
		player.getImageView().setTranslateX(GameApplication.APP_WIDTH/4 - player.getWidth()/2);
		player.getImageView().setTranslateY(GameApplication.APP_HEIGHT/2 - player.getHeight()/2);
		player.getImageView().setViewport(player.getIdleRight());
		HealthBar healthBar = new HealthBar();
//		healthBar.animateBar(-100);
		healthBar.layoutXProperty().bind(player.getImageView().layoutXProperty());
		healthBar.layoutYProperty().bind(player.getImageView().layoutYProperty());
		healthBar.setTranslateX(player.getImageView().getX() - healthBar.getWidth()/4);
		healthBar.setTranslateY(player.getImageView().getY() - 30);

		enemy.getImageView().setTranslateX(3*GameApplication.APP_WIDTH/4 - enemy.getWidth()/2);
		enemy.getImageView().setTranslateY(GameApplication.APP_HEIGHT/2 - enemy.getHeight()/2);
		enemy.getImageView().setViewport(enemy.getIdleLeft());

		battlePane.getChildren().addAll(player.getImageView(), healthBar, enemy.getImageView());
		battleScene = new Scene(battlePane);
	}

	public Scene getScene() {
		return battleScene;
	}

}
