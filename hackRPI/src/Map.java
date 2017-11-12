import java.util.Random;

public class Map {
	private char[][] rooms;
	private int size;
	private int level;

	private Monster[][] activeMonsters;
	private Item[][] activeItems;

	private Random RNG = new Random();
	private Player p;
	private boolean playerDropped;
	private boolean stairsDropped;
	private int playerX;
	private int playerY;
	private int stairsI;
	private int stairsJ;

	private char[][] revealedRooms;

	public Map(int level, Player p) {
		playerDropped = false;
		stairsDropped = false;

		this.size = (int) Math.pow(2, level);
		this.level = level;
		int x;
		this.p = p;
		
		rooms = new char[size][size];
		revealedRooms = new char[size][size];
		activeMonsters = new Monster[size][size];
		activeItems = new Item[size][size];

		while (!playerDropped || !stairsDropped) {
			stairsDropped = false;
			playerDropped = false;
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++) {
					revealedRooms[i][j] = ' ';

					x = RNG.nextInt(10);
					if (x == 0 && !playerDropped) {
						rooms[i][j] = 'X';
						revealedRooms[i][j] = 'P';
						playerX = j;
						playerY = i;
						playerDropped = true;
					} else if (x == 1 && !stairsDropped) {
						rooms[i][j] = 'S';
						stairsI = i;
						stairsJ = j;
						stairsDropped = true;
					} else
						rooms[i][j] = 'X';
				}
		}
	}

	public char moveHorizontal(int dx) {
		if (playerX + dx >= 0 && playerX + dx < size) {
			revealedRooms[playerY][playerX] = rooms[playerY][playerX];
			revealedRooms[playerY][playerX + dx] = 'P';
			playerX += dx;
			return rooms[playerY][playerX];
		} else {
			return '0';
		}
	}

	public char moveVertical(int dy) {
		if (playerY + dy >= 0 && playerY + dy < size) {
			revealedRooms[playerY][playerX] = rooms[playerY][playerX];
			revealedRooms[playerY + dy][playerX] = 'P';
			playerY += dy;
			return rooms[playerY][playerX];
		} else {
			return '0';
		}
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (revealedRooms[i][j] != ' ')
					s += "[" + revealedRooms[i][j] + "]";
				else
					s += "   ";
			}
			s += "\n";
		}
		return s;

	}

	public void addMonsters() {
		int i, j;
		Monster m;
		for (int k = 0; k < level * 3; k++) {
			i = stairsI;
			j = stairsJ;
			while (rooms[i][j] == 'S' || rooms[i][j] == 'M' || rooms[i][j] == 'I') {
				i = RNG.nextInt(size);
				j = RNG.nextInt(size);
			}
			rooms[i][j] = 'M';
			int health = 0, attack = 0;
				health = (int) (Math.random() * (level * 15) + (level*5));
				attack = (int) (Math.random() * (level * 5) + (level*5));
				m = new Monster(health, attack);
				activeMonsters[i][j] = m;
		}
	}

	public Monster getMonster(int i, int j) {
		return activeMonsters[i][j];
	}

	public void pacifyMonster(int i, int j) {
		activeMonsters[i][j] = null;
		rooms[i][j] = '*';
	}

	public void destroyMonster(int i, int j) {
		rooms[i][j] = 'm';
		revealedRooms[i][j] = 'm';
		activeMonsters[i][j] = null;
	}

	public void fillMonsters() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (rooms[i][j] != 'P' && rooms[i][j] != 'S')
					rooms[i][j] = 'M';
	}

	public void addItems() {
		int i, j;
		Item item;
		for (int k = 0; k < level - 2; k++) {
			i = stairsI;
			j = stairsJ;
			while (rooms[i][j] == 'S' || rooms[i][j] == 'M' || rooms[i][j] == 'I') {
				i = RNG.nextInt(size);
				j = RNG.nextInt(size);
			}
			rooms[i][j] = 'I';
			int weight = (int) (Math.random() * 25);
			int type = (int) (Math.random() * 2);
			if (type == 1) {
				int atk = (int) (Math.random() * 10 * level);
				item = new Sword(weight, atk);
			} else {
				item = new Potion(weight);
			}
			activeItems[i][j] = item;
		}
	}

	public Item getItem(int i, int j) {
		return activeItems[i][j];
	}

	public void consumeItem(int i, int j) {
		rooms[i][j] = 'i';
		revealedRooms[i][j] = 'i';
		activeItems[i][j] = null;
	}

	public int getX() {
		return playerX;
	}

	public int getY() {
		return playerY;
	}

}
