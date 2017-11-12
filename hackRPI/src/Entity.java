
public abstract class Entity {
	protected int health;
	protected int attack=0;
	protected int exp;
	protected int maxHealth;

	public Entity(int health) {
		exp = health;
		this.health = health;
		this.maxHealth = health;
	}
	
	public int health() { return health; }
	public int maxHealth() { return maxHealth; }
	public int attack() { return attack; }
	public int exp() { return exp; }
	
	public boolean takeDamage(int dmg) {
		health -= dmg;
		if (health <=0)
			return true;
		return false;
	}

	public boolean attack(Entity e) {
		return e.takeDamage((int) (Math.random() * this.attack + 10));
	}
}
