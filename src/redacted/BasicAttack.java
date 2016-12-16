package redacted;

public class BasicAttack extends Ability {

	private static final String imageFilePath = "redacted/resources/ability_1.png";

	private int damage;

	public BasicAttack() {
		super(imageFilePath);
		cooldown = 4;
		damage = 10;
	}

	public void effect(Entity other) {
		other.subtractHealth(damage);
		animateCooldown();
	}

}
