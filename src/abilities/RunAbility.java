package abilities;
import java.util.Random;

import entities.Entity;

public class RunAbility extends Ability {

	private static final String imageFilePath = "redacted/resources/ability_run.png";

	private static final Random rand = new Random();

	public RunAbility() {
		super(imageFilePath);
		cooldown = 5;
	}

	public void effect(Entity other) {
		animateCooldown();
	}

	public int getRunChance() {
		return rand.nextInt(2);
	}
}
