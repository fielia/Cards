package text.games;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import text.backend.Deck;
import text.backend.Hand;
import text.backend.Card;
import text.backend.Game;

public class Kings extends Game {
	public int cost() {
		return 15;
	}

	public String rules() {
		return "In this card game, players take turns placing cards on piles until they run out of cards. On their turn, they can either place a card of opposite color and one lower rank than the bottom card of a pile (placing any card if it's the first in the pile), place a card of opposite color and one lower rank than the bottom card of a foundational pile (placing a king of any suit if it's the first in the pile), or move a whole pile onto another one, given the first card of the pile being moved is of opposite color and one lower rank than the bottom card of the pile it is moving onto. The player plays until there are no moves left for them, at which point it is the next player's turn.";
	}

	@Override
	protected int playerLimit() {
		return 4;
	}

	@Override
	public String toString() {
		return "Kings in the Corner";
	}

	private void printPiles(ArrayList<Hand> piles) throws InterruptedException {
		for (int i = 0; i < piles.size(); i++) {
			System.out.print("Pile #" + (i + 1) + ": ");
			piles.get(i).printHand3();
		}
		System.out.println();
	}

	private void printAllPiles(ArrayList<Hand> piles, ArrayList<Hand> foundations) throws InterruptedException {
		for (int i = 0; i < piles.size(); i++) {
			System.out.print("Pile #" + (i + 1) + ":              ");
			piles.get(i).printHand3();
		}
		for (int i = 0; i < foundations.size(); i++) {
			System.out.print("Pile #" + (i + piles.size() + 1) + " (Foundation): ");
			if (foundations.get(i).size() == 13) {
				System.out.println("Completed Pile (King to Ace).");
			} else {
				foundations.get(i).printHand3();
			}
		}
		System.out.println();
	}

