
public abstract class Item {
	protected String name;
	protected int weight;
	protected String portraitURL;
	
	public abstract void use();
	
	public Item(int weight) {this.weight = weight;};
	public String getPortrait() { return portraitURL; }
	
	@Override
	public String toString() {
		return name;
	}
}
