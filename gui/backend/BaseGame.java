package gui.backend;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class BaseGame extends JFrame {

	/**
	 * suit priority order for project
	 */
	static final String[] suits = new String[] { "Hearts", "Spades", "Clubs", "Diamonds" };

	/**
	 * project names (global)
	 */
	private static final ArrayList<String> names = new ArrayList<>();

	private JTextArea textArea;
	private JButton button;

	public BaseGame(String title, int width, int height, LayoutManager layout) {
		super(title);

		this.setLayout(layout);

		textArea = new JTextArea();
		button = new JButton("Click Me!");

		add(textArea, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);

		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * returns the requested player's name
	 *
	 * @param index player's index (player's number - 1)
	 * @return player's name
	 */
	public static String getName(int index) {
		return names.get(index);
	}

	/**
	 * checks if the 'names' list has a player's value
	 *
	 * @param playerNum player's number
	 * @return if the list has the player's value
	 */
	protected static boolean hasPlayer(int playerNum) {
		return names.size() > playerNum;
	}

	/**
	 * adds or changes the name of a player in the list
	 *
	 * @param index player's index
	 * @param name  name to place in list
	 */
	protected static void setName(int index, String name) {
		if (names.size() < index) {
			System.out.println("Something went wrong with the names. Please try again.");
		}
		if (names.size() == index) {
			names.add(name);
		} else {
			names.set(index, name);
		}
	}

	/**
	 * asks player to input their name (and checks if the input is correct)
	 *
	 * @param index   player's index
	 * @param message prompt message to ask for the name
	 */
	public static void promptName(int index, String message) {
		Scanner scanner = new Scanner(System.in);
		if (names.size() <= index) {
			while (true) {
				System.out.print(message);
				setName(index, scanner.nextLine());
				System.out.print("Does the name '" + names.get(index) + "' work for you? ");
				char works = scanner.next().toLowerCase().charAt(0);
				if (works == 'n') {
					System.out.println("Okay, you can try again.\n");
				} else if (works == 'y') {
					System.out.println("Okay, moving on.\n");
					break;
				} else {
					System.out.println("A wrong input was entered. Please try again.");
				}
			}
		}
		scanner.close();
	}

	/**
	 * amount of coins the player has to play with
	 */
	private static int coins;

	/**
	 * @return amount of coins
	 */
	protected static int getCoins() {
		return coins;
	}

	/**
	 * @param gain coins to add to total
	 */
	protected static void addCoins(int gain) {
		coins += gain;
	}

	/**
	 * @param loss coins to subtract from total
	 */
	protected static void subtractCoins(int loss) {
		coins -= loss;
	}

	static void checkCoins() {
		if (coins <= 0) {
			System.out.println("Oh no! Your coin total has fallen below 0! We refilled your total to 40.");
			coins = 40;
		}
	}

	/**
	 * @return the cost to play this game
	 */
	public int cost() {
		return 0;
	}

	/**
	 * addition to project to remove busy-waiting warnings with Thread.sleep()
	 *
	 * @param mil Thread.sleep input
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	public static void sleep(long mil) throws InterruptedException {
		Thread.sleep(mil);
	}

	/**
	 * plays a game
	 *
	 * @return if the player won the game (0 = loss, 1 = tie, 2 = won)
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	public int play() throws InterruptedException {
		System.out.println("Error: Something went wrong. Please try again.");
		return 1;
	}

	/**
	 * @return rules for the game
	 */
	public String rules() {
		return "Play the game.";
	}

	/**
	 * @return name of the game
	 */
	public String toString() {
		return "A Game";
	}

	/**
	 * @return max amount of players that can play the game
	 */
	protected int playerLimit() {
		return 0;
	}
}
