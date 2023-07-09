package gui.games;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import text.backend.Deck;
import text.backend.Game;

public class GoFish extends Game {

	public int cost() {
		return 15;
	}

	public String rules() {
		return "In this classic card game, players ask opponents for cards that they have in their hands. If the " +
				"opponent has the card, they fork it over and the player gains a \"match\". If they don't, the" +
				" player has to pick up a card from the stock. Once the entire stock is emptied, the player " +
				"with the most matches wins!";
	}

	@Override
	protected int playerLimit() {
		return 1;
	}

	/**
	 * the amount of games the player has won
	 */
	private int gamesA;
	/**
	 * the amount of games the computer has won
	 */
	private int gamesB;

	public String toString() {
		return "Go Fish (broken)";
	}

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

	/**
	 * checks how many overall points comp and player have
	 *
	 * @param pointA score of player
	 * @param pointB score of computer
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private void scoreCheck(int pointA, int pointB) throws InterruptedException {
		String suffix = "";
		if (Math.abs(pointA - pointB) != 1) {
			suffix = "s";
		}
		Thread.sleep(1000);
		System.out.println("\nYou got " + pointA + " matches, " + Game.getName(0) + ".\nThe Computer got " +
				pointB + " matches.");
		if (pointB > pointA) {
			System.out.println("The Computer wins by " + (pointB - pointA) + " point" + suffix + "! Maybe next time " +
					"you'll win!");
			gamesB++;
		} else if (pointA > pointB) {
			System.out.println("You win by " + (pointA - pointB) + " point" + suffix + "! Congratulations!");
			gamesA++;
		} else {
			System.out.println("It's a tie!");
		}
		Thread.sleep(2000);
	}

	/**
	 * unique printHand() method for this class
	 *
	 * @param hand hand to print
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private void printHand(ArrayList<String> hand) throws InterruptedException {
		int width = hand.size() > 9 ? 2 : 1;
		for (int i = 0; i < hand.size(); i++) {
			System.out.printf("%" + width + "d: " + hand.get(i) + "\n", i + 1);
			Game.sleep(500);
		}
	}

	/**
	 * adds points if two cards in hand
	 *
	 * @param hand  hand to loop through
	 * @param point point to add to
	 * @return changed point parameter
	 */
	private int checkCards(ArrayList<String> hand, int point) {
		ArrayList<String> cards = new ArrayList<>();
		for (String card : hand) {
			int count = 0;
			for (String inHand : hand) {
				if (inHand.equals(card)) {
					count++;
				}
			}
			if (count >= 2) {
				cards.add(card);
				point++;
			}
		}
		for (String card : cards) {
			hand.remove(card);
			hand.remove(card);
		}
		return point;
	}

	public int play() throws InterruptedException {
		gamesA = 0;
		gamesB = 0;
		char again = 'y';
		System.out.println("\n----------------------------\n\nLet's play Go Fish!\n");
		Scanner scanner = new Scanner(System.in);
		while (again == 'y' || again == 'Y') {
			int pointA = 0;
			int pointB = 0;
			ArrayList<String> handA = new ArrayList<>();
			ArrayList<String> handB = new ArrayList<>();
			ArrayList<String> stock = new ArrayList<>();
			Thread.sleep(1000);
			Deck deck = new Deck();
			for (int i = 0; i < 8; i++) {
				handA.add(deck.draw().getRank());
				handB.add(deck.draw().getRank());
			}
			while (deck.size() > 0) {
				stock.add(deck.draw().getRank());
			}
			boolean fish;
			pointA = checkCards(handA, pointA);
			pointB = checkCards(handB, pointB);
			boolean win = false;
			while (!win) {
				if (handA.size() == 0) {
					handA.add(stock.get(0));
					stock.remove(0);
				}
				if (handB.size() == 0) {
					handB.add(stock.get(0));
					stock.remove(0);
				}
				if (stock.size() <= 0) {
					win = true;
					continue;
				}
				boolean turn = true;
				while (turn) {
					System.out.println("\nYour hand: ");
					printHand(handA);
					Thread.sleep(2000);
					int index;
					while (true) {
						try {
							System.out.println(
									"\nWhich card in your hand do you want to ask for, " + Game.getName(0) + "? " +
											"Please enter the index: ");
							index = scanner.nextInt();
							if (index < 0 || index > handA.size()) {
								System.out.println("That index is not valid. Please try again.");
								continue;
							}
							break;
						} catch (InputMismatchException e) {
							System.out.println("That input is not a number. Try again.");
						}
					}
					fish = true;
					for (String inHandB : handB) {
						if (inHandB.equals(handA.get(index))) {
							handA.add(inHandB);
							handB.remove(inHandB);
							fish = false;
							System.out.println("The Computer had that card! Nice!");
							break;
						}
					}
					if (fish) {
						handA.add(stock.get(0));
						System.out.println("The Computer responded with 'Go Fish'.");
						Thread.sleep(1000);
						System.out.println("You picked up the '" + stock.get(0) + "'.");
						stock.remove(0);
						turn = false;
					}
					if (handA.size() == 0) {
						handA.add(stock.get(0));
					}
					stock.remove(0);
					if (handB.size() == 0) {
						handB.add(stock.get(0));
					}
					stock.remove(0);
					if (stock.size() == 0) {
						win = true;
						continue;
					}
					pointA = checkCards(handA, pointA);
					Thread.sleep(2000);
				}
				pointA = checkCards(handA, pointA);
				turn = true;
				while (turn) {
					if (handA.size() == 0) {
						handA.add(stock.get(0));
					}
					stock.remove(0);
					if (stock.size() == 0) {
						win = true;
						continue;
					}
					if (handB.size() == 0) {
						handB.add(stock.get(0));
					}
					stock.remove(0);
					if (stock.size() == 0) {
						win = true;
						continue;
					}
					Thread.sleep(2000);
					String compGuess = handB.get((int) (Math.random() * handB.size()));
					System.out.println("\nThe Computer is asking if you have any " + compGuess + "s.");
					fish = true;
					for (String card : handA) {
						if (card.equals(compGuess)) {
							handB.add(card);
							handA.remove(card);
							fish = false;
							System.out.println("You reluctantly hand over the card.");
							break;
						}
					}
					if (fish) {
						handB.add(stock.get(0));
						stock.remove(0);
						System.out.println("You happily exclaim 'Go Fish'!");
						turn = false;
					}
					Thread.sleep(2000);
					pointB = checkCards(handB, pointB);
					if (stock.size() == 0) {
						win = true;
					}
				}
			}
			scoreCheck(pointA, pointB);
			System.out.println("\nWould you like to play again, " + Game.getName(0) + "?");
			again = scanner.next().charAt(0);
		}
		scanner.close();
		return overallWinner();
	}
}
