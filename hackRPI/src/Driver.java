import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Driver extends Application {

	// image source:
	// https://duality587.wordpress.com/category/legend-of-grimrock/

	private static final String BG_PORTRAIT = "https://i.imgur.com/M3qtGs9.jpg";
	private static final String STAIRS_PORTRAIT = "https://i.imgur.com/1nAgjvl.jpg";

	private Map map;
	private int level = 1;
	private Player p;

	private BorderPane game;
	private ImageView portrait;

	private int reputation = 0;

	public Text mapText;
	public Text console;
	public TextField input;
	public ListView scroll;

	private Text health;
	private Text weight;
	private Text attack;
	private Text exp;

	private Item item;

	private Monster target;
	private Text enemyHealth;

	private String inputText;

	private boolean stairsInput = false;
	private boolean monsterInput = false;
	private boolean itemInput = false;

	public EventHandler<KeyEvent> inputHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent e) {
			if (e.getCode() == KeyCode.ENTER) {
				inputText = input.getText();
				println("> " + inputText);
				input.setText("");
				if (input.getText().toUpperCase() == "INV")
					manageInv();
				if (stairsInput)
					parseStairsInput();
				else if (monsterInput)
					parseMonsterInput();
				else if (itemInput)
					parseItemInput();
			}
		}
	};

	public EventHandler<KeyEvent> movementHandler = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent e) {

			if (e.getCode() == KeyCode.UP) {
				tile = map.moveVertical(-1);
				if (tile == '0')
					console.setText(console.getText() + "\nYou hit a wall!");
				else
					updateMap();
			} else if (e.getCode() == KeyCode.DOWN) {
				tile = map.moveVertical(1);
				if (tile == '0')
					println("You hit a wall!");
				else
					updateMap();
			} else if (e.getCode() == KeyCode.LEFT) {
				tile = map.moveHorizontal(-1);
				if (tile == '0')
					println("You hit a wall!");
				else
					updateMap();
			} else if (e.getCode() == KeyCode.RIGHT) {
				tile = map.moveHorizontal(1);
				if (tile == '0')
					println("You hit a wall!");
				else
					updateMap();
			} else if (e.getCode() == KeyCode.ENTER)
				if (input.getText().toUpperCase() == "INV") {
					println("> " + inputText);
					input.setText("");
					manageInv();
				}
		}
	};
	private char tile;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		game = new BorderPane();
		VBox vbox = new VBox(10);
		scroll = new ListView();
		mapText = new Text();
		console = new Text(
				"Welcome." + "\nUse the arrow keys to move." + "\nLEGEND" + "\nP - Player" + "\nS - Staircase"
						+ "\nM - Monster" + "\nm - Deceased Monster" + "\n* - Pacified Monster" + "\nI - Item");
		mapText.setTextAlignment(TextAlignment.CENTER);
		console.setTextAlignment(TextAlignment.CENTER);
		console.setStyle("-fx-alignment: center");

		input = new TextField();

		p = new Player(100, 100);
		map = new Map(level, p);
		map.addMonsters();

		input.setOnKeyPressed(movementHandler);

		StackPane stack1 = new StackPane();
		stack1.getChildren().add(mapText);

		mapText.setFont(Font.font("Monospaced"));
		mapText.setText(map.toString());

		HBox stats = new HBox(5);
		health = new Text("Health: " + p.health());
		health.setFill(Color.GREEN);
		weight = new Text("Weight: " + p.getWeightString());
		weight.setFill(Color.BROWN);
		attack = new Text("Attack: " + p.attack());
		attack.setFill(Color.RED);
		exp = new Text("Experience: " + p.getProgress());
		exp.setFill(Color.BLUE);

		stats.getChildren().addAll(health, weight, attack, exp);

		scroll.getItems().add(console);
		scroll.setStyle("-fx-alignment: center");

		vbox.getChildren().addAll(stack1, scroll, input);
		vbox.setAlignment(Pos.CENTER);

		portrait = new ImageView(BG_PORTRAIT);
		portrait.fitHeightProperty().set(300);
		portrait.fitWidthProperty().set(300);
		portrait.preserveRatioProperty().set(true);

		enemyHealth = new Text();
		enemyHealth.setTextAlignment(TextAlignment.CENTER);
		VBox enemyStats = new VBox(5);
		enemyStats.getChildren().addAll(portrait, enemyHealth);

		Button start = new Button("Enter the Dungeon");
		start.setOnMouseClicked(e -> {
			game.setCenter(vbox);
			game.setRight(enemyStats);
			input.requestFocus();
		});
		start.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				game.setCenter(vbox);
				game.setRight(enemyStats);
				input.requestFocus();
			}
		});

		game.setTop(stats);
		ImageView image = new ImageView("https://i.imgur.com/8ys71mP.jpg");
		StackPane imageP = new StackPane();
		image.fitWidthProperty().bind(stage.widthProperty());
		image.fitHeightProperty().bind(stage.heightProperty());
		imageP.getChildren().addAll(image, start);
		Scene scene = new Scene(game, 800, 450);
		stage.setScene(scene);

		game.setCenter(imageP);
		stats.setAlignment(Pos.CENTER);

		stage.show();
		stage.setTitle("hackRPI 2017");
		start.requestFocus();
	}

	public void updateMap() {
		portrait.setImage(new Image(BG_PORTRAIT));
		mapText.setText(map.toString());
		if (tile == 'S') {
			portrait.setImage(new Image(STAIRS_PORTRAIT));
			println("You found the stairs! Go down them? (Y/N)");
			getResponse('S');
		}

		else if (tile == 'I') {
			println("You see something on the floor...");
			item = map.getItem(map.getY(), map.getX());
			portrait.setImage(new Image(item.getPortrait()));
		}

		else if (tile == 'M') {
			println("You encounter a monster!");
			target = map.getMonster(map.getY(), map.getX());
			portrait.setImage(new Image(target.getPortrait()));
			enemyHealth.setText("Health: " + target.currentHealth());

			println("It's a " + target + " with " + target.health() + " health!");
			println("It " + target.atkType() + " you for " + target.attack() + " damage!");
			if (target.attack(p)) {
				println("YOU DIED.");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Oh no!");
				alert.setHeaderText(null);
				alert.setContentText(
						"You were killed by a " + target + ". You had a reputation of " + reputation + ".");
				alert.showAndWait();
				System.exit(0);
			} else {
				println("You have " + p.health() + " health left.");
				println("Fight back? (Y/N)");
				getResponse('M');
			}
			updateStats();
		}
	}

	public void getResponse(char key) {
		stairsInput = false;
		monsterInput = false;
		itemInput = false;
		if (key == 'S')
			stairsInput = true;
		else if (key == 'M')
			monsterInput = true;
		else if (key == 'I')
			itemInput = true;
		input.removeEventHandler(KeyEvent.KEY_PRESSED, movementHandler);
		input.setOnKeyPressed(inputHandler);
	}

	public void parseStairsInput() {
		if (inputText.toUpperCase().equals("Y")) {
			nextLevel();
			startMovement();
		} else if (inputText.toUpperCase().equals("N")) {
			println("You pass over the stairs.");
			startMovement();
		} else {
			getResponse('S');
			println("Huh?");
		}
		portrait.setImage(new Image(BG_PORTRAIT));
	}

	public void parseMonsterInput() {
		if (inputText.toUpperCase().equals("Y")) {
			int power = (int) (Math.random() * p.attack() + 10);
			boolean won = p.attack(target, power);
			println("You attack and do " + power + " damage.");
			enemyHealth.setText(target.currentHealth());
			if (won) {
				portrait.setImage(new Image(BG_PORTRAIT));
				println("You ruthlessly murdered the " + target + ".");
				println("You gained " + target.exp() + " experience.");
				if (reputation > -10 && reputation < 10)
					println("It had to be you or it.");
				else {
					if (reputation < -150)
						println("What have you done.");
					else if (reputation < -100)
						println("Killing these monsters is your divine purpose. You are a god.");
					else if (reputation < -75)
						println("You feel nothing. You don't even remember how you got here, but you have a new purpose now.");
					else if (reputation < -50)
						println("It was nothing but a slight inconvenience.");
					else if (reputation < -25)
						println("It never had a chance. You could've spared it, and you didn't.");
					else if (reputation < -10)
						println("You aren't exactly happy to do it, but you wouldn't hesitate to do it again.");
					else if (reputation > 30)
						println("You pray that the Gods above will forgive you for your transgressions.");
					else if (reputation > 20)
						println("Your heart aches for these creatures, but sometimes you have no choice.");
					else if (reputation > 10)
						println("It was mercy.");
				}
				reputation -= target.maxHealth();
				map.destroyMonster(map.getY(), map.getX());
			} else
				reputation -= power;
			updateStats();
			startMovement();
		} else {
			if (reputation < -100) {
				println("The " + target + " knows who you are. It won't let you get away!");
				updateMap();
			} else {
				println("The " + target + " respects your submission and promises not to attack again.");
				println("You feel proud of yourself.");
				reputation += target.maxHealth();
				map.pacifyMonster(map.getY(), map.getX());
			}
		}
		startMovement();
	}

	public void parseItemInput() {
		if (inputText.toUpperCase().equals("Y")) {
			println("You decide to pick up the item. It weighs "+item.weight()+" units.");
			if (item.weight() + p.getCurrentWeight() > p.getCapacity())
				println("It's too heavy!");
			else {
				p.addItem(item);
				if (item.attack() >= 0)
					println("You got a sword!" + "\nYour attack increased by " + item.attack() + "!");
				else {
					// TODO: let player choose when to use potion
					println("You got a potion!" + "\nIt seems potent enough to increase your health by "
							+ item.potency() + " percent!");
					p.consumePotion(item);
				}
			}
		}
		startMovement();
	}
	
	// TODO: make inventory management work
	public void manageInv() {
		println(p.getInventory());
		println("Select an item to use/drop.");
	}

	public void startMovement() {
		input.removeEventHandler(KeyEvent.KEY_PRESSED, inputHandler);
		input.setOnKeyPressed(movementHandler);
	}

	public void println(String s) {
		scroll.getItems().add(new Text(s));
		scroll.scrollTo(scroll.getItems().size() - 1);
		console.setText(console.getText() + "\n" + s);
	}

	public void nextLevel() {
		tile = '0';
		map = new Map(++level, p);
		println("You advanced to floor " + level + "!");
		if (reputation < -20 || reputation > 20) {
			println("Your reputation precedes you.");
			if (reputation < -20)
				println("The monsters have heard about your murder spree.");
			else if (reputation < -50)
				println("The monsters know to fear you, but do not fear death.");
			else if (reputation < -100)
				println("The monsters are determined to stop you.");
			else if (reputation < -150) {
				println("The monsters have no chance of recovery from this genocide. Either you will die here, or their species will.");
				map.fillMonsters();
			}
		}
		map.addMonsters();
		map.addItems();
		updateMap();
		if (level > 5) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Congratulation");
			alert.setHeaderText(null);
			alert.setContentText("You got past all 5 levels! Good job!");
			alert.showAndWait();
			System.exit(0);
		}
	}

	public void updateStats() {
		health.setText("Health: " + p.health());
		if (p.health() > 75)
			health.setFill(Color.GREEN);
		else if (p.health() < 25)
			health.setFill(Color.DARKRED);
		else if (p.health() < 50)
			health.setFill(Color.ORANGE);
		else if (p.health < 75)
			health.setFill(Color.YELLOW);

		weight.setText("Weight: " + p.getWeightString());
		attack.setText("Attack: " + p.attack());
		exp.setText("Experience: " + p.getProgress());
	}
}
