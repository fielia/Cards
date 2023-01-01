package gui.backend;

public class BaseCard {
	private static final String BACK_IMAGE = "![](assets/card_images/back.png)";
	private final String frontImage;
	private final String rank;
	private final String suit;
	private boolean revealed;

	public BaseCard(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
		this.frontImage = "![](assets/card_images/" + suit.toLowerCase() + "_" + rank.toLowerCase() + ".png)";
		this.revealed = true;
	}
	
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
