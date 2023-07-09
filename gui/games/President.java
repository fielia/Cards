package gui.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;
import text.backend.Hand;

public class President extends Game {

	public int cost() {
		return 30;
	}

	public String rules() {
		return "This game has anywhere from 3 to 7 players, and players are dealt the entire deck. In this game, Aces" +
				" are highest in rank, though 2s are ranked higher. The first player plays any number of any " +
				"card rank of their choice, and every other player has to match or beat the rank and the " +
				"number of cards played, UNLESS they play a two or play all the rest of the rank currently on " +
				"top, making the amount of that rank in the pile reach 4, regardless of how many cards you " +
				"place to do that. Both of these plays result in the pile being 'cleared', and the center " +
				"starting fresh with the player that cleared it. If you can't play any cards, you will pass, " +
				"and if everyone passes, the player that last played plays again!";
	}

	/**
	 * list of player's hands
	 */
	private final ArrayList<Hand> hands = new ArrayList<>();
	/**
	 * list of if players have passed their turns
	 */
	private final ArrayList<Boolean> hasPassed = new ArrayList<>();
	/**
	 * current amount of same-ranked cards in the center pile
	 */
	private int currentAmount = 0;
	/**
	 * current rank of top card(s) in the center pile
	 */
	private int highestRank = 0;
	/**
	 * Scanner variable
	 */
	private Scanner scanner;

	/**
	 * Creates first player
	 */
	public President() {
		hands.add(new Hand());
		hasPassed.add(false);
	}

	public String toString() {
		return "President";
	}

	protected int playerLimit() {
		return 7;
	}

