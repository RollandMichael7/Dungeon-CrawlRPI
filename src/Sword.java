
public class Sword extends Item {
	// image source:
	// http://elderscrolls.wikia.com/wiki/Steel_Sword_(Skyrim)
	private int attack;

	public Sword(int weight, int atk) {
		super(weight);
		name = "Sword";
		attack = atk;
		portraitURL = "https://i.imgur.com/LKxaPw8.jpg";
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub

	}

}
