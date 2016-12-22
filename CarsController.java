import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class CarsController extends JPanel implements MouseListener, KeyListener, ActionListener{

	static final int STILL = 0, RIGHT = 1, LEFT = 2;
	static int currentDirection = STILL;

	int score;
	
	private CarsModel model;
	private static CarsView view;
	private static Timer updatePositionsTimer;
	private static Timer tankTimer;
	private static Timer invincibilityTimer;

	//Adds Listeners and has Timers
	public CarsController(CarsModel model, final CarsView view) {
		this.model = model;
		this.view = view;
		view.addKeyListener(this);
		view.addMouseListener(this);
		
		//Updates the positions of the background, tanks, and changes the score and the number of invincibility bursts
		updatePositionsTimer = new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CarsView.moveBackground();
				CarsModel.changeScore();
				CarsModel.moveTanks();
				CarsModel.getScore();
				CarsModel.addInvincibility();
				view.repaint();
			}
		});
		//adds tanks, and the timer runs faster each time so that the tanks spawn more rapidly as the game goes on
		tankTimer = new Timer(800, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CarsModel.addTank();
				view.repaint();
				if(tankTimer.getDelay() > 300){
					tankTimer.setDelay(tankTimer.getDelay() - 10);
				}
				if(CarsModel.getScore() > 1000){
					if(tankTimer.getDelay() <= 300 && tankTimer.getDelay() > 250){
						tankTimer.setDelay(tankTimer.getDelay() - 4);
					}
				}
				
			}
		});			
		//makes the car invincible for a period of time
		invincibilityTimer = new Timer(1300, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CarsModel.invincibility = false;
				view.repaint();
			}
		});			
		
		

	}
	//resets the timers when the game ends
	public static void reset(){
		if((updatePositionsTimer != null) || (tankTimer != null)){
			updatePositionsTimer.stop();
			tankTimer.stop();
			view.repaint();
		}
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent ev) {
		//When the car isn't crashed, the left and right arrow keys move the car and the up arrow key gives invincibility
		if(!CarsModel.crash){
			if(ev.getKeyCode() == KeyEvent.VK_LEFT){
				model.moveCarLeft();
				view.repaint();
			}
			else if(ev.getKeyCode() == KeyEvent.VK_RIGHT){
				model.moveCarRight();
				view.repaint();
			}
			else if(ev.getKeyCode() == KeyEvent.VK_UP){
				if(CarsModel.numOfInvincibilities > 0){
					CarsModel.invincibility = true;
					invincibilityTimer.restart();
					CarsModel.numOfInvincibilities--;
					view.repaint();
				}
			}
		}
		//If the car is crashed the spacebar restarts the game
		if(CarsModel.crash){
			if(CarsModel.numOfLives != 0){
				if(ev.getKeyCode() == KeyEvent.VK_SPACE){
					CarsModel.crash = false;
					CarsModel.firstPlay = false;
					updatePositionsTimer.start();
					tankTimer.start();	
						for(int i = 0; i < CarsModel.tanks.size(); i++){
							CarsModel.tanks.clear();
						}
				}
				view.repaint();
			}
			else if(CarsModel.numOfLives == 0){
				if(ev.getKeyCode() == KeyEvent.VK_SPACE){
				view.repaint();
				CarsModel.crash = false;
				CarsModel.firstPlay = false;
				CarsModel.numOfInvincibilities = 2;
				CarsModel.numOfLives = 3;
				CarsModel.score = 0;
				model = new CarsModel(view);
				view.repaint();
				updatePositionsTimer.start();
				tankTimer.setDelay(800);
				tankTimer.start();
				Sound.PLAYMUSIC.loop();
					for(int i = 0; i < CarsModel.tanks.size(); i++){
						CarsModel.tanks.clear();
					}
				}	
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
