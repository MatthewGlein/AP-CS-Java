import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class CarsMain {

	   public static void main(String[] args) {
		  CarsView view = new CarsView();
		  new CarsController(null, view);
	      Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	      view.setLocation( (screensize.width - view.getWidth())/2,
	    		  				(screensize.height - view.getHeight())/2 );
	      view.setVisible(true);
	      
	      
	   }
	      
}