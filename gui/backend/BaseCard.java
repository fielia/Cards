package gui.backend;

public class BaseCard {
	private static final String BACK_IMAGE = "![](./assets/card_images/back.png)";
	private final String frontImage;
	private final String rank;
	private final String suit;
	private boolean revealed;

	/**
	 * creates card image
	 * @param rank rank of card
	 * @param suit suit of card
	 */
	public BaseCard(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
		this.frontImage = "![](./assets/card_images/" + suit.toLowerCase() + "_" + rank.toLowerCase() + ".png)";
		this.revealed = false;
	}
	
	/**
	 * @return card image address
	 */
	public String getImage() {
		if (revealed) {
			return frontImage;
		} else {
			return BACK_IMAGE;
		}
	}
	
	/**
	 * @return card rank
	 */
	public String getRank() {
		return rank;
	}
	
	/**
	 * @return card suit
	 */
	public String getSuit() {
		return suit;
	}
	
	/**
	 * @return if the card is face-up
	 */
	public boolean isRevealed() {
		return revealed;
	}
	
	/**
	 * @param revealed whether the card should be face up or not
	 */
	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
	}
}
