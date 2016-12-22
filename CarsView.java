import highscores.HighscoreManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CarsView extends JFrame implements ActionListener{
	
	private static CarsPanel carsPanel;
	
	private int screenHeight = 800;
	private int screenWidth = 480;
	Image carImage;
	Image tankImage;
	Image roadImage;
	Image explosionImage;
	Image shieldImage;
	
	CarsModel model;
	CarsModel carXPosition;
	CarsModel carYPosition;
	Tanks tankXPosition;
	Tanks tankYPosition;
	ArrayList<Tanks> tanks = new ArrayList<Tanks>();;
	
	static int Ybackground = 0;
	
	HighscoreManager hm = new HighscoreManager();

	public CarsView() {
		
		//mainPanel Setup
		super("Tanks");
		carsPanel = new CarsPanel();
		JPanel mainPanel = new JPanel();
		this.setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(carsPanel, BorderLayout.CENTER);
		
		setSize(screenWidth, screenHeight);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setResizable(false);
		
		 JPanel controlPanel = new JPanel();
			
		 try{
			 carImage = ImageIO.read(getClass().getResource("car.gif"));
		 } catch(IOException e){
			 System.out.print("ERROR");
		 }
		 
		 try{
			 tankImage = ImageIO.read(getClass().getResource("tank.gif"));
		 } catch(IOException e){
			 System.out.print("ERROR");
		 }
		 
		 try{
			 roadImage = ImageIO.read(getClass().getResource("road.png"));
		 } catch(IOException e){
			 System.out.print("ERROR");
		 }
		 
		 try{
			 explosionImage = ImageIO.read(getClass().getResource("explosion.gif"));
		 } catch(IOException e){
			 System.out.print("ERROR");
		 }
		 
		 try{
			 shieldImage = ImageIO.read(getClass().getResource("shield.gif"));
		 } catch(IOException e){
			 System.out.print("ERROR");
		 }
		 
		 
	}
	//moves the background down, and if it gets above 800, restart the image from the top.
	public static void moveBackground(){
		Ybackground += 20;
		if(Ybackground >= 800){
			Ybackground = 0;
		}
		carsPanel.repaint();
	}
	
	public class CarsPanel extends JPanel{

//		public void setDoubleBuffered(boolean aFlag){
//			aFlag = true;
//		}
//		
		@Override
		 public void paintComponent(Graphics g) {
			 super.paintComponent(g);
			 
			 //draws road
			 g.drawImage(roadImage, 0, Ybackground, null);
			 g.drawImage(roadImage, 0, Ybackground - 900, null);
			 
			 //draws tanks
			 for(int i = 0; i < model.tanks.size(); i++){
				model.tanks.get(i).draw(g);
		   	 }
			 
			 //draws car
			 g.drawImage(carImage, carXPosition.carX, carYPosition.carY, null);
			 
			 if(CarsModel.invincibility){
				 g.drawImage(shieldImage, carXPosition.carX - 17, carYPosition.carY - 20, null);
			 }
			 

			 
			 //sets color and font and draws the score and bursts of invincibility at the top of the screen
			 g.setColor(Color.black);
			 g.setFont(new Font("comicsans", Font.BOLD, 40));
			 g.drawString("Score: " + Integer.toString(CarsModel.score), 10, 40);
			 g.setFont(new Font("comicsans", Font.BOLD, 17));
			 g.drawString("Bursts of Invincibility: " + CarsModel.numOfInvincibilities, 10, 70);
			 g.drawString("Lives: " + CarsModel.numOfLives, 10, 100);
			 carsPanel.repaint();
			 
			 //If it is the first play, draw press spacebar to begin and show the controls
			 if(CarsModel.firstPlay){
				 g.setColor(Color.GREEN);
				 g.setFont(new Font("comicsans", Font.BOLD, 30));
				 g.drawString("Press spacebar for new game", 12, 300);
				 g.setFont(new Font("comicsans", Font.BOLD, 15));
				 g.drawString("Left arrow to move left", 140, 340);
				 g.drawString("Right arrow to move right", 138, 360);
				 g.drawString("Up arrow to use an invincibility burst ", 90, 380);
				 g.drawString("Every 500 points you get another Burst of Invincibility", 38, 420);
				 Sound.PLAYMUSIC.loop();
			 }
			 //If not the first play and crash is true then show game over, score, and press spacebar to play again.
			 if(!CarsModel.firstPlay){
				if(CarsModel.crash){
					if(CarsModel.numOfLives != 0){
						 g.setColor(Color.red);
						 g.setFont(new Font("comicsans", Font.BOLD, 25));
						 g.drawString("Press Spacebar to play next life", 50, 300);
					}
					else if(CarsModel.numOfLives == 0){
						 g.setFont(new Font("comicsans", Font.BOLD, 30));
						 g.setColor(Color.red);
						 g.drawString("GAME OVER", 140, 200);
						 g.setColor(Color.green);
						 g.drawString("Your Final Score: " + Integer.toString(CarsModel.score), 80, 300);
						 g.drawString("Press Spacebar for New Game", 15, 350);
						 
						 //add score to high score file and print the high score
						 g.setColor(Color.orange);
						 hm.addScore("Matt",CarsModel.score);
						 g.drawString("High Score: " + hm.getHighscoreString(), 110, 450);
						 Sound.PLAYMUSIC.stop();
					}
					g.drawImage(explosionImage, carXPosition.carX - 15, carYPosition.carY - 5, null);
					carsPanel.repaint();
				}

			 }			
		 }
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
