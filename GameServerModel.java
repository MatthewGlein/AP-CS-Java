import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class GameServerModel {

	private ConcurrentHashMap<Integer, GameClientModel> currentGames = new ConcurrentHashMap<Integer, GameClientModel>();
	private ConcurrentHashMap<Integer, PlayerProfile> playerProfiles = new ConcurrentHashMap<Integer, PlayerProfile>();
	private LinkedBlockingQueue<Integer> waitingPlayers = new LinkedBlockingQueue<Integer>(50);
	
	public void addPlayerToQueue(int playerID) throws InterruptedException {
		if (!playerProfiles.containsKey(playerID))
			playerProfiles.put(playerID, new PlayerProfile(playerID));
		currentGames.remove(playerID);
		try {
			waitingPlayers.put(playerID);
		}
		catch (InterruptedException e) {
			
		}
	}

	public GameClientModel startGame(int firstPlayer, int secondPlayer) {
		if (!playerProfiles.containsKey(firstPlayer))
			playerProfiles.put(firstPlayer, new PlayerProfile(firstPlayer));
		if (!playerProfiles.containsKey(secondPlayer))
			playerProfiles.put(secondPlayer, new PlayerProfile(secondPlayer));
		GameClientModel game = new GameClientModel(firstPlayer, secondPlayer);
		currentGames.put(firstPlayer, game);
		currentGames.put(secondPlayer, game);
		playerProfiles.get(firstPlayer).numGamesPlayed++;
		playerProfiles.get(secondPlayer).numGamesPlayed++;
		playerProfiles.get(firstPlayer).idOfMostRecentOpponent = secondPlayer;
		playerProfiles.get(secondPlayer).idOfMostRecentOpponent = firstPlayer;
		return game;
	}

	public boolean makeMove(int row, int col, int playerID) {
		GameClientModel game = currentGames.get(playerID);
		
		if (game == null)
			return false;
		
		if (!isPlayersTurn(playerID))
			return false;
		
		boolean LegalMove = game.PlaceObject(row, col);
		
		if (!LegalMove)
			return false;
		
		if (!game.gameInProgress) {
			if (game.CheckWinner())
				playerProfiles.get(playerID).numGamesWon++;
		}
		
		return true;
	}

	public boolean isWinner(int playerID) {
		GameClientModel game = currentGames.get(playerID);
		if (game == null)
			return false;
		return game.CheckWinner() && (playerID == game.idCurrentPlayer);
	}

	public void incrementsWins(int playerID) {
		if (!playerProfiles.containsKey(playerID))
			playerProfiles.put(playerID, new PlayerProfile(playerID));
		playerProfiles.get(playerID).numGamesWon++;
	}

	public boolean isTie(int playerID) {
		GameClientModel game = currentGames.get(playerID);
		if (game == null)
			return false;
		return game.isTie();
	}

	public boolean isPlayersTurn(int playerID) {
		GameClientModel game = currentGames.get(playerID);
		if (game == null)
			return false;
		return game.idCurrentPlayer == playerID;
	}

	public int getNextPlayer() throws InterruptedException {
		return waitingPlayers.take();
	}

	public int getMostRecentOpponent(int playerID) {
		if (!playerProfiles.containsKey(playerID))
			return -1;
		return playerProfiles.get(playerID).idOfMostRecentOpponent;
	}

	public void setPlayerName(int playerID, String name) {
		if (!playerProfiles.containsKey(playerID))
			playerProfiles.put(playerID, new PlayerProfile(playerID));
		playerProfiles.get(playerID).name = name;
	}

	public String getPlayerName(int playerID) {
		return playerProfiles.get(playerID).name;
	}

	public int getOpponent(int playerID) {
		GameClientModel game = currentGames.get(playerID);
		if (game != null)
			return game.getOtherPlayerID(playerID);
		return -1;
	}

	public GameClientModel getGame(int playerID) {
		return currentGames.get(playerID);
	}

	public void remove(int playerID) {
		GameClientModel game = currentGames.get(playerID);
		if (game != null && game.gameInProgress) {
			int opponentID = game.getOtherPlayerID(playerID);
			playerProfiles.get(opponentID).numGamesWon++;
			currentGames.remove(playerID);
			currentGames.remove(opponentID);
			waitingPlayers.add(opponentID);
		}
		playerProfiles.remove(playerID);
		waitingPlayers.remove(playerID);
	}

	private static class PlayerProfile {
		public int playerID;
		public int numGamesPlayed = 0;
		public int numGamesWon = 0;
		public int idOfMostRecentOpponent = -1;
		public String name = "";
		
		public PlayerProfile(int playerId) {
			this.playerID = playerID;
		}
	}
}
