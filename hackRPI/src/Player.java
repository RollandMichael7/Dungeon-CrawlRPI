public class Player extends Entity {
	private int capacity;
	private int currentWeight;
	private int level;
	private int expToNextLevel;
	private Item[] inventory;

	public Player(int health, int capacity) {
		super(health);
		level = 1;
		exp = 0;
		expToNextLevel = 100;
		this.capacity = capacity;
		attack = 20;
	}

	public String getWeight() {
		return currentWeight + "/" + capacity;
	}

	@Override
	public boolean takeDamage(int dmg) {
		health -= dmg;
		if (health <= 0)
			return true;
		return false;
	}

	public boolean attack(Entity e, int power) {
		if (power > e.health()) {
			getExp(e);
		}
		int d20 = (int) (Math.random() * 21);
		if (d20 >= 20)
			return e.takeDamage(attack * 2);
		return e.takeDamage(power);
	}

	public boolean getExp(Entity e) {
		exp += e.exp();
		if (exp > expToNextLevel) {
			level++;
			attack *= 1.1;
			maxHealth *= 1.1;
			health = maxHealth;
			exp = (exp - expToNextLevel);
			expToNextLevel *= 1.5;
			return true;
		}
		return false;
	}

	public String getProgress() {
		return exp + "/" + expToNextLevel;
	}
}
