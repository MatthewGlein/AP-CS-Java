import java.awt.Dimension;

import java.awt.Toolkit;

import javax.swing.JOptionPane;



public class GameClientMain {

	private static final int DEFAULT_PORT = 45017;
	
	  public static void main(String[] args) {
		  String host = JOptionPane.showInputDialog("Enter the IP address of the\nhost computer");
			if (host == null || host.trim().length() == 0)
				return;
			GameClientView view = new GameClientView();
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		      view.setLocation( (screensize.width - view.getWidth())/2,
		    		  				(screensize.height - view.getHeight())/2 );
		      view.setVisible(true);
			try {
				new GameClientController(host, DEFAULT_PORT, view);
			} catch (Exception e) {
				view.setMessageText("Server is down.");
				e.printStackTrace();
			}
	   }
}
