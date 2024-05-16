package text.backend;

public class Card {

	public enum Suit {
		SPADES,
		HEARTS,
		DIAMONDS,
		CLUBS
	}

	private enum Color {
		RED,
		BLACK
	}

	/**
	 * card's suit
	 */
	private final Suit suit;
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
	 * the color (red or black) of the card
	 */
	private Color color;

	/**
	 * creates a card object
	 *
	 * @param rank rank of the card
	 * @param suit suit of the card
	 */
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = switch (suit) {
			case "Hearts" -> Suit.HEARTS;
			case "Diamonds" -> Suit.DIAMONDS;
			case "Spades" -> Suit.SPADES;
			case "Clubs" -> Suit.CLUBS;
			default -> null;
		};
		this.revealed = false;
		this.counted = false;
		this.rankValue = switch (rank) {
			case "Jack" -> 11;
			case "Queen" -> 12;
			case "King" -> 13;
			case "Ace" -> 14;
			default -> Integer.parseInt(rank);
		};
		this.color = switch (suit) {
			case "Hearts" -> Color.RED;
			case "Diamonds" -> Color.RED;
			case "Spades" -> Color.BLACK;
			case "Clubs" -> Color.BLACK;
			default -> null;
		};
	}

	/**
	 * @return the name of the card
	 */
	public String toString() {
		String suit = switch (this.suit) {
			case HEARTS -> "Hearts";
			case DIAMONDS -> "Diamonds";
			case SPADES -> "Spades";
			case CLUBS -> "Clubs";
			default -> null;
		};
		return this.rank + " of " + suit;
	}

	/**
	 * @return card's suit
	 */
	public Suit getSuit() {
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

	/**
	 * @return color of the card
	 */
	public Color getColor() {
		return color;
	}
}
