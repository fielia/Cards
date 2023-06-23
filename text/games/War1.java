package text.games;

import java.util.Scanner;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;

public class War1 extends Game {
	
	public int cost() {
		return 5;
	}
	
	public String rules() {
		return "This game is automatic. It compares the ranks of the top card in each player's deck, and gives a " +
				       "point to the player that has the card with the higher rank, or a point to both players if the" +
				       " cards tie in rank.";
	}
	
	public String toString() {
		return "War (version 1)";
	}
	
	@Override
	protected int playerLimit() {
		return 0;
	}
	
	public int play() throws InterruptedException {
		int pointA = 0;
		int pointB = 0;
		Scanner scanner = new Scanner(System.in);
		Game.promptName(1, "Enter player 2's name: ");
		Card cardA;
		Card cardB;
		char again = 'y';
		while (again == 'y' || again == 'Y') {
			System.out.println("\n----------------------------\n\nLet's play War!\n");
			Deck deck = new Deck();
			for (int i = 0; i < 26; i++) {
				cardA = deck.draw();
				cardB = deck.draw();
				if (cardA.getRankValue() > cardB.getRankValue()) {
					pointA++;
					System.out.println("Point for " + Game.getName(0) + "!");
				} else if (cardA.getRankValue() < cardB.getRankValue()) {
					pointB++;
					System.out.println("Point for " + Game.getName(1) + "!");
				} else {
					pointA++;
					pointB++;
					System.out.println("Tie!");
				}
				System.out.println(Game.getName(0) + "'s Card: " + cardA.getRank() + "\n" +
						                   Game.getName(1) + "'s Card: " + cardB.getRank() + "\n");
				Thread.sleep(1000);
			}
			if (pointA > pointB) {
				System.out.println("Congrats " + Game.getName(0) + ", you won!");
			} else if (pointA < pointB) {
				System.out.println("Congrats " + Game.getName(1) + ", you won!");
			} else {
				System.out.println("It was a tie!");
			}
			System.out.println(Game.getName(0) + "'s Score: " + pointA + "\n" + Game.getName(1) + "'s Score: " + pointB);
			System.out.print("Would you like to play again? ");
			again = scanner.next().charAt(0);
		}
		scanner.close();
		return 1;
	}
}