	/**
	 * checks if player only has 2s in their hand
	 *
	 * @param hand hand to look through
	 * @return the player only has 2s
	 */
	private boolean only2sLeft(Hand hand) {
		for (Card card : hand) {
			if (card.getRankValue() != 15) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return if all players have passed their turn
	 */
	private boolean checkAllTrue() {
		for (boolean bool : hasPassed) {
			if (!bool) {
				return false;
			}
		}
		return true;
	}

	/**
	 * sets all values in hasPassed to false
	 */
	private void setFalse() {
		Collections.fill(hasPassed, false);
	}

	/**
	 * prints placement of all players and their point values
	 *
	 * @param points array of all players' points
	 */
	private void printScores(int[] points) throws InterruptedException {
		boolean[] used = new boolean[points.length];
		boolean moveOn = true;
		int place = 1;
		while (moveOn) {
			ArrayList<Integer> next = new ArrayList<>();
			int largest = -1;
			for (int i = 0; i < points.length; i++) {
				if ((largest < points[i] || largest == -1) && !used[i]) {
					largest = points[i];
					next.clear();
					next.add(i);
				} else if (largest == points[i] && !used[i]) {
					next.add(i);
				}
			}
			String suffix, suffix2 = largest == 1 ? "" : "s";
			if (place % 10 == 1 && place / 10 != 1) {
				suffix = "st";
			} else if (place % 10 == 2 && place / 10 != 1) {
				suffix = "nd";
			} else if (place % 10 == 3 && place / 10 != 1) {
				suffix = "rd";
			} else {
				suffix = "th";
			}
			Thread.sleep(1000);
			for (int i = 0; i < next.size(); i++) {
				System.out.print(Game.getName(next.get(i)));
				used[next.get(i)] = true;
				if (i == next.size() - 2 && i != 0) {
					System.out.print(", and ");
				} else if (i == next.size() - 2) {
					System.out.print(" and ");
				} else if (i != next.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.print(" got " + place + suffix + " place with " + largest + " point" + suffix2);
			if (next.size() > 1) {
				System.out.print(" each");
			}
			System.out.println("!");
			moveOn = false;
			place += next.size();
			for (boolean bool : used) {
				if (!bool) {
					moveOn = true;
					break;
				}
			}
		}
		Thread.sleep(1000);
	}

	/**
	 * unique printHand() method
	 *
	 * @param hand     hand to print out
	 * @param organize if we want to organize the cards
	 */
	private void printHand(Hand hand, boolean organize) {
		if (organize) {
			hand.organizeCardsInverse();
		}
		int width = hand.size() > 9 ? 2 : 1;
		for (int i = 0; i < hand.size(); i++) {
			hand.get(i).setCounted(false);
			System.out.printf("%" + width + "d: " + hand.get(i) + "\n", i + 1);
		}
	}

	/**
	 * checks if the player can play anything
	 *
	 * @param hand player's hand
	 * @return if the player can play anything
	 */
	private boolean canPlay(Hand hand) {
		for (int i = 0; i < hand.size(); i++) {
			int amt = 0;
			for (Card card : hand) {
				if (hand.get(i).getRankValue() == card.getRankValue()) {
					amt++;
				}
			}
			if ((amt >= currentAmount && hand.get(i).getRankValue() >= highestRank)
					|| (hand.get(i).getRankValue() == highestRank
							&& amt + currentAmount == 4)
					|| (hand.get(i).getRankValue() == 15)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * prints the top cards of all hands
	 *
	 * @param hands ArrayList of hands to print
	 */
	private void printChoices(ArrayList<Hand> hands) {
		for (int i = 0; i < hands.size(); i++) {
			System.out.println("Hand #" + (i + 1) + ": " + hands.get(i).get(0));
		}
	}

	/**
	 * initializes a round by filling hands
	 *
	 * @param numPeople number of people playing
	 * @param hands     list of hands for the people playing
	 */
	private void initRound(int numPeople, ArrayList<Hand> hands) {
		int loopCount = 0;
		Deck deck = new Deck();
		deck.modifyRanks(new String[] { "2" }, new int[] { 15 });
		while (true) {
			loopCount++;
			try {
				for (int i = 0; i < numPeople; i++) {
					int index = (int) (Math.random() * numPeople);
					while (hands.get(index).size() == loopCount) {
						index = (int) (Math.random() * numPeople);
					}
					hands.get(index).add(deck.draw((int) (Math.random() * deck.size())));
				}
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
	}

	/**
	 * plays a full round
	 *
	 * @param numPeople number of people playing
	 * @param hierarchy playing order
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private void round(int numPeople, int[] hierarchy) throws InterruptedException {
		int first = 0, nextHierarchy = 0, lastHierarchy = numPeople, lastPlay = -1;
		boolean firstTime = true;
		for (Hand hand : hands) {
			for (Card card : hand) {
				if (card.getRank().equals("3") && card.getSuit().equals("Spades")) {
					first = hands.indexOf(hand);
				}
			}
		}
		do {
			for (int i = 0; i < numPeople; i++) {
				Game.sleep(1500);
				if (i == numPeople - 1) {
					firstTime = false;
				}
				if (i < first && firstTime) {
					continue;
				}
				if (hands.get(i).size() == 0 || only2sLeft(hands.get(i))) {
					hasPassed.set(i, true);
					continue;
				}
				System.out.println("\nIt's your turn, " + Game.getName(i) + "!\n\nHere's your hand:");
				printHand(hands.get(i), true);
				hasPassed.set(i, turn(i));
				if (hands.get(i).size() == 0) {
					hierarchy[nextHierarchy] = i;
					nextHierarchy++;
					hasPassed.set(i, true);
					System.out.println("You have no cards left! Nice work!\n");
					Thread.sleep(2000);
				} else if (only2sLeft(hands.get(i))) {
					lastHierarchy--;
					hierarchy[lastHierarchy] = i;
					hasPassed.set(i, true);
					System.out.println("You only had 2s left! Too bad!\n");
					Thread.sleep(2000);
				}
				if (nextHierarchy == lastHierarchy) {
					break;
				}
				if (!hasPassed.get(i)) {
					lastPlay = i;
				}
				if (checkAllTrue() || (currentAmount == 0 && highestRank == 0)) {
					System.out.println("\nYou play again, " + Game.getName(lastPlay) + "!");
					currentAmount = 0;
					highestRank = 0;
					i = lastPlay - 1;
					setFalse();
				}
			}
		} while (nextHierarchy != lastHierarchy);
	}

	/**
	 * single player turn
	 *
	 * @param i player number (used to get player's hand and name)
	 * @return if the player has played (false) or passed (true);
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private boolean turn(int i) throws InterruptedException {
		String name = Game.getName(i);
		Hand hand = hands.get(i);
		if (!canPlay(hand)) {
			System.out.println("You can't play! Your turn is over.");
			return true;
		}
		int index, total = 1;
		while (true) {
			try {
				String highestRankName = switch (highestRank) {
					case (11) -> "Jack";
					case (12) -> "Queen";
					case (13) -> "King";
					case (14) -> "Ace";
					case (15) -> "2";
					default -> String.valueOf(highestRank);
				};
				scanner = new Scanner(System.in);
				if (currentAmount != 0 && highestRank != 0) {
					if (currentAmount == 1) {
						System.out.println("There is a '" + highestRankName + "' in the center.\n");
					} else {
						System.out.println(
								"There are " + currentAmount + " '" + highestRankName + "'s in the center.\n");
					}
				} else {
					System.out.println("You're the first to play! You can play anything you want!\n");
				}
				System.out.print("Which card do you want to play? Enter the number here, or enter 0 to pass your " +
						"turn: ");
				index = scanner.nextInt() - 1;
				if (index == -1) {
					System.out.println("You skipped your turn!");
					Game.sleep(2000);
					return true;
				}
				for (Card card : hand) {
					if (card.getRankValue() == hand.get(index).getRankValue()
							&& !card.getSuit().equals(hand.get(index).getSuit())) {
						total++;
					}
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("That's not a number! Please try again.");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("That number isn't valid! Try again.");
			}
		}
		scanner = new Scanner(System.in);
		int amt = 1;
		if (hand.get(index).getRankValue() == 15) {
			System.out.println("Great, " + name + "! You cleared the center!");
			currentAmount = 0;
			highestRank = 0;
			hand.remove(index);
			return false;
		} else if (hand.get(index).getRankValue() < highestRank) {
			System.out.println("That card is lower than the card in the center! Try again.\n");
			return turn(i);
		} else if (total > 1) {
			while (true) {
				int min = 1;
				try {
					if (currentAmount != 0) {
						min = currentAmount;
					}
					if (min < total) {
						System.out.print(
								"How many " + hand.get(index).getRank() + "s do you want to play? Enter a number" +
										" from " + min + " to " + total + ": ");
						amt = scanner.nextInt();
					} else {
						amt = total;
					}
					if (amt <= total && (amt >= min || amt + currentAmount == 4)) {
						break;
					}
					if (min > total) {
						System.out.println("Whoops! You don't have enough cards to play!");
						return turn(i);
					}
				} catch (InputMismatchException e) {
					System.out.println("Oh no! That's not a number! Try again.\n");
					continue;
				}
				System.out.println("That value isn't in between " + min + " and " + total + "!\n");
			}
		}
		if (amt < currentAmount && !(amt + currentAmount == 4 && highestRank == hand.get(index).getRankValue())) {
			System.out.println("Whoops! You don't have enough cards to play!");
			return turn(i);
		}
		if (highestRank == hand.get(index).getRankValue()) {
			currentAmount += amt;
		} else {
			currentAmount = amt;
			highestRank = hand.get(index).getRankValue();
		}
		for (int j = 0; j < amt; j++) {
			for (int k = 0; k < hand.size(); k++) {
				if (hand.get(k).getRankValue() == highestRank) {
					hand.remove(k);
					break;
				}
			}
		}
		if (currentAmount == 4) {
			System.out.println("You cleared the board, " + name + "!");
			currentAmount = 0;
			highestRank = 0;
		}
		System.out.println("Your turn is over.\n\n");
		Thread.sleep(2000);
		return false;
	}

	/**
	 * simulates one player taking a card from another
	 *
	 * @param taker index of player taking a card
	 * @param taken index of player getting a card taken
	 */
	private void takeCard(int taker, int taken) {
		System.out.println(
				"\n\n" + Game.getName(taker) + ", you have to take a card from " + Game.getName(taken) + ".\nHere's " +
						"their hand:");
		printHand(hands.get(taken), false);
		System.out.println("\n\nAnd here's yours:");
		printHand(hands.get(taker), false);
		Card takenCard, givenCard;
		while (true) {
			try {
				System.out.print("So, which card do you want to take? Enter the index: ");
				scanner = new Scanner(System.in);
				int index = scanner.nextInt() - 1;
				takenCard = hands.get(taken).get(index);
				hands.get(taker).add(takenCard);
				hands.get(taken).remove(takenCard);
				break;
			} catch (InputMismatchException e) {
				System.out.println("That's not a number! Input again.\n");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("That index doesn't work. Input again.\n");
			}
		}
		while (true) {
			try {
				System.out.print("Which card do you want to give back? Enter the index: ");
				scanner = new Scanner(System.in);
				int index = scanner.nextInt() - 1;
				givenCard = hands.get(taker).get(index);
				hands.get(taken).add(givenCard);
				hands.get(taker).remove(givenCard);
				break;
			} catch (InputMismatchException e) {
				System.out.println("That's not a number! Input again.\n");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("That index doesn't work. Input again.\n");
			}
		}
		System.out.println("Nice, the " + takenCard + " and the " + givenCard + " have been traded!");
	}

	public int play() throws InterruptedException {
		System.out.println("\n----------------------------\n\nLet's play President!\n");
		int numPeople = 0;
		do {
			scanner = new Scanner(System.in);
			try {
				System.out.print("How many people do you want to play with? Enter a number from 3 to 7: ");
				numPeople = scanner.nextInt();
				scanner.nextLine();
				if (numPeople < 3 || numPeople > 7) {
					System.out.println("That number is not in the range. Try again.\n");
				}
			} catch (InputMismatchException e) {
				System.out.println("That number is not valid! Please try again.");
			}
		} while (numPeople < 3 || numPeople > 7);
		boolean vpMode = numPeople >= 5;
		int[] hierarchy = new int[numPeople], points = new int[numPeople];
		for (int i = 1; i < numPeople; i++) {
			Game.promptName(i, "Enter player " + (i + 1) + "'s name: ");
			hands.add(new Hand());
			hasPassed.add(false);
		}
		boolean firstRound = true;
		char again = 'y';
		do {
			if (hierarchy[0] != 0 || hierarchy[1] != 0) {
				firstRound = false;
			}
			if (firstRound) {
				initRound(numPeople, hands);
				round(numPeople, hierarchy);
			} else {
				ArrayList<Hand> handsToChoose = new ArrayList<>();
				for (int i = 0; i < numPeople; i++) {
					handsToChoose.add(new Hand());
				}
				initRound(numPeople, handsToChoose);
				for (int player : hierarchy) {
					scanner = new Scanner(System.in);
					printChoices(handsToChoose);
					while (true) {
						try {
							System.out.print(
									Game.getName(player) + ", which hand do you want? Enter the corresponding index: ");
							int index = scanner.nextInt() - 1;
							hands.set(player, handsToChoose.get(index));
							handsToChoose.remove(index);
							break;
						} catch (IndexOutOfBoundsException e) {
							System.out.println("That number isn't valid! Try again.\n");
						} catch (InputMismatchException e) {
							System.out.println("That isn't a number! Try again.\n");
						}
					}
				}
				takeCard(hierarchy[0], hierarchy[hierarchy.length - 1]);
				if (vpMode) {
					takeCard(hierarchy[0], hierarchy[hierarchy.length - 1]);
					takeCard(hierarchy[1], hierarchy[hierarchy.length - 2]);
				}
				round(numPeople, hierarchy);
			}
			for (int i = 0; i < hierarchy.length; i++) {
				points[hierarchy[i]] += numPeople - i;
			}
			Scanner scanner1 = new Scanner(System.in);
			do {
				if (again != 'y') {
					System.out.println("That wasn't 'y' or 'n'! Please try again.");
				}
				System.out.print("Do you want to play the next round? Enter 'y' or 'n': ");
				again = scanner1.nextLine().toLowerCase().charAt(0);
			} while (again != 'n' && again != 'y');
			scanner1.close();
		} while (again != 'n');
		System.out.println("Let's see who won!");
		printScores(points);
		return 1;
	}
}
