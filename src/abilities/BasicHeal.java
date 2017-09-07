package abilities;

import entities.Entity;

public class BasicHeal extends Ability {

	private static final String imageFilePath = "redacted/resources/ability_heal.png";

	private int healthPoints;

	public BasicHeal() {
		super(imageFilePath);
		healthPoints = 10;
		cooldown = 5;
	}

	// Make sure to pass in the player character when using heal effect.
	public void effect(Entity other) {
		other.restoreHealth(healthPoints);
		animateCooldown();
	}

}
