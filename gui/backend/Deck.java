package gui.backend;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<BaseCard> {
	
	public Deck() {
		String[] ranks = new String[]{"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
		String[] suits = new String[] {"Hearts", "Spades", "Clubs", "Diamonds"};
		for (String rank : ranks) {
			for (String suit : suits) {
				this.add(new BaseCard(rank, suit));
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
	
	public void moveToBack(BaseCard card) {
		if (!this.contains(card)) {
			throw new IndexOutOfBoundsException("Card is not in the deck.");
		}
	}
}
