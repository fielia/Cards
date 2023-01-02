package gui.backend;

public class BaseCard {
	private static final String BACK_IMAGE = "![](../backend/assets/card_images/back.png)";
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
		this.frontImage = "![](../backend/assets/card_images/" + suit.toLowerCase() + "_" + rank.toLowerCase() + ".png)";
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
	
	public String getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public boolean isRevealed() {
		return revealed;
	}
	
	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
	}
}
