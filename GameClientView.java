import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class GameClientView extends JFrame{

	private int screenHeight = 600;
	private int screenWidth = 600;
	
	private GameBoardPanel gamePanel;
	GameBoardPanel messagePanel;
	XorOButton[][] buttons;
	JLabel label;
	JPanel labelPanel;
	JPanel mainPanel;
	GameClientModel model;
	GameClientController controller;
	
	static int Ybackground = 0;
	
			
	public GameClientView() {
		
		//mainPanel Setup
		super("TRICKY TILES GAME");
		buttons = new XorOButton[5][5];
		mainPanel = new JPanel();
		this.setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		setSize(screenWidth, screenHeight);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setResizable(false);
		
		//Top Label
		labelPanel = new JPanel();
		label = new JLabel("WELCOME TO THE GAMEZONE!");
		labelPanel.add(label);
		mainPanel.add(labelPanel, BorderLayout.NORTH);
		
		gamePanel = new GameBoardPanel();
		gamePanel.setLayout(new GridLayout(5,5));

		//Sets up game for the first time
		SetUpGame();

		//Adds gamePanel to mainPanel
		mainPanel.add(gamePanel, BorderLayout.CENTER);	
	}
	
	//Adds action listeners to the buttons
	public void addActionListener(ActionListener listener){
	
		for(int i = 0; i< 5; i++){
			for(int j = 0; j < 5; j++){
				buttons[i][j].addActionListener(listener);
			}
		}
	}

	
	//Sets up/Restarts game
	public void SetUpGame(){
		for(int i = 0; i< buttons.length; i++){
			for(int j = 0; j < buttons.length; j++){
				XorOButton button = new XorOButton("-");
				button.setFont(new Font("Apple Chancery", Font.PLAIN, 40));
				button.setXYpos(i,j);
				buttons[i][j] = button;
				gamePanel.add(button);
				button.disable();
				button.hide();
			}
		}
	}
	
	public class GameBoardPanel extends JPanel{
		
		int count = 0;
		
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g); 
			if(model == null || model.XO == null){
				
				for(int i = 0; i < buttons.length; i++){
					for(int j = 0; j < buttons.length; j++){
						buttons[i][j].hide();
					}
				}
				g.setColor(Color.ORANGE);
				g.fillRect(0, 0, 600, 600);
				
				g.setFont(new Font("Impact", Font.BOLD, 100));
				g.setColor(Color.RED);
				g.drawString("WAITING", 140, 150);
				g.drawString("FOR", 225, 275);
				g.drawString("CHALLENGER", 60, 400);
				return;
			}
			g.setColor(Color.ORANGE);
			g.fillRect(0, 0, 600, 600);

			for(int i = 0; i < buttons.length; i++){
				for(int j = 0; j < buttons.length; j++){
					buttons[i][j].enable();
					buttons[i][j].show();
				}
			}
			
			for(int i = 0; i < buttons.length; i++){
				for(int j = 0; j < buttons.length; j++){
					if(model.XO[i][j] == 'X'){
						buttons[i][j].setText("X");
						buttons[i][j].setForeground(Color.BLUE);
						//buttons[i][j].setBackground(Color.CYAN);
					}
					if(model.XO[i][j] == 'O'){
						buttons[i][j].setText("O");
						buttons[i][j].setForeground(Color.MAGENTA);
						//buttons[i][j].setBackground(Color.RED);
					}
					if(model.XO[i][j] == ' '){
						buttons[i][j].setText("-");
						buttons[i][j].setForeground(Color.LIGHT_GRAY);
						//buttons[i][j].setBackground(Color.PINK);
						count++;
						if(count == 25){
							popUp();
						}
					}
				}
			}
		}
		
		public void popUp(){
			 JOptionPane.showMessageDialog( mainPanel, "Rules of the Game: \n - Get 4 in a row anywhere to win " + "\n - Get all 4 corners to win "
			 		+ "\n - Get 3 in a row from the middle of an outside row to the " + "\n    middle of another outside row (diagonally) to win");
		}
	}

	public void setMessageText(String string) {
		label.setText(string);
		labelPanel.repaint();	
	}
	
    public void setModel(GameClientModel model) {
		   this.model = model;
    }




	

}
