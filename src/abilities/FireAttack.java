package abilities;

import entities.Entity;

public class FireAttack extends Ability {

	// TODO: Bleed damage

	private static final String fileImagePath = "redacted/resources/ability_3.png";

	private int damage;
//	private int bleedDamage;
//	private int duration;

	public FireAttack() {
		super(fileImagePath);
		damage = 15;
//		bleedDamage = 2;
//		duration = 5;
		cooldown = 10;
	}

	public void effect(Entity other) {
		other.subtractHealth(damage);
		// TODO: Add bleed damage.
		animateCooldown();
	}
}
