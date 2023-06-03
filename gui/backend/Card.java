//taken from http://www.fredosaurus.com/notes-java/examples/graphics/cardDemo/cardDemo.html

package gui.backend;

import java.awt.*;
import javax.swing.*;

public class Card extends BaseCard {
	private final ImageIcon image;
	private int x;
	private int y;
	int rankValue;
	boolean counted;
	
	/**
	 * creates a card object
	 *
	 * @param rank rank of the card
	 * @param suit suit of the card
	 */
	public Card(String rank, String suit) {
		super(rank, suit);
		this.image = new ImageIcon(this.getImage());
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
	 * moves the card to new coordinates
	 * @param x x coordinate of the new position
	 * @param y y coordinate of the new position
	 */
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * calculates if a set of coordinates is within the card
	 * @param x x coordinate of position in question
	 * @param y y coordinate of position in question
	 * @return whether the position is overlapping the card
	 */
	public boolean contains(int x, int y) {
		return (x > this.x && x < (this.x + getWidth()) && y > this.y && y < (this.y + getHeight()));
	}
	
	/**
	 * @return the width of the card
	 */
	public int getWidth() {
		return image.getIconWidth();
	}
	
	/**
	 * @return the height of the card
	 */
	public int getHeight() {
		return image.getIconHeight();
	}

	/**
	 * @return card's rank value
	 */
	public int getRankValue() {
		return rankValue;
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
	 * @return x coordinate of the card
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return y coordinate of the card
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * draws the card
	 * @param g idk
	 * @param c component to draw on
	 */
	public void draw(Graphics g, Component c) {
		image.paintIcon(c, g, x, y);
	}
}
