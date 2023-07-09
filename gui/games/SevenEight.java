package gui.games;

import java.util.InputMismatchException;
import java.util.Scanner;

import text.backend.Card;
import text.backend.Deck;
import text.backend.Game;
import text.backend.Hand;

public class SevenEight extends Game {

	public int cost() {
		return 15;
	}

	public String rules() {
		return "This game pits a human player against a computer. The goal is to get a larger amount of plays than " +
				"the computer. A play consists of one player placing a card in the center, then the other " +
				"trying to beat it. The winner of the play starts the next one. The second player has to play " +
				"a card of the same suit as the one the first player played, and higher rank to win the play, " +
				"if possible. If not, they can play a card from a chosen superior suit, called the trump, that" +
				" \033[3mtrumps\033[0m all other suits, no matter the rank.";
	}

	public String toString() {
		return "Seven-Eight";
	}

	protected int playerLimit() {
		return 1;
	}

	/**
	 * checks who won the hand
	 *
	 * @param trump         suit that is superior
	 * @param cardA         player's card
	 * @param cardB         comp's card
	 * @param isPlayersTurn who's turn it is now
	 * @return true if player won the hand, false if comp won
	 */
	private boolean compareCards(String trump, Card cardA, Card cardB,
			boolean isPlayersTurn) {
		if (cardA.getRankValue() > cardB.getRankValue() && cardA.getSuit().equals(cardB.getSuit())) {
			isPlayersTurn = true;
		} else if (cardA.getRankValue() < cardB.getRankValue() && cardA.getSuit().equals(cardB.getSuit())) {
			isPlayersTurn = false;
		} else if (cardA.getSuit().equals(trump)) {
			isPlayersTurn = true;
		} else if (cardB.getSuit().equals(trump)) {
			isPlayersTurn = false;
		}
		if (isPlayersTurn) {
			System.out.println("\nYay! You got the point!\n");
		} else {
			System.out.println("\nOops! The computer got the point.");
		}
		return isPlayersTurn;
	}

	public int play() throws InterruptedException {
		String trump;
		int pointA = 0;
		Scanner scanner = new Scanner(System.in);
		int pointB = 0;
		Card cardA;
		Card cardB;
		Deck deck = new Deck();
		boolean isPlayersTurn = true;
		System.out.println("\n----------------------------\n\nLet's play Seven - Eight!\n");
		deck.removeIf(card -> !(card.getRankValue() > 7 || (card.getRankValue() == 7 && (card.getSuit().equals(
				"Spades") || card.getSuit().equals("Hearts")))));
		Hand handA = new Hand(5, deck, 2);
		Hand handB = new Hand(5, deck, 2);
		System.out.println("You have received 5 cards. Here they are:");
		handA.printHand2();
		while (true) {
			System.out.println("Which suit do you want to be trump? Input the suit name.");
			char trumpChoice = scanner.next().toLowerCase().charAt(0);
			if (trumpChoice == 's') {
				trump = "Spades";
				break;
			} else if (trumpChoice == 'h') {
				trump = "Hearts";
				break;
			} else if (trumpChoice == 'd') {
				trump = "Diamonds";
				break;
			} else if (trumpChoice == 'c') {
				trump = "Clubs";
				break;
			} else {
				System.out.println("That suit isn't one of the four! As a reminder, the suits you can enter are: " +
						"'Spades', 'Hearts', 'Diamonds', and 'Clubs'.");
			}
		}
		System.out.println("Okay. " + trump + " is now the trump.\nDealing the rest of the cards...\n");
		for (int i = 0; i < 10; i++) {
			handA.add(deck.draw());
			handB.add(deck.draw());
		}
		handB.organizeCards();
		while (handA.size() > 0) {
			Hand handAPlayable = new Hand();
			Hand handBPlayable = new Hand();
			System.out.println("\nHere is your hand:");
			handA.printHand();
			if (isPlayersTurn) {
				System.out.println("\nIt is your turn. Choose a card to play.");
				while (true) {
					try {
						cardA = handA.get(scanner.nextInt() - 1);
						break;
					} catch (IndexOutOfBoundsException e) {
						System.out.println("Whoops! That's not an accurate number! Try again!");
					} catch (InputMismatchException e) {
						System.out.println("That's not a number! Try again!");
					}
				}
				for (Card card : handB) {
					if (cardA.getSuit().equals(card.getSuit())) {
						handBPlayable.add(card);
					}
				}
				if (handBPlayable.size() == 0) {
					cardB = handB.get((int) (Math.random() * handB.size()));
				} else {
					cardB = handBPlayable.get((int) (Math.random() * handBPlayable.size()));
				}
				System.out.println("The computer is playing " + cardB + ".");
			} else {
				cardB = handB.get((int) (Math.random() * handB.size()));
				for (Card card : handA) {
					if (card.getSuit().equals(cardB.getSuit())) {
						handAPlayable.add(card);
						Game.sleep(250);
					}
				}
				do {
					System.out.println("\nIt is the computer's turn. Let's see what they play.");
					Game.sleep(2000);
					System.out.println("They have played " + cardB + ". Which card will you play?");
					while (true) {
						try {
							cardA = handA.get(scanner.nextInt() - 1);
							break;
						} catch (IndexOutOfBoundsException e) {
							System.out.println("Whoops! That's not an accurate number! Try again!");
						} catch (InputMismatchException e) {
							System.out.println("That's not a number! Try again!");
						}
					}
					if (handAPlayable.size() > 0 && !handAPlayable.contains(cardA)) {
						System.out.println("You can't play that card! Try again!");
					}
				} while (handAPlayable.size() > 0 && !handAPlayable.contains(cardA));
			}
			isPlayersTurn = compareCards(trump, cardA, cardB, isPlayersTurn);
			handA.remove(cardA);
			handB.remove(cardB);
			if (isPlayersTurn) {
				pointA++;
			} else {
				pointB++;
			}
		}
		int outcome;
		if (pointA > pointB) {
			System.out.println("You beat the computer by " + (pointA - pointB) + " points! Congratulations, " +
					Game.getName(0) + "!");
			outcome = 2;
		} else if (pointA < pointB) {
			System.out.println("The computer beat you by " + (pointB - pointA) + " points! Better luck next time, + " +
					Game.getName(0) + "!");
			outcome = 0;
		} else {
			System.out.println("You tied with the computer! Nice, " + Game.getName(0) + "!");
			outcome = 1;
		}
		System.out.println("The final score was you vs the computer, " + pointA + " points to " + pointB + " points.");
		scanner.close();
		return outcome;
	}
}
