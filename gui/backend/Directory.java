// taken from http://www.fredosaurus.com/notes-java/examples/graphics/cardDemo/cardDemo.html

package gui.backend;

import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;

public class Directory extends JFrame {
	
	private static final ArrayList<Card> deck = new ArrayList<>();
	
	public static void main(String[] args) {
		Directory window = new Directory();
		window.setTitle("Card Demo 1");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new CardTable(deck));
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	public Directory() {
		ClassLoader cldr = this.getClass().getClassLoader();
		
		int n = 0;
		int xPos = 0;
		int yPos = 0;
		
		String suits = "hscd";
		String faces = "a23456789tjqk";
		for (int suit = 0; suit < suits.length(); suit++) {
			for (int face = 0; face < faces.length(); face++) {
				String imagePath = "cards/images/" + faces.charAt(face) +
						                   suits.charAt(suit) + ".gif";
				URL imageURL = cldr.getResource(imagePath);
				assert imageURL != null;
				ImageIcon img = new ImageIcon(imageURL);
				
				//... Create a card and add it to the deck.
				Card card = new Card(img);
				card.moveTo(xPos, yPos);
				deck.set(n,card);
				
				//... Update local vars for next card.
				xPos += 5;
				yPos += 4;
				n++;
			}
		}
	}
}
