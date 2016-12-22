import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Tanks{

	private int randomTankX = (int) (Math.random()*4);
	private int tankX;
	private int tankY;
	Image tankImage;
	
	//How to make tanks, the X is random
	public Tanks(){
		this.tankX = tankX(randomTankX); 
		this.tankY = 0;
		 try{
			 tankImage = ImageIO.read(getClass().getResource("tank.gif"));
		 } catch(IOException e){
			 System.out.print("ERROR");
		 }
	}
	//Draws the tanks
	public void draw(Graphics g){
		 g.drawImage(tankImage, tankX, tankY, null);
	}
	//Moves the tanks down, and increases the speed of the tanks as the score increases
	public void moveTankDown(){
			tankY += 10;
			if(CarsModel.score > 100 && CarsModel.score < 300){
				tankY += 10;
			}
			if(CarsModel.score > 300 && CarsModel.score < 600){
				tankY += 15;
			}
			if(CarsModel.score > 600 && CarsModel.score < 900){
				tankY += 20;
			}
			if(CarsModel.score > 900 && CarsModel.score < 1200){
				tankY += 30;
			}
			if(CarsModel.score > 1200){
				tankY += 35;
			}
	}
	//Helps make the tanks have a random X, it differs between 4 points so it always spawns in a lane
	public static int tankX( int randomTankX){
		switch(randomTankX){
			case 0: 
				return 30;
			case 1:
				return 150;
			case 2:
				return 270;
			case 3: 
				return 390;
		}
		return 0;

	}
	//Returns the tank's X
	public int getXTank(){
		return tankX;
	}
	//Returns the tank's Y
	public int getYTank(){
		return tankY;
	}
}
