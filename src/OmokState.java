import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *  
 * @author Stepheh
 *
 */
public interface OmokState {
	public enum Pieces {
		EMPTY, BUNNY, BEAR;
	}
	

	// Constants for button size
	public final int BUTTON_WIDTH = 250;
	public final int BUTTON_HEIGHT = 50;

	// Main panel
	public final JPanel gui = new JPanel(new BorderLayout(3, 3));
	
	// Main frame
	public final JFrame frame = new JFrame();
	
	public final JPanel menu = new JPanel();
	public final JButton start = new JButton("Start the game");
	public final JButton exit = new JButton("Oops, wrong game. Get me outta here!");
}
