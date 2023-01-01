package text.games;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Trash extends Game {
	
	public int cost() {
		return 20;
	}
	
	public String rules() {
		return "There are two players who, initially, are dealt 10 cards. Each turn, a player can choose to take a " +
				       "card from the deck or take the top card of the discard pile. If the card's rank is less than " +
				       "or equal to the amount of cards they have, they place the card in the correct spot. Then they" +
				       " look at the card previously there. If that card's rank is within the right range, and is not" +
				       " the same as an already revealed spot, they place it. The process repeats until you get a " +
				       "King, a Queen, or an already revealed card. Then, the player's turn ends, and the other " +
				       "player starts their turn. If a player picks up a Jack, they can choose where to put it, and " +
				       "then can always pick it up again.\nOnce all the cards in a player's hand are revealed, the " +
				       "round is over, and the player that won will be dealt 1 less card the next round. If a player " +
				       "wins a round where they have only one card, they win the entire game!";
	}
	
	/**
	 * unique printHand() method
	 *
	 * @param hand hand to print out
	 */
	private void printHand(ArrayList<Card> hand) {
		int width = hand.size() > 9 ? 2 : 1;
		for (int i = 0; i < hand.size(); i++) {
			String value = hand.get(i).isRevealed() ? hand.get(i).toString() : "NOT REVEALED";
			System.out.printf("%" + width + "d: %s\n", i + 1, value);
		}
	}
	
	private boolean allRevealed(ArrayList<Card> hand) {
		for (Card card: hand) {
			if (!card.isRevealed()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * the amount of cards in player A's hand
	 */
	private int numberOfCardsA;
	/**
	 * the amount of cards in player B's hand
	 */
	private int numberOfCardsB;
	/**
	 * the card in the center of the pile
	 */
	private Card centerCard;
	
	
	public String toString() {
		return "Trash";
	}
	
	@Override
	protected int playerLimit() {
		return 2;
	}
	
	/**
	 * places a picked up card
	 *
	 * @param hand player's hand
	 * @param card picked up card
	 * @return card dropped in center
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private Card drawCard(ArrayList<Card> hand, Card card) throws InterruptedException {
		int no_of_cards = hand.size();
		int index;
		Scanner scanner = new Scanner(System.in);
		Thread.sleep(1000);
		while (true) {
			Game.sleep(1000);
			if (card.getRank().equals("Jack")) {
				if (allRevealed(hand)) {
					System.out.println("You got a Jack, but your hand is already filled!");
					break;
				}
				while (true) {
					try {
						System.out.println("\nThat's a random Jack!\nHere is your hand again:");
						printHand(hand);
						System.out.print("Input the index of the card you want to replace with the Jack, or 0 if you " +
								                 "want to place it in a random spot: ");
						index = scanner.nextInt() - 1;
						if (index == -1) {
							do {
								index = (int) (Math.random() * hand.size());
							} while (hand.get(index).isRevealed());
							System.out.println((index + 1) + " is the random number.");
						}
						Game.sleep(1000);
						if (!hand.get(index).isRevealed()) {
							System.out.println(hand.get(index) + " is the card in that spot.");
							Card card1 = card;
							card = hand.get(index);
							hand.remove(index);
							hand.add(index, card1);
							hand.get(index).setRevealed(true);
						} else {
							System.out.println("That card is already visible.");
						}
						break;
					} catch (IndexOutOfBoundsException e) {
						System.out.println("That spot doesn't exist! Please try again.");
					} catch (InputMismatchException e) {
						System.out.println("That isn't a number! Try again.");
						scanner.nextLine();
					}
				}
			} else if (card.getRankValue() <= no_of_cards || card.getRank().equals("Ace")) {
				System.out.println("\nThat card will replace its corresponding card.");
				if (card.getRank().equals("Ace")) {
					index = 0;
				} else {
					index = card.getRankValue() - 1;
				}
				Game.sleep(1000);
				if (!hand.get(index).isRevealed() || hand.get(index).getRank().equals("Jack")) {
					System.out.println(hand.get(index) + " is the card in that spot.");
					Card card1 = card;
					card = hand.get(index);
					hand.remove(index);
					hand.add(index, card1);
					hand.get(index).setRevealed(true);
				} else {
					System.out.println("That card is already visible. Too bad.");
					break;
				}
			} else if (card.getRankValue() > Math.max(numberOfCardsA, numberOfCardsB)) {
				System.out.println("\nThat card is garbage. Better luck next time!");
				scanner.close();
				return new Card("King", "Spades");
			} else {
				System.out.println("\nThat card is garbage. Better luck next time!");
				break;
			}
		}
		scanner.close();
		return card;
	}
	
	/**
	 * one player's turn
	 * @param stock the stock for the game
	 * @param hand the player's hand
	 * @param garbage if the most recent card placed down is a 'garbage' card
	 * @param playerIndex the index of the player (used to get names)
	 * @return the new value for garbage
	 * @throws InterruptedException Thread.sleep() throws this
	 */
	private boolean turn(ArrayList<Card> stock, ArrayList<Card> hand, boolean garbage, int playerIndex) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		Game.sleep(3000);
		char pickUp;
		System.out.println("\nIt is " + Game.getName(playerIndex) + "'s turn.\nHere are your cards: ");
		printHand(hand);
		Game.sleep(1000);
		while (true) {
			if (!garbage) {
				System.out.println("\nDo you want to take a card from the deck or take the card " +
						                   Game.getName((playerIndex + 1) % 2) + " just used, the " + centerCard +
						                   "? Input 'deck' to take from the deck, or 'take' to take the card just " +
						                   "placed down.");
				while (true) {
					try {
						pickUp = scanner.nextLine().toLowerCase().charAt(0);
						break;
					} catch (StringIndexOutOfBoundsException | NoSuchElementException e) {
						System.out.println();
					}
				}
			} else {
				pickUp = 'd';
			}
			if (pickUp == 'd') {
				centerCard = stock.get(0);
				stock.remove(0);
				System.out.println("\nYou pick up a card from the deck. Here it is: '" + centerCard + "'.");
			} else if (pickUp == 't') {
				System.out.println(
						"\nYou take a look at the card just placed down.");
			} else {
				System.out.println("That wasn't a valid input. Please try again.");
				continue;
			}
			break;
		}
		centerCard = drawCard(hand, centerCard);
		Game.sleep(2000);
		System.out.println("Here is your updated hand: ");
		printHand(hand);
		scanner.close();
		return centerCard.getRank().equals("King");
	}
	
	public int play() throws InterruptedException {
		int roundNo = 1;
		System.out.println("\n----------------------------\n\nLet's play Trash!\n");
		boolean nextRound = true;
		Game.promptName(1, "Enter player 2's name: ");
		int cards;
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("How many cards do you want to start with? Enter a number less than 10: ");
				cards = scanner.nextInt();
				if (cards > 10 || cards < 1) {
					System.out.println("That number isn't in between 1 and 10! Try again.");
					continue;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("That's not a number! Please try again.");
			}
		}
		numberOfCardsA = cards;
		numberOfCardsB = cards;
		while (nextRound) {
			ArrayList<Card> handA = new ArrayList<>();
			ArrayList<Card> handB = new ArrayList<>();
			Deck deck = new Deck();
			ArrayList<Card> stock = new ArrayList<>();
			for (int i = 0; i < numberOfCardsA; i++) {
				handA.add(deck.draw());
			}
			for (int i = 0; i < numberOfCardsB; i++) {
				handB.add(deck.draw());
			}
			for (int i = 0; i < deck.size(); i++) {
				stock.add(deck.draw());
			}
			centerCard = stock.get(0);
			stock.remove(0);
			boolean garbage = true;
			while (true) {
				garbage = turn(stock, handA, garbage, 0);
				if (allRevealed(handA)) {
					numberOfCardsA--;
					break;
				}
				garbage = turn(stock, handB, garbage, 1);
				if (allRevealed(handB)) {
					numberOfCardsB--;
					break;
				}
			}
			System.out.println("\nRound Over!!");
			if (numberOfCardsA == 0) {
				System.out.println("Wow! " + Game.getName(0) + " beat " + Game.getName(1) + " by " +
						                   numberOfCardsB + " cards in " + (roundNo) + " rounds! Nice work!");
				break;
			} else if (numberOfCardsB == 0) {
				System.out.println("Wow! " + Game.getName(1) + " beat " + Game.getName(0) + " by " +
						                   numberOfCardsA + " cards in " + (roundNo) + " rounds! Nice work!");
				break;
			}
			roundNo++;
			while (true) {
				System.out.println("Would you like to move on to round " + (roundNo) + "?");
				char select = scanner.next().toLowerCase().charAt(0);
				if (select != 'y' && select != 'n') {
					System.out.println("That wasn't a correct input! Please try again!");
					continue;
				}
				nextRound = (select == 'y');
				break;
			}
		}
		scanner.close();
		return 1;
	}
}
