package text.games;

import java.util.InputMismatchException;
import java.util.Scanner;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;
import text.backend.Hand;

public class BlackJack2 extends Game {

	public int cost() {
		return 10;
	}

	public String rules() {
		return "The goal of this game is to get the total sum of your cards greater than the Dealer's, while not " +
				"going over 21. Aces are worth 11 points in this version, but change their value to 1 point if" +
				" the sum of your cards is greater than 21. Number cards are worth their rank value, and " +
				"Jacks, Queens, and Kings are worth 10 points. If you 'bust', or go over 21 points, the Dealer" +
				" wins one point, and vice versa. If neither goes over 21, whoever gets the larger amount of " +
				"points in their hand wins, or both win a point if it's a tie.\nThis version also introduces " +
				"betting. At the beginning of the round, you can bet any amount of your current coins. If you " +
				"win, you get double the amount you bet back, or 2.5 times as much if your hand is worth 21 " +
				"points at the end. If you lose, you don't get anything back.";
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
		return "BlackJack (version 2)";
	}

	protected int playerLimit() {
		return 1;
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
			System.out.println(Game.getName(0) + ", you win by " + (pointA - pointB) + " point" + suffix + "! " +
					"Congratulations!");
			outcome = 2;
		} else {
			System.out.println("It's a tie!");
			outcome = 1;
		}
		pointA = 0;
		pointB = 0;
		Thread.sleep(2000);
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
		char again = 'y';
		int bet = 0;
		Scanner scanner = new Scanner(System.in);
		while (again == 'y' || again == 'Y') {
			System.out.println("\n----------------------------\n\nLet's play Blackjack!\n");
			if (bet != 0) {
				String suffix = bet == 1 ? "" : "s";
				System.out.println("There are " + bet + "coin" + suffix + " in the 'bet' pile.");
			}
			int valueA;
			int valueB = 0;
			while (true) {
				try {
					Scanner nextScanner = new Scanner(System.in);
					String suffix = getCoins() == 1 ? "" : "s";
					System.out.print("How many coins do you want to bet? You have " + getCoins() + " coin" + suffix +
							" to bet with. ");
					int newBet = nextScanner.nextInt();
					if (newBet > getCoins()) {
						System.out.println("You don't have that many coins!");
						continue;
					}
					bet += newBet;
					nextScanner.close();
					break;
				} catch (InputMismatchException e) {
					System.out.println("Please only input a number!");
				}
			}
			Deck deck = new Deck();
			deck.modifyRanks(new String[] { "Ace", "King", "Queen", "Jack" }, new int[] { 11, 10, 10, 10 });
			Hand handA = new Hand(2, deck, 2);
			Hand handB = new Hand(2, deck, 2);
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
					if (valueA > 21) {
						for (Card card : handA) {
							if (card.getRank().equals("Ace")) {
								valueA -= 10;
							}
						}
					}
					System.out.println("Your hand's value: " + valueA);
				}
				if (choice.equals("stay")) {
					break;
				}
				if (!choice.equals("hit")) {
					System.out.println("Please only enter 'hit' or 'stay'.");
				}
			}
			if (choice.equals("hit")) {
				System.out.println("You're Busted.");
				pointB++;
				Thread.sleep(2000);
			} else {
				System.out.println("\nDealer's turn.");
				while (valueB < 17) {
					Thread.sleep(3000);
					System.out.println("\nDealer's hand: \n");
					Thread.sleep(2000);
					handB.printHand();
					valueB = handValue(handB);
					if (valueB > 21) {
						for (Card card : handB) {
							if (card.getRank().equals("Ace")) {
								valueB -= 10;
							}
						}
					}
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
				if (valueB > 21) {
					System.out.println("Dealer lost.");
					pointA++;
					Thread.sleep(2000);
				} else if (valueB > valueA) {
					Thread.sleep(1000);
					System.out.println("Dealer wins! Better luck next time!");
					pointB++;
					Thread.sleep(2000);
				} else if (valueB < valueA) {
					Thread.sleep(1000);
					System.out.println("You win, " + Game.getName(0) + "! Congratulations!");
					pointA += 1;
					Thread.sleep(2000);
				} else {
					Thread.sleep(1000);
					System.out.println("It's a tie! You both get a point.");
					pointA++;
					pointB++;
					Thread.sleep(2000);
				}
			}
			int receive;
			if (valueA == 21 && valueB != 21) {
				addCoins((int) (bet * 1.5));
				receive = (int) (bet * 2.5);
				bet = 0;
			} else if (valueA > 21 || (valueA < valueB && valueB <= 21)) {
				subtractCoins(bet);
				receive = 0;
				bet = 0;
			} else if ((valueA < 21 && valueA > valueB) || valueB > 21) {
				addCoins(bet);
				receive = bet * 2;
				bet = 0;
			} else {
				subtractCoins(bet);
				receive = 0;
			}
			String suffix1 = receive == 1 ? "" : "s", suffix2 = getCoins() == 1 ? "" : "s";
			System.out.println("You receive " + receive + " coin" + suffix1 + "! Now you have " + getCoins() + " coin" +
					suffix2 + "!");
			System.out.println("\nThe current score is You: " + pointA + ", and Dealer: " + pointB +
					".");
			do {
				Thread.sleep(2000);
				if (again != 'y') {
					System.out.print("You didn't type yes or no. Please choose one: ");
				} else {
					System.out.print("Would you like to play again, " + Game.getName(0) + "? ");
				}
				again = scanner.next().charAt(0);
			} while (again != 'y' && again != 'n' && again != 'Y' && again != 'N');
		}
		System.out.println("Okay, hope to see you again soon!");
		scanner.close();
		return scoreCheck();
	}
}
