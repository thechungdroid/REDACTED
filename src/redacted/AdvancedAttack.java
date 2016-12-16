package redacted;

public class AdvancedAttack extends Ability {

	private static final String imageFilePath = "redacted/resources/ability_2.png";

	private int damage;

	public AdvancedAttack() {
		super(imageFilePath);
		damage = 20;
		cooldown = 5;
	}

	public void effect(Entity other) {
		other.subtractHealth(damage);
		animateCooldown();
	}

}
