import javax.swing.JFrame;

public class Example
{
	public static void main(String[] args) {
		RashModel aModel = new RashModel();
		RashView aView = new RashView(aModel);
		aModel.setView(aView);
		RashController aController = new RashController(aModel,aView);
		JFrame aFrame = new JFrame();
		aFrame.setBounds(0,0, Constants.WIN_WIDTH, Constants.WIN_HEIGHT);
		aFrame.setLayout(null);
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.getContentPane().add(aView);
		aFrame.setResizable(false);
		aFrame.setVisible(true);
	}
}
