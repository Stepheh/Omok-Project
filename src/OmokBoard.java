
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


public class OmokBoard implements OmokState, ActionListener{
	
	protected int size = 0;
	protected JPanel board;
	private JButton[][] buttons;
	private Pieces[][] states;
	
	private int numPieces;
	private int player = 0;
	
	public OmokBoard() {
		this(19);
	}
	
	public OmokBoard(int size) {
		this.size = size;
		numPieces = 0;
		buttons = new JButton[size][size];
		states = new Pieces[size][size];
		initializeGui();
	}
	
	/**
	 * This function will initialize the gui of the board.
	 */
	private final void initializeGui() {
		menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));

		start.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		exit.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		
		// Center every component of the main menu
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		start.addActionListener(this);
		exit.addActionListener(this);
		
		// Add empty space between each component for AESTHETICS
		menu.add(Box.createRigidArea(new Dimension(0, 30)));
		menu.add(start);
		menu.add(Box.createRigidArea(new Dimension(0, 30)));
		menu.add(exit);
		
		gui.add(menu);
	}
	
	/**
	 * This method will create and initialize the board.
	 */
	private final void createBoard() {
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.PAGE_START);

		board = new JPanel(new GridLayout(size, size));
		board.setBorder(new LineBorder(Color.BLACK));
		
		// Create the board squares
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				ImageIcon icon = new ImageIcon("images/icon.png");
				JButton b = new JButton(icon);
				b.setMargin(buttonMargin);
				b.setPreferredSize(new Dimension(30, 30));
				b.setOpaque(true);
				b.setBorderPainted(false);
				
				buttons[row][col] = b;
				states[row][col] = Pieces.EMPTY;
			}
		}
		
		// Fill the board
		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				board.add(buttons[row][col]);
			}
		}
		// Set ActionListeners to all buttons.
		for (int row = 0; row < size; ++row) {
			for (int col = 0; col < size; ++col) {
				buttons[row][col].addActionListener(this);
			}
		}
		gui.add(board);
		board.setBorder(new EmptyBorder(10,10,10,10));
	}
	
	public final JComponent getBoard() {
		return board;
	}
	
	public void actionPerformed(ActionEvent e) {
		Object b = e.getSource();
		
		if (b == start) {
			frame.getContentPane().removeAll();
			createBoard();
			frame.getContentPane().add(getBoard());
			frame.pack();
			frame.revalidate();
		} else if (b == exit) {
			System.exit(0);
		} else {		// Game detects a movement on board.
			int rows = 0;
			int cols = 0;
			// Find which button which was clicked
			for (int row = 0; row < size; ++row) {
				for (int col = 0; col < size; ++col) {
					if (buttons[row][col] == b) {
						rows = row;
						cols = col;
					}
				}
			}
			if (validMove(rows, cols)) {
				ImageIcon icon;
				Image img;
				Image newimg;
				switch(player) {
				case 1:
					icon = new ImageIcon("images/token1.png");
					img = icon.getImage() ;
					newimg = img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH ) ;  
					icon = new ImageIcon(newimg);
					
					buttons[rows][cols].setIcon(icon);
					states[rows][cols] = Pieces.BEAR;
					break;
				case 0:
					icon = new ImageIcon("images/token2.png");
					img = icon.getImage() ;
					newimg = img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH ) ;  
					icon = new ImageIcon(newimg);
					
					buttons[rows][cols].setIcon(icon);
					states[rows][cols] = Pieces.BUNNY;
					break;
				}
				if (checkStates(rows, cols, player)) {
					System.out.println("Entered state");
					//TODO: Display win message/board + end game board aka. would you like to play again?
					//Temporary message.
					if (player == 1)
						System.out.println("Bear won the game! WOOOHOO");
					else
						System.out.println("Bunny won the game! WOOHOO");
				}
				// Change the player.
				player = player - 1 == 0 ? 0 : 1;
			}
			
			System.out.println("Selected " + rows + " : " + cols);
		}
	}
	
	/**
	 * This function will be verifying if the move the player is attempting to
	 * play is valid or not.
	 * @param row represents the row of the board.
	 * @param col represents the column of the board.
	 */
	private boolean validMove(int row, int col) {
		if (states[row][col] == Pieces.EMPTY)
			return true;
		return false;
	}
	
	// TODO: Check if the board is a winning state or not.
	// Might need to generalize this.
	/**
	 *  This function will be verifying the current status of the game board
	 *  starting from the token the player entered.
	 *  It will return true if a player has won, and false otherwise.
	 * @param row The row the player chose.
	 * @param col The column the player chose.
	 * @return
	 */
	private boolean checkStates(int row, int col, int player) {
		if (VerticalWin(row, col, player)) {
			return true;
		} else if (HorizontalWin(row, col, player)) {
			return true;
		} //TODO: missing diagonals
		return false;
	}
	
	/**
	 * This function will check if there are five of the same tokens placed
	 * consecutively on the same column given the last move played.
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean VerticalWin(int row, int col, int player) {
		switch (player) {
		case 1:
			// Inserted token at position 0 of the column.
			if (row+1<size && row+2<size && row+3<size && row+4<size
				&& states[row][col] == Pieces.BEAR && states[row+1][col] == Pieces.BEAR
				&& states[row+2][col] == Pieces.BEAR && states[row+3][col] == Pieces.BEAR
				&& states[row+4][col] == Pieces.BEAR) {
					return true;
			}
			// Inserted token at position 1 of the column.
			else if (row-1>=0 && row+1<size && row+2<size && row+3<size
				&& states[row][col] == Pieces.BEAR && states[row-1][col] == Pieces.BEAR
				&& states[row+1][col] == Pieces.BEAR && states[row+2][col] == Pieces.BEAR
				&& states[row+3][col] == Pieces.BEAR) {
					return true;
			}
			// Inserted token at position 2 of the column.
			else if (row-1>=0 && row-2>=0 && row+1<size && row+2<size
				&& states[row][col] == Pieces.BEAR && states[row-1][col] == Pieces.BEAR
				&& states[row-2][col] == Pieces.BEAR && states[row+1][col] == Pieces.BEAR
				&& states[row+2][col] == Pieces.BEAR) {
					return true;
			}
			// Inserted token at position 3 of the column.
			else if (row-1>=0 && row-2>=0 && row-3>=0 && row+1<size
				&& states[row][col] == Pieces.BEAR && states[row-1][col] == Pieces.BEAR
				&& states[row-2][col] == Pieces.BEAR && states[row-3][col] == Pieces.BEAR
				&& states[row+1][col] == Pieces.BEAR) {
					return true;
			}
			// Inserted token at position 4 of the column.
			else if (row-1>=0 && row-2>=0 && row-3>=0 && row-4>=0
				&& states[row][col] == Pieces.BEAR && states[row-1][col] == Pieces.BEAR
				&& states[row-2][col] == Pieces.BEAR && states[row-3][col] == Pieces.BEAR
				&& states[row-4][col] == Pieces.BEAR) {
					return true;
			}
		case 0:
			// Inserted token at position 0 of the column.
			if (row+1<size && row+2<size && row+3<size && row+4<size
				&& states[row][col] == Pieces.BUNNY && states[row+1][col] == Pieces.BUNNY
				&& states[row+2][col] == Pieces.BUNNY && states[row+3][col] == Pieces.BUNNY
				&& states[row+4][col] == Pieces.BUNNY) {
					return true;
			}
			// Inserted token at position 1 of the column.
			else if (row-1>=0 && row+1<size && row+2<size && row+3<size
				&& states[row][col] == Pieces.BUNNY && states[row-1][col] == Pieces.BUNNY
				&& states[row+1][col] == Pieces.BUNNY && states[row+2][col] == Pieces.BUNNY
				&& states[row+3][col] == Pieces.BUNNY) {
					return true;
			}
			// Inserted token at position 2 of the column.
			else if (row-1>=0 && row-2>=0 && row+1<size && row+2<size
				&& states[row][col] == Pieces.BUNNY && states[row-1][col] == Pieces.BUNNY
				&& states[row-2][col] == Pieces.BUNNY && states[row+1][col] == Pieces.BUNNY
				&& states[row+2][col] == Pieces.BUNNY) {
					return true;
			}
			// Inserted token at position 3 of the column.
			else if (row-1>=0 && row-2>=0 && row-3>=0 && row+1<size
				&& states[row][col] == Pieces.BUNNY && states[row-1][col] == Pieces.BUNNY
				&& states[row-2][col] == Pieces.BUNNY && states[row-3][col] == Pieces.BUNNY
				&& states[row+1][col] == Pieces.BUNNY) {
					return true;
			}
			// Inserted token at position 4 of the column.
			else if (row-1>=0 && row-2>=0 && row-3>=0 && row-4>=0
				&& states[row][col] == Pieces.BUNNY && states[row-1][col] == Pieces.BUNNY
				&& states[row-2][col] == Pieces.BUNNY && states[row-3][col] == Pieces.BUNNY
				&& states[row-4][col] == Pieces.BUNNY) {
					return true;
			}
		}
		return false;
	}
	
	// TODO: Must generalize this function.
	/**
	 * This function will check if there are five of the same tokens placed
	 * consecutively on the same row given the last move played.
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean HorizontalWin(int row, int col, int player) {
		if (states[row][col] == Pieces.BEAR && states[row][col+1] == Pieces.BEAR
				&& states[row][col+2] == Pieces.BEAR && states[row][col+3] == Pieces.BEAR
				&& states[row][col+4] == Pieces.BEAR) {
			return true;
		} else if (states[row][col] == Pieces.BUNNY && states[row][col+1] == Pieces.BUNNY
				&& states[row][col+2] == Pieces.BUNNY && states[row][col+3] == Pieces.BUNNY
				&& states[row][col+4] == Pieces.BUNNY) {
			return true;
		}
		return false;
	}
	
	//TODO: Must generalize this section
	// O^n is too bad of a complexity.
	/**
	 * This function will check if there are five of the same tokens placed
	 * consecutively in a diagonal given the last move played.
	 * @return
	 */
	private boolean DiagonalWins(int row, int col, int player) {
		return false;
	}
	
	public final JComponent getGUI() {
		return gui;
	}
	
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("Game starts!");
				OmokBoard graph = new OmokBoard();
				
				frame.setTitle("Omok Game!");
				frame.add(graph.getGUI());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLocationByPlatform(true);

				frame.pack();
				// frame.setMinimumSize(frame.getSize());
				frame.setMinimumSize(new Dimension(400, 300));
				frame.setVisible(true);
			}
		};
		SwingUtilities.invokeLater(r);
	}
}
