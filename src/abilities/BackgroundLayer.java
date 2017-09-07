package abilities;
import java.util.HashSet;
import java.util.Set;

import entities.Entity;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import redacted.GameApplication;

public class BackgroundLayer {

	private Pane gamePane;
	private ImageView backgroundImageView;

	// May need to add a set of bounds to incorporate more complex obstacles.
	// Loop through multiple Recangle2D's to see if an entity intersects with objects.
	private Set<Rectangle2D> bounds;
	private HashSet<Double> exits;
	private HashSet<Entity> enemies;
	private int spawnX;
	private int spawnY;
	private int returnSpawnX;
	private int returnSpawnY;

	public BackgroundLayer(String imageFilePath, Set<Rectangle2D> bounds, int spawnX, int spawnY) {
		gamePane = new Pane();
		gamePane.setMinWidth(GameApplication.APP_WIDTH);
		gamePane.setMinHeight(GameApplication.APP_HEIGHT);
		backgroundImageView = new ImageView(imageFilePath);
		this.bounds = bounds;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		gamePane.getChildren().addAll(backgroundImageView);
	}

	public Pane getPane() {
		return gamePane;
	}

	public ImageView getImageView() {
		return backgroundImageView;
	}

	public Set<Rectangle2D> getBounds() {
		return bounds;
	}

	public void addBound(Rectangle2D bound) {
		if(bounds == null) {
			bounds = new HashSet<Rectangle2D>();
		}
		bounds.add(bound);
	}

	public void setEnemies(HashSet<Entity> enemies) {
		this.enemies = enemies;
		for(Entity e : enemies) {
			gamePane.getChildren().add(e.getImageView());
		}
	}

	public HashSet<Entity> getEnemies() {
		return enemies;
	}

	public void removeEnemy(Entity enemy) {
		enemies.remove(enemy);
	}

	public void setReturnLocation(int x, int y) {
		returnSpawnX = x;
		returnSpawnY = y;
	}

	// Spawn location takes a boolean to indicate whether the player is returning to the location from an exit point.
	public int getSpawnX(boolean isReturn) {
		if(isReturn) {
			return returnSpawnX;
		} else {
			return spawnX;
		}
	}

	public int getSpawnY(boolean isReturn) {
		if(isReturn) {
			return returnSpawnY;
		} else {
			return spawnY;
		}
	}

	public void setExit(double value) {
		if(exits == null) {
			exits = new HashSet<Double>();
		}
		exits.add(value);
	}

	public boolean isExit(double value) {
		if(exits.contains(value)) {
			return true;
		} else {
			return false;
		}
	}
}
