package text.games;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;
import text.backend.Hand;

public class GinRummy extends Game {

	public int cost() {
		return 20;
	}

	public String rules() {
		return "In this 2 player game, players are trying to make matches with their cards. To win, a player must " +
				"have 2 unique matches of 3 different cards, and 1 separate match of 4 different cards. A " +
				"3-card match can either be a series match or a rank match, while a 4-card match can only be a" +
				" series match. A rank match is a match where all the cards have the same getRank() (e.g. the " +
				"7 of Spades, 7 of Diamonds, and 7 of Hearts are a rank match). A series match consists of 3 " +
				"or 4 cards that all have the same suit, and form a series with their rank. Aces can be a part" +
				" of a Queen-King series, or a 3-2 series (e.g. the Ace of Clubs, King, of Clubs, and Queen of" +
				" Clubs, the Jack of Hearts, 10 of Hearts, and 9 of Hearts, and the 4 of Spades, 3 of Spades, " +
				"2 of Spades, and the Ace of Spades, are all series matches, but the 2 of Hearts, Ace of " +
				"Hearts, and King of Hearts, and the 5 of Spades, 6 of Hearts, 7 of Diamonds, and 8 of Clubs, " +
				"are not).";
	}

	public String toString() {
		return "Gin Rummy (broken)";
	}

	protected int playerLimit() {
		return 2;
	}

	/**
	 * deck of cards
	 */
	private Deck stock;
	/**
	 * pile of cards already played
	 */
	private ArrayList<Card> centerPile;

	/**
	 * one player turn
	 *
	 * @param hand hand of player
	 * @param name name of player
	 */
	private void turn(Hand hand, String name) {
		if (stock.size() == 0) {
			Card temp = centerPile.get(centerPile.size() - 1);
			stock.addAll(centerPile);
			stock.shuffle();
			centerPile.add(temp);
			System.out.println("Reshuffled the center pile. It's now in the stock!");
		}
		Scanner scanner = new Scanner(System.in);
		hand.printHand();
		char pileType;
		while (true) {
			try {
				System.out.println(
						"\nWould you like to take a card from the stock or a card from the center pile (The " +
								"current top card is " + centerPile.get(centerPile.size() - 1) + ")? Enter 'stock' to" +
								" take from the stock or 'center' to take from the center pile?");
				pileType = scanner.next().toLowerCase().charAt(0);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Here, take a card from the stock.");
				pileType = 's';
			}
			if (pileType == 's' || centerPile.size() == 0) {
				while (true) {
					System.out.println("Here is your card: " + stock.get(0) + ". Would you like to take it or leave " +
							"it?");
					char take = scanner.next().toLowerCase().charAt(0);
					if (take == 't') {
						while (true) {
							try {
								System.out.println(
										"Which card would you like to drop, " + name + "? Input it's corresponding " +
												"value.");
								int index = scanner.nextInt() - 1;
								hand.add(stock.draw());
								centerPile.add(hand.get(index));
								hand.remove(index);
								break;
							} catch (InputMismatchException e) {
								System.out.println("Please only input a number.");
							} catch (IndexOutOfBoundsException e) {
								System.out.println("That number isn't valid. Try again!");
							}
						}
						hand.organizeCards();
						break;
					} else if (take == 'l') {
						System.out.println("Alright, that card has been placed in the center.");
						centerPile.add(stock.draw());
						break;
					}
					System.out.println("Please only input 'take' and 'leave'.\n");
				}
				break;
			} else if (pileType == 'c') {
				while (true) {
					try {
						System.out.println("The card last put down is the " +
								centerPile.get(centerPile.size() - 1) +
								". Which card will you substitute for it? Enter its index.");
						int index = scanner.nextInt() - 1;
						hand.add(stock.draw());
						centerPile.add(hand.get(index));
						hand.remove(index);
						break;
					} catch (InputMismatchException e) {
						System.out.println("Please only input a number.");
					} catch (IndexOutOfBoundsException e) {
						System.out.println("That number isn't valid. Try again!");
					}
				}
				hand.organizeCards();
				break;
			}
			System.out.println("Whoops, that input didn't work, try again.");
		}
		scanner.close();
	}

