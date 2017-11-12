
public abstract class Item {
	protected String name;
	protected int weight;
	protected int attack;
	protected double potency;
	protected String portraitURL;
	
	public abstract void use();
	
	public Item(int weight) {this.weight = weight;};
	public String getPortrait() { return portraitURL; }
	public int weight() { return weight; }
	public int attack() { return attack; }
	public double potency() { return potency; }
	
	@Override
	public String toString() {
		return name;
	}
}
