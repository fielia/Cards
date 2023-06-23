package gui.games;

import java.awt.LayoutManager;
import java.util.Scanner;

import gui.backend.BaseGame;
import gui.backend.Card;
import gui.backend.Deck;
import gui.backend.Hand;

public class BlackJack extends BaseGame {
	
	public int cost() {
		return 10;
	}
	
	public String rules() {
		return "The goal of this BaseGame is to get the total sum of your cards to be greater than the Dealer's, while " +
				       "not going over 21. Aces are worth 1 point in this version, number cards are worth their rank " +
				       "value, and Jacks, Queens, and Kings are worth 10 points. If you 'bust', or go over 21 points," +
				       " the Dealer wins one point, and vice versa. If neither goes over 21, whoever gets the larger " +
				       "amount of points in their hand wins, or both win a point if it's a tie.";
	}
	
	/**
	 * point value for the player
	 */
	private int pointA = 0;
	/**
	 * point value for the computer
	 */
	private int pointB = 0;
	
	public String toString() {
		return "BlackJack (version 1)";
	}
	
	protected int playerLimit() {
		return 1;
	}

	public BlackJack(String title, int width, int height, LayoutManager layout) {
		super(title, width, height, layout);
	}
	
	/**
	 * checks how many overall points the player and computer have
	 *
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private int scoreCheck() throws InterruptedException {
		String suffix = "";
		int outcome;
		if (Math.abs(pointA - pointB) != 1) {
			suffix = "s";
		}
		Thread.sleep(1000);
		if (pointB > pointA) {
			System.out.println("Dealer wins by " + (pointB - pointA) + " point" + suffix + "! " +
					                   "Maybe next time you'll win!");
			outcome = 0;
		} else if (pointA > pointB) {
			System.out.println(BaseGame.getName(0) + ", you win by " + (pointA - pointB) + " point" + suffix + "! " +
					                   "Congratulations!");
			outcome = 2;
		} else {
			System.out.println("It's a tie!");
			outcome = 1;
		}
		Thread.sleep(2000);
		pointA = 0;
		pointB = 0;
		return outcome;
	}
	
	/**
	 * @param hand list of cards to calculate value from
	 * @return value of hand parameter
	 */
	private int handValue(Hand hand) {
		int value = 0;
		for (Card card : hand) {
			value += card.getRankValue();
		}
		return value;
	}
	
	public int play() throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		char again = 'y';
		while (again == 'y') {
			System.out.println("\n----------------------------\n\nLet's play Blackjack!\n");
			Deck deck = new Deck();
			deck.modifyRanks(new String[] {"Ace", "King", "Queen", "Jack"}, new int[] {1, 10, 10, 10});
			Hand handA = new Hand(2, deck, 2);
			Hand handB = new Hand(2, deck, 2);
			int valueA;
			int valueB;
			scanner = new Scanner(System.in);
			System.out.println("Your hand:");
			handA.printHand();
			valueA = handValue(handA);
			System.out.println("Your hand's value: " + valueA);
			Thread.sleep(1000);
			System.out.println("\nDealer's card: " + handB.get(0));
			String choice = "";
			while (valueA <= 21) {
				System.out.print("Do you want to hit or stay? ");
				choice = scanner.nextLine().toLowerCase();
				if (choice.equals("hit")) {
					handA.add(deck.draw());
					System.out.println("\nYour hand:");
					handA.printHand();
					valueA = handValue(handA);
					System.out.println("Your hand's value: " + valueA);
				} else if (choice.equals("stay")) {
					break;
				}
				if (!choice.equals("hit")) {
					System.out.println("Please only enter 'hit' or 'stay'.");
				}
			}
			if (choice.equals("hit")) {
				System.out.println("You're Busted");
				pointB++;
				Thread.sleep(2000);
			} else {
				System.out.println("\nDealer's turn.");
				valueB = 0;
				while (valueB < 17) {
					Thread.sleep(3000);
					System.out.println("\nDealer's hand:");
					Thread.sleep(2000);
					handB.printHand();
					valueB = handValue(handB);
					Thread.sleep(2000);
					System.out.println("Dealer's hand value: " + valueB);
					handB.add(deck.draw());
					Thread.sleep(3000);
					if (valueB < 17) {
						System.out.println("The Dealer decides to hit.");
					} else if (valueB <= 20) {
						System.out.println("The Dealer decides to stay.");
					}
				}
				if (valueB > 20) {
					System.out.println("Dealer busted.");
					pointA++;
					Thread.sleep(2000);
				} else if (valueB > valueA) {
					Thread.sleep(1000);
					System.out.println("Dealer wins! Better luck next time!");
					pointB++;
					Thread.sleep(2000);
				} else if (valueB < valueA) {
					Thread.sleep(1000);
					System.out.println("You win, " + BaseGame.getName(0) + "! Congratulations!");
					pointA++;
					Thread.sleep(2000);
				} else {
					Thread.sleep(1000);
					System.out.println("It's a tie! You both get a 'win point'.");
					pointA++;
					pointB++;
					Thread.sleep(2000);
				}
			}
			System.out.println("\nThe current score is You: " + pointA + ", and Dealer: " + pointB + ".");
			do {
				if (again != 'y') {
					System.out.println("You did not type 'y' or 'n'. Please choose one.");
				}
				Thread.sleep(2000);
				System.out.println("Would you like to play again, " + BaseGame.getName(0) + "?");
				again = scanner.next().toLowerCase().charAt(0);
			}
			while (again != 'n' && again != 'y');
		}
		System.out.println("OK. Hope to see you soon!");
		scanner.close();
		return scoreCheck();
	}
}
