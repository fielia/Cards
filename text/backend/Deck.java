package text.backend;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {

	/**
	 * creates and shuffles a deck of cards, setting it to a list as this
	 */
	public Deck() {
		this(1);
	}

	/**
	 * creates and shuffles many decks of cards, setting it to a list as this
	 *
	 * @param decks the amount of decks to create
	 */
	public Deck(int decks) {
		String[] ranks = new String[] { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
		for (int i = 0; i < decks; i++) {
			for (String rank : ranks) {
				for (String suit : Game.suits) {
					this.add(new Card(rank, suit));
				}
			}
		}
		shuffle();
	}

	/**
	 * changes the value of each card of a certain rank
	 *
	 * @param ranks  list of ranks of cards to change
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
	 * method to shuffle the deck
	 */
	public void shuffle() {
		Collections.shuffle(this);
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
	 * @param index index of card extracting from the deck - must be valid for the
	 *              list
	 * @return the card at position 'index'
	 */
	public Card draw(int index) {
		Card drawnCard = this.get(index);
		this.remove(index);
		return drawnCard;
	}
}
