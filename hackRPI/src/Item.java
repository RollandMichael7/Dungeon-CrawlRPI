
public abstract class Item {
	protected Map m;
	protected String name;
	protected int weight;
	protected String portraitURL;
	
	public abstract void use();
	
	public Item(Map m, int weight) {this.m=m; this.weight = weight;};
	public String portraitURL() { return portraitURL; }
	
	@Override
	public String toString() {
		return name;
	}
}
