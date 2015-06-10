import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Example
{
	public static void main(String[] args)
	{
		KademliaModel aModel = new KademliaModel();
		KademliaView aView = new KademliaView(aModel);
		aModel.setView(aView);
		KademliaController aController = new KademliaController(aModel,aView);
		JFrame aFrame = new JFrame();
		aFrame.setLocation(0,0);
		aFrame.setSize(Constants.WIN_WIDTH, Constants.WIN_HEIGHT);
		aFrame.setLayout(null);
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.getContentPane().add(aView);
		aFrame.setVisible(true);
	}
}
