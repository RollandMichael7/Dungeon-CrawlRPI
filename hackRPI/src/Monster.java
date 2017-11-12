
public class Monster extends Entity {
	private String name;
	private String atkType;
	private String portraitURL;
	
	
	/* Image sources:
	 * http://wowwiki.wikia.com/wiki/File:3D-Orc.png
	 * https://www.artstation.com/artwork/dungeon-and-dragon-monster-manual
	 * http://ex515.wikia.com/wiki/Pizza_Steve
	 * http://pngimg.com/imgs/fantasy/zombie/
	 * http://vsbattles.wikia.com/wiki/File:Castlevania_Chronicles_Dracula.png
	 * http://annoying-apple.wikia.com/wiki/File:Peanut_butter_jelly_time_8dddd_by_mariomario54321-d35zla9.png
	 */
	private static final String[][] monsters = {
			{"Orc" , "bashes", "https://i.imgur.com/EK3k4gX.jpg"},
			{"Goblin", "attacks", "https://i.imgur.com/oCH4Ecg.jpg"},
			{"Wolf Thing", "bites", "https://i.imgur.com/MVPu9MI.jpg"},
			{"Living Slice of Pizza", "throws pepperoni at", "https://i.imgur.com/Hn51ISb.jpg"},
			//{"Lost Old Man", "chastises"},
			//{"Eldritch Horror", "looks at"},
			{"Zombie", "bites", "https://i.imgur.com/vimu3FJ.jpg"},
			{"Vampire", "bites", "https://i.imgur.com/nXnpdEz.jpg"},
			{"Banana Man", "peanut butter and jellies", "https://i.imgur.com/GZFs35M.jpg"}
	};
	
	
	public Monster(int hp, int atk) {
		super(hp);
		attack = atk;
		exp += (atk*2);
		int i = (int) (Math.random() * monsters.length);
		name = monsters[i][0];
		atkType = monsters[i][1];
		portraitURL = monsters[i][2];
	}
	
	public String atkType() {
		return atkType;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public int health() {
		return health;
	}
	
	public String currentHealth() {
		return health+" / "+maxHealth;
	}
	
	public String getPortrait() {
		return portraitURL;
	}
}
