import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import netgame.common.Client;

public class GameClientController extends JPanel implements MouseListener,
		KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;	//added to remove class warning message
	private GameClientModel model;
	private GameClientView view;
	private ClientConnection connection;
	private int myID;
	private String name;

	public GameClientController(String host, int port, final GameClientView view) {
		this.view = view;
		this.view.addActionListener(this);
		view.addKeyListener(this);
		view.addMouseListener(this);
		
		try {
			connection = new ClientConnection(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setMyID(connection.getID());
		
		setMyName(connection.getMyName());
		//System.out.println("GameClientController name = " + name);
	}
	
	// Changes buttons to X or Y if there is nothing there
	@Override
	public void actionPerformed(ActionEvent ev) {
		XorOButton button = (XorOButton) ev.getSource();
		
		if(model.CheckWinner()){
			connection.send("newgame");
			return;
		}
		
		if(getMyID() != model.idCurrentPlayer)
			return;
		
		if (!model.PlaceObject(button.getXpos(), button.getYpos())) 
			return;

		connection.send(new int[]{button.getXpos(), button.getYpos()});
		
		
		view.repaint();
	}

	private class ClientConnection extends Client {

		//String name;		//not needed here -- removed to make waiting challenger function work properly
		
		public ClientConnection(String host, int port) throws IOException {
			super(host, port);
		}

		public String getMyName() {
			return name;
		}

		@Override
		protected void messageReceived(final Object message) {
			if (message instanceof GameClientModel) {
				 	//since the system might be repainting at any moment,
				 	//you only want to modify the model in the Swing thread
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						updateModel((GameClientModel) message);
						view.repaint();
					}
				});			
			}
			if (message instanceof String) {
				String command = (String) message;
				if (command.equals("waiting")) {
					view.setMessageText("Waiting for Challenger");
					view.setModel(null);
					view.repaint();
				}
			}
		}

		protected void serverShutdown(String message) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(view,
							"The server has disconnected.\n"
									+ "The game is ended.");
					System.exit(0);
				}
			});
		}

		protected void extraHandshake(ObjectInputStream in, ObjectOutputStream out) throws IOException {
			name = JOptionPane.showInputDialog("What's your name?");
			try {
				out.writeObject(name);
				Boolean userNameAvailable = (Boolean) in.readObject();
				while (!userNameAvailable) {
					String name = JOptionPane.showInputDialog("Someone swooped that name, choose another.");
					out.writeObject(name);
					userNameAvailable = (Boolean) in.readObject();
				}
				view.repaint();
			} catch (IOException e) {
				e.printStackTrace();
				view.setMessageText("Error while getting game information.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	private void updateModel(GameClientModel model) {
		this.model = model;
		view.setModel(model);
		
		//this.name = name;		//this assignment is not needed
		//System.out.println("updateModel Name = " + name);
		
		if (model.XO == null) {
			return; // haven't started yet -- waiting for another player
		} 
		else if (!model.gameInProgress) {
			view.setTitle("GAME OVER");
			if (model.isTie())
				view.setMessageText("TIE...Better luck next time, " + name + ".  Click any square to play again.");
			else if (getMyID() == model.idWinner)
				view.setMessageText("Hey, " + name + "!  You WIN!  Click any square to beat someone else.");
			else
				view.setMessageText("Bummer, " + name + ".  You LOSE. :(  Click any square to get revenge!");
		}
		else {
			if (getMyID() == model.idPlayer1)
				//view.setTitle("You are playing X's");
				view.setTitle("Hi " + name + "!  You are playing X's.");
			else
				//view.setTitle("You are playing O's");
				view.setTitle("Hi " + name + "!  You are playing O's.");
			
			if (getMyID() == model.idCurrentPlayer)
				view.setMessageText("It's your turn.  Select a square.");
			else
				view.setMessageText("Challenger's turn.  Be patient.");
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
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
	public void mouseClicked(MouseEvent ev) {
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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public int getMyID() {
		return myID;
	}

	public void setMyID(int myID) {
		this.myID = myID;
	}

	public String getMyName() {
		return name;
	}
	
	public void setMyName(String name) {
		this.name = name;
	}
}
