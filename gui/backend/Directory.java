// taken from http://www.fredosaurus.com/notes-java/examples/graphics/cardDemo/cardDemo.html

package gui.backend;

import javax.swing.JFrame;

public class Directory extends JFrame {
	
	private static final Deck deck = new Deck();
	
	public static void main(String[] args) {
		Directory window = new Directory();
		window.setTitle("Card Demo 1");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new CardTable(deck));
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
