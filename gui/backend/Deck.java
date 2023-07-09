package gui.backend;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {

	public Deck() {
		String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
		String[] suits = { "Hearts", "Spades", "Clubs", "Diamonds" };
		for (String rank : ranks) {
			for (String suit : suits) {
				this.add(new Card(rank, suit));
			}
		}
		shuffle();
	}

	/**
	 * method to shuffle the deck
	 */
	public void shuffle() {
		Collections.shuffle(this);
	}

	public void moveToBack(Card card) {
		if (!this.contains(card)) {
			throw new IndexOutOfBoundsException("Card is not in the deck.");
		}
		this.remove(card);
		this.add(card);
	}

	/**
	 * changes the value of each card of a certain rank
	 *
	 * @param ranks list of ranks of cards to change
	 * @param values list new value of the cards
	 * @throws IndexOutOfBoundsException ranks and values must be of equal length
	 */
	public void modifyRanks(String[] ranks, int[] values) throws IndexOutOfBoundsException {
		if (ranks.length != values.length) {
			throw new IndexOutOfBoundsException("Ranks and Values must be of the same length");
		}
		for (int i = 0; i < ranks.length; i++) {
			for (Card card : this) {
				if (card.getRank().equals(ranks[i])) {
					card.rankValue = values[i];
				}
			}
		}
	}

	/**
	 * draws the top card in the deck and removes it
	 *
	 * @return the top card
	 */
	public Card draw() {
		return draw(0);
	}

	/**
	 * draws the card at position 'index' in the deck and removes it
	 *
	 * @param index index of card extracting from the deck - must be valid for the list
	 * @return the card at position 'index'
	 */
	public Card draw(int index) {
		Card drawnCard = this.get(index);
		this.remove(index);
		return drawnCard;
	}
}
