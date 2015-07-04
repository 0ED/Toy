import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FileDialog;
import javax.swing.BoxLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class KademliaController implements ActionListener,MouseListener
{
	public KademliaModel model;
	public KademliaView view;
	public boolean isStart;

    public KademliaController(KademliaModel aModel, KademliaView aView)
	{
		this.model = aModel;
		this.view = aView;
		this.isStart = false;
		this.view.startButton.addActionListener(this);
		this.view.addMouseListener(this);
    }
    
    /**
     * @param anEvent クリック情報
     */
    public void actionPerformed(ActionEvent anEvent)
	{
		if (anEvent.getSource() instanceof JButton) {
			JButton aButton = (JButton)anEvent.getSource();
			if (aButton.getText().equals("Start") && (!isStart)) {
				this.model.run();
				isStart = true;
			}
		}
    }

	public void mouseClicked(MouseEvent anEvent)
	{
	}

	public void mouseEntered(MouseEvent anEvent) {}
	public void mouseExited(MouseEvent anEvent) {}
	public void mousePressed(MouseEvent anEvent) {}
	public void mouseReleased(MouseEvent anEvent) {}
}
