import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Rectangle;

public class RashView extends javax.swing.JPanel
{
	protected RashModel model;

	public RashView(RashModel aModel) {
		this.model = aModel;
		this.setLayout(null);
		this.setBounds(0,0, Constants.WIN_WIDTH,Constants.WIN_HEIGHT);
		this.setBackground(Color.BLACK);
	}

	public void paintBasicBoard(Graphics aGraphics)
	{
		Graphics2D aGraphics2D = (Graphics2D) aGraphics;
		aGraphics.drawImage(this.model.rashBoardImage,
				this.model.boardRect.x, this.model.boardRect.y,
				this.model.boardRect.width, this.model.boardRect.height, this);
		aGraphics2D.setColor(new Color(220,255,255));
		aGraphics2D.setStroke(new BasicStroke(1.8f));

		Rectangle whiteRect = this.model.whiteBoardRect;
		for(int i = 1; i < Constants.MAP_SIZE; i++) {
			aGraphics2D.drawLine(whiteRect.x, whiteRect.y + i*this.model.interval,
					whiteRect.x + whiteRect.width, whiteRect.y + i*this.model.interval);
			aGraphics2D.drawLine(whiteRect.x + i*this.model.interval,	whiteRect.y,
					whiteRect.x + i*this.model.interval, whiteRect.y + whiteRect.height);
		}
	}

	public void paintLightCycle(Graphics aGraphics)
	{
		for(LightCycle aCycle : this.model.lightCycles) {
			String cyclePath = "img/lightCycles/" + aCycle.color + "_" + aCycle.angle + ".png";
			Image aLightCycleImage = Toolkit.getDefaultToolkit().getImage(cyclePath);

			aGraphics.drawImage(aLightCycleImage, aCycle.rect.x, aCycle.rect.y, aCycle.rect.width, aCycle.rect.height, this);
		}
	}

	public void paintComponent(Graphics aGraphics) {
		this.paintBasicBoard(aGraphics);
		this.paintLightCycle(aGraphics);
	}
}