	/**
	 * @param hand hand to check for a win
	 * @return has the hand won
	 */
	private boolean checkForWin(Hand hand) {
		int matches = 0;
		for (Card card1 : hand) {
			for (Card card2 : hand) {
				for (Card card3 : hand) {
					for (Card card4 : hand) {
						if ((((card1.getRankValue() + 1 == card2.getRankValue()
								&& card2.getRankValue() + 1 == card3.getRankValue()
								&& card3.getRankValue() + 1 == card4.getRankValue()) ||
								(card1.getRankValue() == 14 && card2.getRankValue() == 2 && card3.getRankValue() == 3
										&& card4.getRankValue() == 4))
								&& card1.getSuit().equals(card2.getSuit()) &&
								card3.getSuit().equals(card2.getSuit()) && card3.getSuit().equals(card4.getSuit())) &&
								(!card1.isCounted() && !card2.isCounted() && !card3.isCounted()
										&& !card4.isCounted())) {
							matches += 2;
							card1.setCounted(true);
							card2.setCounted(true);
							card3.setCounted(true);
							card4.setCounted(true);
							System.out.println(
									matches + "\nHere it is: " + card1 + ", and " + card2 + ", and" +
											" " + card3 + ", and " + card4 + ".");
						}
					}
				}
			}
		}
		for (Card card1 : hand) {
			for (Card card2 : hand) {
				for (Card card3 : hand) {
					if (((card1.getRankValue() + 1 == card2.getRankValue()
							&& card2.getRankValue() + 1 == card3.getRankValue() &&
							card2.getSuit().equals(card3.getSuit()) && card2.getSuit().equals(card1.getSuit())) ||
							(card1.getRankValue() == 14 && card2.getRankValue() == 2 && card3.getRankValue() == 3))
							&& !(card1.isCounted()
									|| card2.isCounted() || card3.isCounted())) {
						matches++;
						card1.setCounted(true);
						card2.setCounted(true);
						card3.setCounted(true);
						System.out.println(
								matches + "\nHere it is: " + card1 + ", and " + card2 + ", and " + card3 + ".");
					}
				}
			}
		}
		for (int i = 1; i < hand.size() - 1; i++) {
			for (Card card1 : hand) {
				for (Card card2 : hand) {
					if ((hand.get(i).getRank().equals(card2.getRank()) && hand.get(i).getRank().equals(card1.getRank()))
							&&
							(!hand.get(i).getSuit().equals(card1.getSuit())
									&& !hand.get(i).getSuit().equals(card2.getSuit()))
							&& !(hand.get(i).isCounted() || card1.isCounted() || card2.isCounted())) {
						card1.setCounted(true);
						hand.get(i).setCounted(true);
						card2.setCounted(true);
						matches++;
						System.out.println(
								matches + "\nHere it is: " + card2 + ", and " + hand.get(i) + ", and " + card1 + ".");
					}
				}
			}
		}
		for (Card card : hand) {
			card.setCounted(false);
		}
		return matches == 4;
	}

	public int play(Scanner scanner) throws InterruptedException {
		centerPile = new ArrayList<>();
		stock = new Deck();
		stock.modifyRanks(new String[] { "Ace" }, new int[] { 1 });
		int winner = 0;
		boolean gameOver;
		Hand handA = new Hand(10, stock, 1);
		Hand handB = new Hand(10, stock, 1);
		System.out.println("\n----------------------------\n\nLet's play Gin Rummy!\n");
		Game.promptName(1, "Enter player 2's name: ", scanner);
		while (winner == 0) {
			System.out.println("\n\nNow it's " + Game.getName(0) + "'s turn. \nHere is your hand:\n");
			turn(handA, Game.getName(0));
			gameOver = checkForWin(handA);
			if (gameOver) {
				winner = 1;
				break;
			}
			System.out.println("\n\nNow it's " + Game.getName(1) + "'s turn. \nHere is your hand:\n");
			turn(handB, Game.getName(1));
			gameOver = checkForWin(handB);
			if (gameOver) {
				winner = -1;
			}
		}
		Thread.sleep(2000);
		if (winner == 1) {
			System.out.println("Congratulations " + Game.getName(0) + "! You beat " + Game.getName(1) + "!");
		} else {
			System.out.println("Congratulations " + Game.getName(1) + "! You beat " + Game.getName(0) + "!");
		}
		Thread.sleep(1000);
		System.out.println("\n" + Game.getName(0) + "'s hand:");
		handA.printHand();
		Thread.sleep(1000);
		System.out.println("\n" + Game.getName(1) + "'s hand:");
		handB.printHand();
		System.out.println("\n\nThanks for playing! Please come again soon!\n\n----------------------------\n\n");
		return 1;
	}
}
