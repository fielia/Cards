package text.games;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;
import text.backend.Hand;

import java.util.Scanner;

public class RealWar extends Game {

	public int cost() {
		return 5;
	}

	public String rules() {
		return "This game is automatic. It compares the ranks of the top card in each player's deck, and gives both cards to the player with the higher rank, or completes a \"war\" in the ranks are the same.";
	}

	public String toString() {
		return "War (real version)";
	}

	@Override
	protected int playerLimit() {
		return 0;
	}

	private int compare(Hand handA, Hand handB, Hand cardsInPlay) {
		Card cardA = cardsInPlay.get(0);
		Card cardB = cardsInPlay.get(1);
		int wars = 0;
		if (cardA.getRankValue() > cardB.getRankValue()) {
			handA.addAll(cardsInPlay);
			System.out.println("Point for " + Game.getName(0) + "!");
		} else if (cardA.getRankValue() < cardB.getRankValue()) {
			handB.addAll(cardsInPlay);
			System.out.println("Point for " + Game.getName(1) + "!");
		} else {
			wars = tie(handA, handB, cardsInPlay);
			System.out.println("Tie!");
		}
		System.out.println(Game.getName(0) + "'s Card: " + cardA.getRank() + "\n" +
				Game.getName(1) + "'s Card: " + cardB.getRank() + "\n");
		return wars;
	}

	private int tie(Hand handA, Hand handB, Hand cardsInPlay) {
		for (int i = 0; i < 3; i++) {
			if (handA.size() == 1 || handB.size() == 1) {
				break;
			}
			cardsInPlay.add(handA.draw());
			cardsInPlay.add(handB.draw());
		}
		int wars = compare(handA, handB, cardsInPlay);
		return wars + 1;
	}

	public int play(Scanner scanner) throws InterruptedException {
		int winsA = 0;
		int winsB = 0;
		Game.promptName(1, "Enter player 2's name: ", scanner);
		char again = 'y';
		while (again == 'y' || again == 'Y') {
			System.out.println("\n----------------------------\n\nLet's play War!\n");
			int numBattles = 0;
			int numWars = 0;
			Deck deck = new Deck();
			Hand handA = new Hand(26, deck, 2);
			Hand handB = new Hand(26, deck, 2);
			Hand cardsInPlay = new Hand();
			while (handA.size() != 0 && handB.size() != 0) {
				cardsInPlay.add(handA.draw());
				cardsInPlay.add(handB.draw());
				numWars += compare(handA, handB, cardsInPlay);
				numBattles++;
				Thread.sleep(1000);
			}
			if (handA.size() == 0) {
				System.out.println("Congrats " + Game.getName(1) + ", you won!");
				winsB++;
			} else if (handB.size() == 0) {
				System.out.println("Congrats " + Game.getName(0) + ", you won!");
				winsA++;
			} else {
				System.out.println("Something went wrong. Try again!");
			}
			System.out.println("The game was completed in " + numBattles + " battles and " + numWars + " wars.");
			System.out.print("Would you like to play again? ");
			again = scanner.next().charAt(0);
		}
		if (winsA > winsB) {
			System.out.println("Congrats " + Game.getName(0) + ", you had more wins!");
		} else if (winsA < winsB) {
			System.out.println("Congrats " + Game.getName(1) + ", you had more wins!");
		} else {
			System.out.println("Both players had the same amount of wins.");
		}
		return 1;
	}
}
