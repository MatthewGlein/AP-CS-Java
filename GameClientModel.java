import java.awt.Dimension;

import java.awt.Toolkit;
import java.io.Serializable;


public class GameClientModel implements Serializable{

	public char[][] XO;
	private  GameClientView view;
	public int idPlayer1;
	public int idPlayer2;
	public int idCurrentPlayer;
	public int idWinner; 
	public boolean gameInProgress = true; 
	public boolean gameEndedInTie;
	
	public GameClientModel(int idPlayer1, int idPlayer2) {
		this.idPlayer1 = idPlayer1;
		this.idPlayer2 = idPlayer2;
		SetUpGame();
	}

	public boolean isTie(){
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if(XO[i][j] == 'X' || XO[i][j] == 'O'){
					continue;
				}
				else return false;
			}
		}
		return true;
	}

	// Puts X or O into the Array to keep track of the game
	public boolean PlaceObject(int X, int Y){
		if(XO[X][Y] != ' ' || gameInProgress == false){
			return false;
		}
		XO[X][Y] = (idCurrentPlayer == idPlayer1) ? 'X' : 'O'; 
		if (CheckWinner()) {
			gameInProgress = false;
			idWinner = idCurrentPlayer;
		}
		else if (isTie()) { 
			gameInProgress = false;
			gameEndedInTie = true;
		}
		else { 
			idCurrentPlayer = (idCurrentPlayer == idPlayer1) ? idPlayer2: idPlayer1;
		}
		return true;
		
	}
	
	
	// Puts the XO array to blank
	public void SetUpGame() {
		XO = new char[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				XO[i][j] = ' ';
			}
		}
		// randomly select first player
		if (Math.random() < 0.5) 
			idCurrentPlayer = idPlayer1;
		else 
			idCurrentPlayer = idPlayer2;
		gameEndedInTie = false;
		idWinner = -1;
		gameInProgress = true;
	}
	
	//Checks if Player 1 won
	public boolean CheckWinner(){
		if(		//Vertical 4 in a row
				(XO[0][0] != ' ' && (XO[0][0] == XO[1][0] && XO[1][0] == XO[2][0] && XO[2][0] == XO[3][0])) ||
				(XO[1][0] != ' ' && (XO[1][0] == XO[2][0] && XO[2][0] == XO[3][0] && XO[3][0] == XO[4][0])) ||
				(XO[1][1] != ' ' && (XO[0][1] == XO[1][1] && XO[1][1] == XO[2][1] && XO[2][1] == XO[3][1])) ||
				(XO[1][1] != ' ' && (XO[1][1] == XO[2][1] && XO[2][1] == XO[3][1] && XO[3][1] == XO[4][1])) ||
				(XO[0][2] != ' ' && (XO[0][2] == XO[1][2] && XO[1][2] == XO[2][2] && XO[2][2] == XO[3][2])) ||
				(XO[1][2] != ' ' && (XO[1][2] == XO[2][2] && XO[2][2] == XO[3][2] && XO[3][2] == XO[4][2])) ||
				(XO[0][3] != ' ' && (XO[0][3] == XO[1][3] && XO[1][3] == XO[2][3] && XO[2][3] == XO[3][3])) ||
				(XO[1][3] != ' ' && (XO[1][3] == XO[2][3] && XO[2][3] == XO[3][3] && XO[3][3] == XO[4][3])) ||
				(XO[0][4] != ' ' && (XO[0][4] == XO[1][4] && XO[1][4] == XO[2][4] && XO[2][4] == XO[3][4])) ||
				(XO[1][4] != ' ' && (XO[1][4] == XO[2][4] && XO[2][4] == XO[3][4] && XO[3][4] == XO[4][4])) ||
				
				//Horizontal 4 in a row
				(XO[0][0] != ' ' && (XO[0][0] == XO[0][1] && XO[0][1] == XO[0][2] && XO[0][2] == XO[0][3])) ||
				(XO[0][1] != ' ' && (XO[0][1] == XO[0][2] && XO[0][2] == XO[0][3] && XO[0][3] == XO[0][4])) ||
				(XO[1][0] != ' ' && (XO[1][0] == XO[1][1] && XO[1][1] == XO[1][2] && XO[1][2] == XO[1][3])) ||
				(XO[1][1] != ' ' && (XO[1][1] == XO[1][2] && XO[1][2] == XO[1][3] && XO[1][3] == XO[1][4])) ||
				(XO[2][0] != ' ' && (XO[2][0] == XO[2][1] && XO[2][1] == XO[2][2] && XO[2][2] == XO[2][3])) ||
				(XO[2][1] != ' ' && (XO[2][1] == XO[2][2] && XO[2][2] == XO[2][3] && XO[2][3] == XO[2][4])) ||
				(XO[3][0] != ' ' && (XO[3][0] == XO[3][1] && XO[3][1] == XO[3][2] && XO[3][2] == XO[3][3])) ||
				(XO[3][1] != ' ' && (XO[3][1] == XO[3][2] && XO[3][2] == XO[3][3] && XO[3][3] == XO[3][4])) ||
				(XO[4][0] != ' ' && (XO[4][0] == XO[4][1] && XO[4][1] == XO[4][2] && XO[4][2] == XO[4][3])) ||
				(XO[4][1] != ' ' && (XO[4][1] == XO[4][2] && XO[4][2] == XO[4][3] && XO[4][3] == XO[4][4])) ||
				
				//Diagnals 4 in a row
				(XO[0][0] != ' ' && (XO[0][0] == XO[1][1] && XO[1][1] == XO[2][2] && XO[2][2] == XO[3][3])) ||
				(XO[1][1] != ' ' && (XO[1][1] == XO[2][2] && XO[2][2] == XO[3][3] && XO[3][3] == XO[4][4])) ||
				(XO[4][0] != ' ' && (XO[4][0] == XO[3][1] && XO[3][1] == XO[2][2] && XO[2][2] == XO[1][3])) ||
				(XO[3][1] != ' ' && (XO[3][1] == XO[2][2] && XO[2][2] == XO[1][3] && XO[1][3] == XO[0][4])) ||
				(XO[3][0] != ' ' && (XO[3][0] == XO[2][1] && XO[2][1] == XO[1][2] && XO[1][2] == XO[0][3])) ||
				(XO[4][1] != ' ' && (XO[4][1] == XO[3][2] && XO[3][2] == XO[2][3] && XO[2][3] == XO[1][4])) ||
				(XO[1][0] != ' ' && (XO[1][0] == XO[2][1] && XO[2][1] == XO[3][2] && XO[3][2] == XO[4][3])) ||
				(XO[0][1] != ' ' && (XO[0][1] == XO[1][2] && XO[1][2] == XO[2][3] && XO[2][3] == XO[3][4])) ||
				
				//Three in a row
				(XO[2][0] != ' ' && (XO[2][0] == XO[1][1] && XO[1][1] == XO[0][2])) ||
				(XO[0][2] != ' ' && (XO[0][2] == XO[1][3] && XO[1][3] == XO[2][4])) ||
				(XO[2][4] != ' ' && (XO[2][4] == XO[3][3] && XO[3][3] == XO[4][2])) ||
				(XO[2][0] != ' ' && (XO[2][0] == XO[3][1] && XO[3][1] == XO[4][2])) ||
				
				(XO[0][0] != ' ' && (XO[0][0] == XO[4][0] && XO[4][0] == XO[0][4] && XO[0][4] == XO[4][4]))
			){
			System.out.println("YOU WIN");
			return true;
			
			}
		
		return false;
	}

	public int getOtherPlayerID(int playerID) {
		if (playerID == idPlayer1)
			return idPlayer2;
		else if (playerID == idPlayer2)
			return idPlayer1;
		else
			return -1;
	}
	
}
