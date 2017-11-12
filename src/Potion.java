
public class Potion extends Item {
	private int potency;
	
	// image source:
	// https://skyrim.gamepedia.com/Potion_of_Invisibility
	public Potion(int weight, int potency) {
		super(weight);
		portraitURL = "https://i.imgur.com/xH8QcjN.jpg";
		this.potency = potency;
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub

	}

}
