package text.backend;

import text.games.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Directory {
	
	/**
	 * prompts player to choose a game to play
	 *
	 * @param games list of games to choose from
	 * @return game chosen
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private static Game getGame(ArrayList<Game> games) throws InterruptedException {
		int highestName = 0, highestPeople = 0, highestCost = 0;
		for (Game game: games) {
			highestName = Math.max(game.toString().length(), highestName);
			highestPeople = Math.max(String.valueOf(game.playerLimit()).length(), highestPeople);
			highestCost = Math.max(String.valueOf(game.cost()).length(), highestCost);
		}
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < games.size(); i++) {
			String limitSuffix = games.get(i).playerLimit() == 1 ? "person" : "people";
			int width = games.size() > 9 ? 2 : 1;
			
			System.out.printf("%" + width + "d: %-" + (highestName + 1) + "s ", i + 1, games.get(i) + ".");
			System.out.printf("As many as %" + highestPeople + "d %-6s can play. ", games.get(i).playerLimit(),
			                  limitSuffix);
			System.out.printf("It costs %" + highestCost + "d coins to play.\n\n", games.get(i).cost());
			Game.sleep(500);
		}
		while (true) {
			try {
				System.out.println("\nYou have " + Game.getCoins() + " coins.\nWhich game do you want to play? Select" +
						                   " the corresponding number, or input 0 to exit.");
				int index = scanner.nextInt();
				scanner.nextLine();
				if (index < 0 || index > games.size()) {
					System.out.println("That isn't a valid number! Try again.");
					continue;
				} else if (index == 0) {
					System.out.println("Okay. See you later!");
					return null;
				}
				return games.get(index - 1);
			} catch (InputMismatchException e) {
				System.out.println("That isn't a number! Try again.");
			}
			scanner.close();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("\n\n\n\n\n");
		System.out.println("Welcome to the Virtual Card Deck! We have numerous games to choose from, each of which " +
				                   "supply a virtual spin to a classic game! This experience is fueled by coins, " +
				                   "which you need to play games. You earn coins after wins! Once you lose too many " +
				                   "coins, we automatically refill them for you.\n");
		Game.addCoins(100);
		Game.promptName(0, "Please enter your name, so we can personalize your experience: ");
		ArrayList<Game> games = new ArrayList<>() {{
			add(new BlackJack());
			add(new BlackJack2());
			add(new CrazyEights());
			add(new GinRummy());
			add(new GoFish());
			add(new GOPS());
			add(new President());
			add(new SevenEight());
			add(new Trash());
			add(new War1());
			add(new War2());
		}};
		Collections.shuffle(games);
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n\n");
			Game game = getGame(games);
			if (game == null) {
				break;
			}
			if (game.cost() > Game.getCoins()) {
				System.out.println("Oh no! You can't play this game because you don't have enough coins!");
				char refill;
				while (true) {
					System.out.println("Would you like to refill your coins?");
					refill = scanner.nextLine().toLowerCase().charAt(0);
					if (refill != 'y' && refill != 'n') {
						System.out.println("That input didn't work. Please try again.");
						continue;
					}
					break;
				}
				if (refill == 'y') {
					Game.subtractCoins(Game.getCoins() - 1);
					Game.addCoins(game.cost());
				} else {
					continue;
				}
			}
			while (true) {
				System.out.println("Do you need to know the rules for " + game + "?");
				char agree = scanner.nextLine().toLowerCase().charAt(0);
				if (agree == 'y') {
					System.out.println("Okay.\n");
					System.out.println(game.rules());
					System.out.print("Enter something here once you are ready to play: ");
					scanner.nextLine();
					System.out.println("Let's go!");
				} else if (agree == 'n') {
					System.out.println("Okay.\n");
				} else {
					System.out.println("That wasn't a valid input. Please try again.");
					continue;
				}
				break;
			}
			Game.subtractCoins(game.cost());
			int earning = game.play();
			Game.addCoins(game.cost() * earning);
			Game.checkCoins();
			System.out.println("You earned " + game.cost() * earning + " coins.");
			System.out.println("\n\n");
		}
		scanner.close();
	}
}
