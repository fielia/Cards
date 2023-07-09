package gui.games;

import java.util.ArrayList;
import java.util.Scanner;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;
import text.backend.Hand;

public class GOPS extends Game {

	public int cost() {
		return 15;
	}

	public String rules() {
		return "In this card game, two players, one human and one computer, have to 'bid' for cards, and get points " +
				"based on the value of the cards (2-10 have point values equal to their rank, Jacks are worth " +
				"11 points, Queens are worth 12 points, and Kings are worth 13 points). You're given all the " +
				"Spades, and the Computer is given the Clubs. There are 2 settings that slightly change the " +
				"game: you can make the Aces have the highest point value (14) or the lowest (1), and you can " +
				"add Hearts to the bidding pile. Any Hearts cards would be worth double their usual point " +
				"values.\nWhen bidding for cards, the player that bids the higher rank wins the entire bidding" +
				" pile. If there's a tie, the pile is added to with the next bidding cards, and you bid on all" +
				" of them the next round.";
	}

	public String toString() {
		return "Game of Pure Strategy";
	}

	@Override
	protected int playerLimit() {
		return 1;
	}

	/**
	 * calculates total points
	 *
	 * @param point object with cards to tally
	 * @return total point value of parameter
	 */
	private int calcPoints(Hand point) {
		int pointTotal = 0;
		for (Card card : point) {
			if (card.getSuit().equals("Diamonds")) {
				pointTotal += card.getRankValue();
			} else if (card.getSuit().equals("Hearts")) {
				pointTotal += card.getRankValue() * 2;
			}
		}
		return pointTotal;
	}

	/**
	 * calculates a winner based off bid cards
	 *
	 * @param cardA   player's bid card
	 * @param cardB   comp's bid card
	 * @param bidSize if the cards in the bid is equal to 1 (only used for grammar)
	 * @return 0 if no winner, 1 if player wins, -1 if comp wins
	 */
	private int compareCards(Card cardA, Card cardB, boolean bidSize) {
		boolean isPlayersTurn = cardA.getRankValue() > cardB.getRankValue();
		System.out.println("The computer bet the " + cardB + ".");
		String suffix = bidSize ? "s" : "";
		if (cardA.getRankValue() == cardB.getRankValue()) {
			System.out.println("It's a tie!");
			return 0;
		} else if (isPlayersTurn) {
			System.out.println("\nYay! You got the card" + suffix + "!\n");
			return 1;
		} else {
			System.out.println("\nOops! The computer got the card" + suffix + ".\n");
			return -1;
		}
	}

	public int play() throws InterruptedException {
		Hand handA = new Hand();
		Hand pointA = new Hand();
		Hand handB = new Hand();
		Hand pointB = new Hand();
		Deck stock = new Deck();
		stock.clear();
		ArrayList<Card> bidCards = new ArrayList<>();
		int maxPoints = 0;
		Scanner scanner = new Scanner(System.in);
		System.out.println("""

				----------------------------

				Let's play the Game of Pure Strategy!
				You will play against the computer. Good Luck!

				Do you want the Ace to be highest value or the lowest? Input 'y' for highest or 'n' for lowest.""");
		boolean lower = (scanner.next().toLowerCase().charAt(0) == 'n');
		System.out.println("\nWould you like to play with 2 suits for bidding or just 1? Input 'y' for 2 suits and " +
				"'n' for 1.");
		boolean multipleSuits = (scanner.next().toLowerCase().charAt(0) == 'y');
		if (multipleSuits) {
			System.out.println("Hearts are worth two times the initial points.");
		}
		Deck deck = new Deck();
		if (lower) {
			deck.modifyRanks(new String[] { "Ace" }, new int[] { 1 });
		}
		for (Card card : deck) {
			if (card.getSuit().equals("Spades")) {
				handA.add(card);
			} else if (card.getSuit().equals("Clubs")) {
				handB.add(card);
			} else if (card.getSuit().equals("Diamonds")) {
				maxPoints += card.getRankValue();
				stock.add(card);
			} else if (card.getSuit().equals("Hearts") && multipleSuits) {
				maxPoints += card.getRankValue() * 2;
				stock.add(card);
			}
		}
		stock.shuffle();
		System.out.println("\nThe maximum points are " + maxPoints + " points.\n");
		handB.organizeCards();
		while (stock.size() > 0) {
			System.out.println("Your hand:");
			handA.printHand2();
			String suffix = multipleSuits ? "s" : "";
			if (multipleSuits) {
				bidCards.add(stock.draw());
			}
			bidCards.add(stock.draw());
			System.out.print("\nThe card" + suffix + " up for grabs are");
			for (int i = 0; i < bidCards.size(); i++) {
				if (i == bidCards.size() - 1 && i != 0) {
					System.out.print(" and");
				}
				System.out.print(" the " + bidCards.get(i));
				if (bidCards.size() > 2 && i != bidCards.size() - 1) {
					System.out.print(",");
				}
			}
			System.out.print(". ");
			Card cardA;
			while (true) {
				try {
					System.out.println("The computer has selected their card.\nWhich card will you use to bid? " +
							"Input the value listed.");
					cardA = handA.get(scanner.nextInt() - 1);
					break;
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Oh no! That number isn't a valid input. Please try again.");
					scanner.nextLine();
				}
			}
			Card cardB = handB.get((int) (Math.random() * handB.size()));
			int winner = compareCards(cardA, cardB, bidCards.size() == 1);
			if (winner == 1) {
				pointA.addAll(bidCards);
				bidCards.clear();
			} else if (winner == -1) {
				pointB.addAll(bidCards);
				bidCards.clear();
			}
			handA.remove(cardA);
			handB.remove(cardB);
			Game.sleep(5000);
		}
		int pointValueA = calcPoints(pointA);
		int pointValueB = calcPoints(pointB);
		int outcome;
		if (pointValueA > pointValueB) {
			System.out.println("Congratulations, " + Game.getName(0) + "! You beat the computer " + pointValueA + " " +
					"to " + pointValueB + "!\nNice work!");
			outcome = 2;
		} else if (pointValueA < pointValueB) {
			System.out.println("Oh no, " + Game.getName(0) + "! The computer beat you " + pointValueB + " to " +
					pointValueA + "!\nTry better next time!");
			outcome = 0;
		} else {
			System.out.println("Wow, " + Game.getName(0) + "! You and the computer tied, both with " + pointValueA +
					" points!");
			outcome = 1;
		}
		Game.sleep(5000);
		scanner.close();
		return outcome;
	}
}
