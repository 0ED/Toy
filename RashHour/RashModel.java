import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;
import javax.imageio.ImageIO;

public class RashModel
{
	protected RashView view;
	protected Rectangle boardRect, whiteBoardRect;
	protected int interval;
	protected List<LightCycle> lightCycles;
	protected Image rashBoardImage;

	public RashModel() {
		this.boardRect = new Rectangle();
		this.whiteBoardRect = new Rectangle();
		this.lightCycles = new ArrayList<LightCycle>();
	}

	public void setView(RashView aView) {
		this.view = aView;
		try { this.prepare(); }
		catch(IOException anException) { System.err.println("To read File error"); }
	}

	public void prepare() throws IOException
	{
		this.rashBoardImage = ImageIO.read(new File("img/RashBoard.png"));
		this.boardRect.setSize(this.rashBoardImage.getWidth(this.view), this.rashBoardImage.getWidth(this.view));
		this.boardRect.setLocation(Constants.WIN_CENTER_WIDTH - this.boardRect.width / 2,
								Constants.WIN_CENTER_HEIGHT - this.boardRect.height / 2);

		this.rashBoardImage = Toolkit.getDefaultToolkit().getImage("img/RashBoard.png");
		int offsetWidth = 32; //画像の黄色の部分のoffset
		int offsetHeight = offsetWidth; //画像の黄色の部分のoffset
		this.whiteBoardRect.setLocation(this.boardRect.x + offsetWidth, this.boardRect.y + offsetHeight);
		this.whiteBoardRect.setSize(this.boardRect.width - 2*offsetWidth, this.boardRect.height - 2*offsetHeight);
		this.interval = this.whiteBoardRect.width / Constants.MAP_SIZE;

		BufferedReader aReader = new BufferedReader(new FileReader(new File(Constants.mapFileName)));
		String line;
		while ((line = aReader.readLine()) != null)
		{
			String[] param = line.split(",");
			int x = Integer.parseInt(param[0]), y = Integer.parseInt(param[1]);
			int len = Integer.parseInt(param[2]), angle = Integer.parseInt(param[3]);
			String color = param[4];
			Rectangle aCycleRect = new Rectangle(this.whiteBoardRect);
			aCycleRect.translate(this.interval * x, this.interval * y);
			if (angle % 2 == 0) { aCycleRect.setSize(this.interval,this.interval * len); }
			else { aCycleRect.setSize(this.interval * len, this.interval); }

			this.lightCycles.add(new LightCycle(aCycleRect, angle, color));
		}
	}
}
