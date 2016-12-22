import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;


public class CarsModel {

	static int carY = 580;
	static int carX = 30;
	static int numOfInvincibilities = 2;
	static int numOfLives = 3;
	static ArrayList<Tanks> tanks = new ArrayList<Tanks>();
	
	private static CarsView view;
	public static int score = 0;
	
	private static Timer tankTimer;

	static boolean crash = true;
	static boolean firstPlay = true;
	static boolean invincibility;
	
	public CarsModel(final CarsView view){
		this.view = view;
	}
	//Moves car right if it isn't as far as it can go to the right
	public static void moveCarRight(){
		if(carX < 390){
			carX += 120;
		}
	}
	//Moves car left if it isn't as far as it can go to the left
	public static void moveCarLeft(){
		if(carX > 30){
			carX -= 120;
		}
	}
	//Returns the X of the car, used to see if the tank and car are crashing
	public static int getCarX(){
		return carX;
	}
	//Gets the Y of the car, used to see if the tank and car are crashing
	public static int getCarY(){
		return carY;
	}
	//Adds one to the score
	public static int changeScore(){
		return score += 1;
	}
	//Returns the score, mainly for the end game screen
	public static int getScore(){
		return score;
	}
	//Adds a tank to the tank array
	public static void addTank(){
		tanks.add(new Tanks());
	}
	//Moves all the tanks in the array down
	public static void moveTanks(){
		for(int i = 0; i < tanks.size(); i++){
			tanks.get(i).moveTankDown();
			//If a tank goes off the screen it gets removed
			if(tanks.get(i).getYTank() >= 800){
				tanks.remove(i);
			}
			//If the car is not invincible, it tests if the car and tank are crashing, 
			//and if they are it resets the timers and sets crash to be true
			if(!invincibility){
				if((tanks.get(i).getXTank()) == getCarX() && 
						tanks.get(i).getYTank() >= 480 &&
						tanks.get(i).getYTank() <= 685 ){
					CarsController.reset();
					crash = true;
					numOfLives--;
				}
			}
		}
	}
	//If the score if a multiple of 500 the player gets an extra jump
	public static void addInvincibility(){
		if(score % 500 == 0){
			numOfInvincibilities++;
		}
	}
	

}
