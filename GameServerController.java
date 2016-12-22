import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

import netgame.common.Hub;


public class GameServerController extends Hub implements Serializable{

	private GameServerModel model; // Records the state of the game.
	private HashSet<String> userNames = new HashSet<String>();
	private boolean running;

	public GameServerController(int port) throws IOException{
		super(port);
		model = new GameServerModel();
		setAutoreset(true);
		new Thread() {
			public void start() {
				running = true;
				while (running) {
					try {
						int firstPlayer = model.getNextPlayer();
						int secondPlayer = model.getNextPlayer();
						if (firstPlayer == model.getMostRecentOpponent(secondPlayer)) {
							int nextPlayer = model.getNextPlayer();
							model.addPlayerToQueue(secondPlayer);
							secondPlayer = nextPlayer;
							System.out.println("queue");
						}
						GameClientModel game = model.startGame(firstPlayer,secondPlayer);
						GameServerController.this.sendToOne(firstPlayer,game);
						GameServerController.this.sendToOne(secondPlayer,game);
					} catch (InterruptedException e) {

					}
				}
			}
		}.start();
	}

	protected void messageReceived(int playerID, Object message) {
		if (message instanceof int[]) {
			int[] move = (int[]) message;
			if (model.makeMove(move[0], move[1], playerID)) {
				GameClientModel game = model.getGame(playerID);
				int opponentID = model.getOpponent(playerID);
				this.sendToOne(playerID, game);
				this.sendToOne(opponentID, game);
			}
		}
		else {
			String command = (String) message;
			if (command.equals("newgame")) {
				try {
					this.sendToOne(playerID, "waiting");
					model.addPlayerToQueue(playerID);
				}
				catch (InterruptedException e) {
					
				}
			}
		}

	}

	protected void playerConnected(int playerID) {

	}

	protected void playerDisconnected(int playerID) {
		userNames.remove(model.getPlayerName(playerID));
		int opponentID = model.getOpponent(playerID);
		model.remove(playerID);
		this.sendToOne(opponentID, "waiting");

	}

	protected void extraHandshake(int playerID, ObjectInputStream in,
			ObjectOutputStream out) throws IOException {
		String name;
		try {
			name = (String) in.readObject();
			while (userNames.contains(name)) {
				out.writeObject(false);
				name = (String) in.readObject();
			}
			userNames.add(name);
			out.writeObject(true); // username was available
			model.addPlayerToQueue(playerID);
			model.setPlayerName(playerID, name);
		} catch (ClassNotFoundException e) {
			return;
		} catch (InterruptedException e) {
			return;
		}

	}

}
