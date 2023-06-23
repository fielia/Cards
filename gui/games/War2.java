package gui.games;

import java.util.ArrayList;
import java.util.Scanner;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;

public class War2 extends Game {
	
	public int cost() {
		return 10;
	}
	
	public String rules() {
		return "This is an interactive version of War. The player has to choose a card to place down, which will be " +
				       "compared with the card the computer chooses. The player with the higher ranked card gets the " +
				       "point, or both players get a point if they tie.";
	}
	
	public String toString() {
		return "War (version 2)";
	}
	
	@Override
	protected int playerLimit() {
		return 2;
	}
	
	/**
	 * the amount of games the player has won
	 */
	private int gamesA;
	/**
	 * the amount of games the computer has won
	 */
	private int gamesB;
	
	/**
	 * @return the player that had more victories
	 */
	private int overallWinner() {
		if (gamesA > gamesB) {
			return 2;
		} else if (gamesA < gamesB) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public int play() throws InterruptedException {
		char cont = 'y';
		Deck deck = new Deck();
		Scanner scanner = new Scanner(System.in);
		while (cont == 'y' || cont == 'Y') {
			System.out.println("\n----------------------------\n\nLet's play War!\n");
			int pointA = 0;
			int pointB = 0;
			int handNum;
			ArrayList<Card> handA = new ArrayList<>();
			ArrayList<Card> handB = new ArrayList<>();
			for (int i = 0; i < 52; i++) {
				if (i % 2 == 0) {
					handA.add(deck.draw());
				} else {
					handB.add(deck.draw());
				}
			}
			while (handA.size() > 0) {
				for (int i = 0; i < handA.size(); i++) {
					System.out.println((i + 1) + ": " + handA.get(i));
					Game.sleep(100);
				}
				Game.sleep(2000);
				while (true) {
					try {
						System.out.println("Which card do you want to play? Enter its number. Be sure it's the card " +
								                   "you want.");
						handNum = scanner.nextInt() - 1;
						break;
					} catch (IndexOutOfBoundsException e) {
						System.out.println("That card number is not in your hand. Please try again.");
						Game.sleep(3000);
					}
				}
				Card cardA = handA.get(handNum);
				handA.remove(handNum);
				Card cardB = handB.get(handNum);
				handB.remove(handNum);
				if (cardA.getRankValue() > cardB.getRankValue()) {
					pointA++;
					System.out.println("\nPoint for " + Game.getName(0) + "!");
				} else if (cardA.getRankValue() < cardB.getRankValue()) {
					pointB++;
					System.out.println("\nPoint for Computer!");
				} else {
					pointA++;
					pointB++;
					System.out.println("\nTie!");
				}
				System.out.println(
						Game.getName(0) + "'s Card: " + cardA.getRank() + "\nComputer's Card: " +
								cardB.getRank() + "\n");
				Game.sleep(1000);
			}
			if (pointA > pointB) {
				System.out.println("Congrats " + Game.getName(0) + ", you won!");
				gamesA++;
			} else if (pointA < pointB) {
				System.out.println("Whoops, the Computer won!");
				gamesB++;
			} else {
				System.out.println("It was a tie!");
			}
			System.out.println(Game.getName(0) + "'s Score: " + pointA + "\nComputer's Card: " + pointB);
			System.out.print("Would you like to play again? ");
			cont = scanner.next().charAt(0);
		}
		scanner.close();
		return overallWinner();
	}
}
