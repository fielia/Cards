package text.backend;

public class Card {

	/**
	 * card's suit
	 */
	private final String suit;
	/**
	 * card's rank
	 */
	private final String rank;
	/**
	 * the value of the card's rank
	 */
	int rankValue;
	/**
	 * if the card has been revealed (only used in Trash)
	 */
	private boolean revealed;
	/**
	 * if the card has been used to count matched (only used in Seven-Eight)
	 */
	private boolean counted;

	/**
	 * creates a card object
	 *
	 * @param rank rank of the card
	 * @param suit suit of the card
	 */
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
		this.revealed = false;
		this.counted = false;
		this.rankValue = switch (rank) {
			case "Jack" -> 11;
			case "Queen" -> 12;
			case "King" -> 13;
			case "Ace" -> 14;
			default -> Integer.parseInt(rank);
		};
	}

	/**
	 * @return the name of the card
	 */
	public String toString() {
		return this.rank + " of " + this.suit;
	}

	/**
	 * @return card's suit
	 */
	public String getSuit() {
		return suit;
	}

	/**
	 * @return card's rank
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * @return card's rank value
	 */
	public int getRankValue() {
		return rankValue;
	}

	/**
	 * @return if the card has been revealed
	 */
	public boolean isRevealed() {
		return revealed;
	}

	/**
	 * @param revealed new value for revealed
	 */
	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
	}

	/**
	 * @return if this card has been counted
	 */
	public boolean isCounted() {
		return counted;
	}

	/**
	 * @param counted new value for counted
	 */
	public void setCounted(boolean counted) {
		this.counted = counted;
	}
}