	private boolean canPlay(Hand hand, ArrayList<Hand> piles, ArrayList<Hand> foundations) {
		for (Hand pile : piles) {
			for (Card card : hand) {
				if (validateMove(card, pile, false)) {
					return true;
				}
			}
			for (Hand otherPile : piles) {
				if (!pile.equals(otherPile) && validateMove(pile.get(0), otherPile, false)) {
					return true;
				}
			}
			for (Hand foundation : foundations) {
				if (validateMove(pile.get(0), foundation, true)) {
					return true;
				}
			}
		}

		for (Hand foundation : foundations) {
			for (Card card : hand) {
				if (validateMove(card, foundation, true)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Validates that a move is valid.
	 * 
	 * @param card the card that is playing
	 * @param pile the pile the card is being placed on
	 * @return true if the move is valid, false otherwise
	 */
	private boolean validateMove(Card card, Hand pile, boolean isFoundation) {
		if (pile.size() == 0) {
			if (!isFoundation) {
				return true;
			} else {
				return card.getRankValue() == 13;
			}
		}

		Card bottomCard = pile.get(pile.size() - 1);

		return card.getColor() != bottomCard.getColor() && card.getRankValue() + 1 == bottomCard.getRankValue();
	}

	private void turn(Scanner scanner, Hand hand, Deck deck, ArrayList<Hand> piles, ArrayList<Hand> foundations)
			throws InterruptedException {
		while (canPlay(hand, piles, foundations)) {
			System.out.println("\n");
			hand.printHand();
			System.out.println();
			Game.sleep(200);
			printAllPiles(piles, foundations);
			Game.sleep(200);
			char move = ' ';
			while (true) {
				try {
					System.out.print("Do you want to play a card from your hand or move a pile? ");
					move = scanner.next().toLowerCase().charAt(0);
					if (move != 'h' && move != 'p') {
						throw new IllegalArgumentException();
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println("That is not a valid input. Please try again.");
				} catch (IllegalArgumentException e) {
					System.out.println(
							"That is not a valid input. Please try again. \033[3mHint: Input 'hand' to play a card from your hand, or 'pile' to move a pile.\033[0m");
				}
			}

			if (move == 'h') {
				System.out.println();
				Game.sleep(200);
				hand.printHand();
				System.out.println();
				Game.sleep(200);
				int cardIndex = -1;
				while (true) {
					try {
						System.out.print("Which card do you want to play? Enter its number: ");
						cardIndex = scanner.nextInt() - 1;
						if (cardIndex < 0 || cardIndex > hand.size()) {
							throw new IllegalArgumentException();
						}
						scanner.nextLine();
						break;
					} catch (InputMismatchException e) {
						System.out.println("That is not a number. Please try again.");
					} catch (IllegalArgumentException e) {
						System.out.println("That is not a valid card. Please try again.");
					}
					scanner.nextLine();

				}
				Card cardPlayed = hand.get(cardIndex);
				System.out.println();
				Game.sleep(200);
				printAllPiles(piles, foundations);
				Game.sleep(200);
				int pileIndex = -1;
				while (true) {
					try {
						System.out
								.print("On which pile do you want to place the " + cardPlayed
										+ " on? Enter its number: ");
						pileIndex = scanner.nextInt() - 1;
						scanner.nextLine();
						if (pileIndex < 0 || pileIndex > piles.size() + foundations.size()) {
							throw new IllegalArgumentException();
						}
						break;
					} catch (InputMismatchException e) {
						System.out.println("That is not a number. Please try again.");
					} catch (IllegalArgumentException e) {
						System.out.println("That is not a valid pile. Please try again.");
					}
					scanner.nextLine();
				}
				Hand pile;
				boolean isFoundation;
				if (pileIndex < piles.size()) {
					pile = piles.get(pileIndex);
					isFoundation = false;
				} else {
					pile = foundations.get(pileIndex - piles.size());
					isFoundation = true;
				}
				if (!validateMove(cardPlayed, pile, isFoundation)) {
					System.out.println("That is not a valid move! Please try again.");
					continue;
				}
				pile.add(cardPlayed);
				hand.remove(cardPlayed);
			} else {
				System.out.println();
				Game.sleep(200);
				printPiles(piles);
				Game.sleep(200);
				int movePileIndex = -1;
				while (true) {
					try {
						System.out.print("Which pile do you want to move? Enter its number: ");
						movePileIndex = scanner.nextInt() - 1;
						scanner.nextLine();
						if (movePileIndex < 0 || movePileIndex > piles.size() || piles.get(movePileIndex).size() == 0) {
							throw new IllegalArgumentException();
						}
						break;
					} catch (InputMismatchException e) {
						System.out.println("That is not a number. Please try again.");
					} catch (IllegalArgumentException e) {
						System.out.println("That is not a valid pile. Please try again.");
					}
					scanner.nextLine();
				}
				Hand movePile = piles.get(movePileIndex);
				System.out.println();
				Game.sleep(200);
				printAllPiles(piles, foundations);
				Game.sleep(200);
				int placePileIndex = -1;
				while (true) {
					try {
						System.out
								.print("On which pile do you want to place the " + movePile.get(0)
										+ " on? Enter its number: ");
						placePileIndex = scanner.nextInt() - 1;
						scanner.nextLine();
						if (placePileIndex < 0 || placePileIndex > piles.size() + foundations.size()
								|| placePileIndex == movePileIndex) {
							throw new IllegalArgumentException();
						}
						break;
					} catch (InputMismatchException e) {
						System.out.println("That is not a number. Please try again.");
					} catch (IllegalArgumentException e) {
						System.out.println("That is not a valid pile. Please try again.");
					}
					scanner.nextLine();
				}
				Hand placePile;
				boolean isFoundation;
				if (placePileIndex < piles.size()) {
					placePile = piles.get(placePileIndex);
					isFoundation = false;
				} else {
					placePile = foundations.get(placePileIndex - piles.size());
					isFoundation = true;
				}
				if (!validateMove(movePile.get(0), placePile, isFoundation)) {
					System.out.println("That is not a valid move! Please try again.");
					continue;
				}
				placePile.add(movePile);
				movePile.clear();
			}
		}
		System.out.println("You have no more moves left! Your turn is over.");
		Game.sleep(2000);
	}

	@Override
	public int play(Scanner scanner) throws InterruptedException {
		Deck deck = new Deck();
		deck.modifyRanks(new String[] { "Ace" }, new int[] { 1 });
		System.out.println("\n----------------------------\n\nLet's play Kings in the Corner!\n");
		int players = 0;
		while (true) {
			try {
				System.out.print("Enter the amount of players, from 2 to 4: ");
				players = scanner.nextInt();
				scanner.nextLine();
				if (players < 2 || players > 4) {
					throw new IllegalArgumentException();
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("That was not a number. Please try again.");
			} catch (IllegalArgumentException e) {
				System.out.println("That is not a valid amount of players. Please try again.");
			}
			scanner.nextLine();
		}

		ArrayList<Hand> hands = new ArrayList<>();
		for (int i = 0; i < players; i++) {
			Game.promptName(i, "Enter player " + (i + 1) + "'s name: ", scanner);
			hands.add(new Hand());
		}
		deck.dealCards(hands, 7);

		ArrayList<Hand> piles = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			piles.add(new Hand());
			piles.get(i).add(deck.draw());
		}

		ArrayList<Hand> foundations = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			foundations.add(new Hand());
		}

		System.out.println(
				"\n\033[3mNote: The printed piles are displayed from top of the pile to bottom of the pile, so a placed card or moved pile will be added to the end.\033[0m\n");

		boolean continuePlaying = true;
		int finalPlayer = 0;

		while (continuePlaying) {
			for (int player = 0; player < players; player++) {
				Hand currentHand = hands.get(player);
				System.out.println("\nIt is " + Game.getName(player) + "'s turn!");
				Game.sleep(1500);
				Card drawnCard = deck.draw();
				System.out.println(Game.getName(player) + " drew the " + drawnCard + ".");
				currentHand.add(drawnCard);
				currentHand.organizeCards();
				turn(scanner, currentHand, deck, piles, foundations);
				if (currentHand.size() == 0) {
					continuePlaying = false;
					System.out.println("\n\nWow, " + Game.getName(player) + ", you've won! Nice work.");
					finalPlayer = player;
					break;
				}
			}
		}

		boolean[] hasBeenMentioned = new boolean[players];
		int place = 2;
		hasBeenMentioned[finalPlayer] = true;
		boolean moveOn = true;
		while (moveOn) {
			ArrayList<Integer> next = new ArrayList<>();
			int smallest = 0;
			for (int i = 0; i < players; i++) {
				if ((smallest > hands.get(i).size() || smallest == 0) && !hasBeenMentioned[i]) {
					smallest = hands.get(i).size();
					next.clear();
					next.add(i);
				} else if (smallest == hands.get(i).size() && !hasBeenMentioned[i]) {
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
				System.out.print(Game.getName(next.get(i)));
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
		System.out.println("\n");

		return 1;
	}
}
