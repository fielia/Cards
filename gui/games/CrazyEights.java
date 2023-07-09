package gui.games;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;
import text.backend.Hand;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CrazyEights extends Game {

	public int cost() {
		return 30;
	}

	public String rules() {
		return "This multiplayer game has anywhere from 3 to 25 players, with the option to have all of them (except " +
				"one) be controlled by the computer. You have to choose a card from your hand that either has " +
				"the same rank as the card in the center, the same suit as the card in the center, or is an 8." +
				" If you can't play anything, you have to pick up a card. 8s are wild, meaning that the player" +
				" that played an 8 can change the suit to whatever they'd like. The first player to get rid of" +
				" all their cards wins! From there, players are ranked based on the amount of cards left in " +
				"their hand.\n";
	}

	/**
	 * the initial deck of cards
	 */
	private Deck stock;
	/**
	 * the current card in the center
	 */
	private Card centerCard;
	/**
	 * the names of all the players (might be randomized every turn)
	 */
	private ArrayList<String> names;
	/**
	 * the hands for each player (corresponds to 'names' field)
	 */
	private ArrayList<Hand> hands;

	public String toString() {
		return "Crazy Eights";
	}

	protected int playerLimit() {
		return 40;
	}

	/**
	 * creates an array of all the possible cards a hand can play
	 *
	 * @param hand hand to look through and find the playable cards
	 * @return array of playable cards
	 */
	private ArrayList<Card> calcPossibleCards(Hand hand) {
		ArrayList<Card> possibleCards = new ArrayList<>();
		for (Card card : hand) {
			if (card.getSuit().equals(centerCard.getSuit()) || card.getRank().equals(centerCard.getRank())
					|| card.getRankValue() == 8) {
				possibleCards.add(card);
			}
		}
		return possibleCards;
	}

	/**
	 * randomizes turn order
	 *
	 * @param isComp current order of computers
	 * @return new order of computers
	 */
	private ArrayList<Boolean> randomize(ArrayList<Boolean> isComp) {
		ArrayList<String> newList1 = new ArrayList<>(names.size());
		ArrayList<Hand> newList2 = new ArrayList<>(hands.size());
		ArrayList<Boolean> comps = new ArrayList<>(isComp.size());
		boolean[] set = new boolean[hands.size()];
		for (int i = 0; i < hands.size(); i++) {
			int newIndex;
			do {
				newIndex = (int) (names.size() * Math.random());
			} while (set[newIndex]);
			set[newIndex] = true;
			newList1.add(names.get(newIndex));
			newList2.add(hands.get(newIndex));
			comps.add(isComp.get(newIndex));
		}
		names = newList1;
		hands = newList2;
		return comps;
	}

	/**
	 * one human player turn
	 *
	 * @param hand hand of player
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private void turnHuman(Hand hand) throws InterruptedException {
		int index;
		Scanner scanner = new Scanner(System.in);
		ArrayList<Card> possibleCards;
		possibleCards = calcPossibleCards(hand);
		System.out.println("Here is your hand:");
		hand.printHand();
		if (possibleCards.size() == 0) {
			System.out.println("Oh no! You can't play any cards! Here, take one from the deck.");
			hand.add(stock.draw());
			scanner.close();
			return;
		}
		System.out.println("The current card on top is the " + centerCard + ".");
		while (true) {
			System.out.print("Which card will you play? Input the index. ");
			try {
				index = scanner.nextInt() - 1;
				scanner.nextLine();
				if (index < 0 || index >= hand.size()) {
					System.out.println("That's not a card! Please try again!");
					continue;
				}
			} catch (InputMismatchException e) {
				System.out.println("That's not a number! Try again.");
				continue;
			}
			if (!possibleCards.contains(hand.get(index))) {
				System.out.println("Whoops! you can't play that card!");
				continue;
			}
			break;
		}
		if (hand.get(index).getRankValue() == 8) {
			String newSuitString;
			System.out.println("Oh wow! An Eight! What suit do you want it to represent?");
			while (true) {
				System.out.println("Input the suit name in proper capitals.");
				newSuitString = scanner.nextLine();
				if (!newSuitString.equals("Spades") && !newSuitString.equals("Hearts") &&
						!newSuitString.equals("Diamonds") && !newSuitString.equals("Clubs")) {
					System.out.println("I'm sorry, that's not a valid suit. Please only enter 'Spades', 'Hearts', " +
							"'Diamonds', and 'Clubs'.");
					continue;
				}
				System.out.println("Done!");
				break;
			}
			hand.set(index, new Card(hand.get(index).getRank(), newSuitString));
		}
		stock.add(stock.size() - 1, centerCard);
		centerCard = hand.get(index);
		hand.remove(index);
		Thread.sleep(1500);
		System.out.println("Nice turn!!");
		scanner.close();
	}

	/**
	 * one computer player turn
	 *
	 * @param hand hand of computer
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private void turnComp(Hand hand) throws InterruptedException {
		ArrayList<Card> possibleCards;
		Card oldCard;
		possibleCards = calcPossibleCards(hand);
		if (possibleCards.size() == 0) {
			System.out.println("The computer can't play any cards!");
			if (stock.size() != 0) {
				hand.add(stock.draw());
				System.out.println("It took one from the deck, increasing its total card count to " + (hand.size()) +
						".");
			} else {
				System.out.println("Because there aren't any cards in the deck, " +
						"it passed its turn.");
			}
			return;
		}
		System.out.println("The current card on top is the " + centerCard + ".");
		int index = (int) (Math.random() * possibleCards.size());
		System.out.println("The computer chose to play the " + possibleCards.get(index) + ".");
		oldCard = possibleCards.get(index);
		if (possibleCards.get(index).getRankValue() == 8) {
			int newSuitIndex = (int) (Math.random() * 4);
			String newSuitString = switch (newSuitIndex) {
				case 0 -> "Spades";
				case 1 -> "Hearts";
				case 2 -> "Diamonds";
				default -> "Clubs";
			};
			System.out.println("Oh wow! An Eight! The computer changed the suit to be " + newSuitString + ".");
			oldCard = new Card("8", newSuitString);
		}
		stock.add(centerCard);
		centerCard = oldCard;
		hand.remove(possibleCards.get(index));
		String suffix = hand.size() == 1 ? "" : "s";
		if (hand.size() != 0) {
			System.out.println("They now have " + (hand.size()) + " card" + suffix + " left.");
		}
		Thread.sleep(1500);
	}

	public int play() throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n----------------------------\n\nLet's play Crazy Eights!\n");
		int numPeople = 0, numComps = 0;
		do {
			try {
				System.out.print("How many players, human and computer, do you want to play with? Enter a number from" +
						" 2 to 40: ");
				numPeople = scanner.nextInt();
				scanner.nextLine();
				if (numPeople < 2 || numPeople > 40) {
					System.out.println("That number is not in the range. Try again.\n");
				}
			} catch (InputMismatchException e) {
				System.out.println("That number is not valid! Please try again.");
			}
		} while (numPeople < 2 || numPeople > 40);
		do {
			try {
				System.out.print("How many human players do you want to play with? Enter a number from 0 to " +
						numPeople + ": ");
				numComps = numPeople - scanner.nextInt();
				scanner.nextLine();
				if (numComps < 0 || numComps > numPeople) {
					System.out.println("That number is not in the range. Try again.\n");
				}
			} catch (InputMismatchException e) {
				System.out.println("That number is not valid! Please try again.");
			}
		} while (numComps < 0 || numComps > numPeople);
		ArrayList<Boolean> isComp = new ArrayList<>();
		names = new ArrayList<>(numPeople);
		hands = new ArrayList<>(numPeople);
		for (int i = 0; i < numPeople; i++) {
			boolean secondTime = false, error = false;
			while (true) {
				Scanner scanner1 = new Scanner(System.in);
				if (Game.hasPlayer(i) && numComps + i < numPeople) {
					names.add(Game.getName(i));
					isComp.add(false);
					break;
				} else if (numComps + i < numPeople && !error) {
					System.out.print("Enter player " + (i + 1) + "'s name: ");
					String nextName = scanner1.nextLine();
					if (secondTime) {
						names.set(i, nextName);
					} else {
						names.add(nextName);
						isComp.add(false);
					}
					Game.setName(i, nextName);
				} else {
					names.add("Computer " + (i + 1 - numPeople + numComps));
					isComp.add(true);
					break;
				}
				System.out.print("Does the name '" + names.get(i) + "' work for you? ");
				char works = scanner.nextLine().toLowerCase().charAt(0);
				if (works == 'n') {
					secondTime = true;
					System.out.println("Okay, you can try again.\n");
				} else if (works == 'y') {
					System.out.println("Okay, moving on.\n");
					break;
				} else {
					error = true;
					System.out.println("That input is invalid. Please try again.");
				}
				scanner1.close();
			}
		}
		int cardsPer;
		if (numPeople <= 5) {
			cardsPer = 9;
		} else if (numPeople <= 7) {
			cardsPer = 7;
		} else if (numPeople <= 10) {
			cardsPer = 5;
		} else if (numPeople <= 16) {
			cardsPer = 4;
		} else if (numPeople <= 30) {
			cardsPer = 3;
		} else {
			cardsPer = 2;
		}
		if (numPeople >= 25) {
			cardsPer += 1;
			stock = new Deck(3);
		} else if (numPeople > 20) {
			cardsPer += 1;
			stock = new Deck(2);
		} else if (numPeople >= 15) {
			cardsPer += 2;
			stock = new Deck(2);
		} else {
			stock = new Deck();
		}
		for (int i = 0; i < numPeople; i++) {
			hands.add(new Hand(cardsPer, stock, numPeople - hands.size()));
		}
		centerCard = stock.draw();
		ArrayList<String> OGNames = names;
		ArrayList<Hand> OGHands = new ArrayList<>();
		boolean randomize;
		while (true) {
			try {
				System.out.print("Would you like to randomize the turn order every round? ");
				char r = scanner.nextLine().toLowerCase().charAt(0);
				if (r != 'y' && r != 'n') {
					System.out.println("That input is not 'yes' or 'no'. Try again.\n");
				}
				randomize = r == 'y';
				break;
			} catch (InputMismatchException e) {
				System.out.println("That input is not valid! Please try again.");
			}
		}
		int winner = -1;
		isComp = randomize(isComp);
		while (winner == -1) {
			if (randomize) {
				isComp = randomize(isComp);
				System.out.println("\n\n\nRandomizing the order!\n");
				Game.sleep(1000);
			}
			for (int i = 0; i < names.size(); i++) {
				System.out.println("\n\nIt's " + names.get(i) + "'s turn.");
				Game.sleep(1500);
				if (!isComp.get(i)) {
					turnHuman(hands.get(i));
				} else {
					turnComp(hands.get(i));
				}
				Game.sleep(2000);
				if (hands.get(i).size() == 0) {
					winner = i;
					break;
				}
			}
		}
		System.out.println("Congratulations " + names.get(winner) + "! You have gotten rid of all your cards first! " +
				"You win! Good job!\n");
		boolean[] hasBeenMentioned = new boolean[numPeople];
		int place = 2;
		for (String name : OGNames) {
			OGHands.add(hands.get(names.indexOf(name)));
		}
		hasBeenMentioned[OGNames.indexOf(names.get(winner))] = true;
		boolean moveOn = true;
		while (moveOn) {
			ArrayList<Integer> next = new ArrayList<>();
			int smallest = 0;
			for (int i = 0; i < OGNames.size(); i++) {
				if ((smallest > OGHands.get(i).size() || smallest == 0) && !hasBeenMentioned[i]) {
					smallest = OGHands.get(i).size();
					next.clear();
					next.add(i);
				} else if (smallest == OGHands.get(i).size() && !hasBeenMentioned[i]) {
					next.add(i);
				}
			}

			String suffix, suffix2 = smallest == 1 ? "" : "s";
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
				System.out.print(OGNames.get(next.get(i)));
				hasBeenMentioned[next.get(i)] = true;
				if (i == next.size() - 2 && i != 0) {
					System.out.print(", and ");
				} else if (i == next.size() - 2) {
					System.out.print(" and ");
				} else if (i != next.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.print(" got " + place + suffix + " place with " + smallest + " card" + suffix2);
			if (next.size() > 1) {
				System.out.print(" each");
			}
			System.out.println("!");
			moveOn = false;
			place += next.size();
			for (boolean bool : hasBeenMentioned) {
				if (!bool) {
					moveOn = true;
					break;
				}
			}
		}
		if (numComps == 0 || numComps == numPeople) {
			winner = 1;
		} else if (isComp.get(winner)) {
			winner = 0;
		} else {
			winner = 2;
		}
		Thread.sleep(1000);
		System.out.println("\n\nPlease play again soon!");
		scanner.close();
		return winner;
	}
}
