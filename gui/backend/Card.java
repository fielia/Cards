//taken from http://www.fredosaurus.com/notes-java/examples/graphics/cardDemo/cardDemo.html

package gui.backend;

import java.awt.*;
import javax.swing.*;

class Card {
	private final ImageIcon image;
	private int x;
	private int y;
	
	public Card(ImageIcon image) {
		this.image = image;
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean contains(int x, int y) {
		return (x > this.x && x < (this.x + getWidth()) &&
				        y > this.y && y < (this.y + getHeight()));
	}
	
	public int getWidth() {
		return image.getIconWidth();
	}
	
	public int getHeight() {
		return image.getIconHeight();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void draw(Graphics g, Component c) {
		image.paintIcon(c, g, x, y);
	}
}
