// taken from http://www.fredosaurus.com/notes-java/examples/graphics/cardDemo/cardDemo.html

package gui.backend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;

public class CardTable extends JComponent implements MouseListener, MouseMotionListener {

	private static final Color backgroundColor = Color.GREEN;
	private static final int tableSize = 400;

	private final ArrayList<Card> deck;

	private int dragFromX = 0;
	private int dragFromY = 0;

	private Card currentCard = null;

	public CardTable(ArrayList<Card> deck) {
		this.deck = deck;

		setPreferredSize(new Dimension(tableSize, tableSize));
		setBackground(Color.blue);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);

		// ... Display the cards, starting with the first array element.
		// The array order defines the z-axis depth.
		for (Card c : deck) {
			c.draw(g, this);
		}
	}

	/**
	 * Check to see if press is within any card.
	 * 
	 * @param e the event to be processed
	 */
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		currentCard = null;
		for (int card = deck.size() - 1; card >= 0; card--) {
			Card testCard = deck.get(card);
			if (testCard.contains(x, y)) {
				dragFromX = x - testCard.getX();
				dragFromY = x - testCard.getY();
				currentCard = testCard;
				break;
			}
		}
	}

	/**
	 * Set x,y to mouse position and repaint.
	 * 
	 * @param e the event to be processed
	 */
	public void mouseDragged(MouseEvent e) {
		if (currentCard != null) {

			int newX = e.getX() - dragFromX;
			int newY = e.getY() - dragFromY;
			newX = Math.max(newX, 0);
			newX = Math.min(newX, getWidth() - currentCard.getWidth());
			newY = Math.max(newY, 0);
			newY = Math.min(newY, getHeight() - currentCard.getHeight());

			currentCard.moveTo(newX, newY);

			this.repaint();
		}
	}

	/**
	 * Turn off dragging if mouse exits panel.
	 * 
	 * @param e the event to be processed
	 */
	public void mouseExited(MouseEvent e) {
		currentCard = null;
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
