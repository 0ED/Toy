import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class KademliaView extends JPanel
{
	public KademliaModel model;
	public int nodeR;
	public List<Point> points;
	public List<Pair<Integer,Integer>> branches; //ノードのペア
	public String messageString;
	public JButton startButton;

	public KademliaView(KademliaModel aModel)
	{
		this.model = aModel;
		this.nodeR = 0;
		this.messageString="";
		this.points = new ArrayList<Point>();
		this.branches = new ArrayList<Pair<Integer,Integer>>();
		this.setLayout(null);
		this.setLocation(0,0);
		this.setSize(Constants.WIN_WIDTH,Constants.WIN_HEIGHT);
		this.setBackground(Color.WHITE);

		//スタートボタン
		startButton = new JButton("Start");
		startButton.setLocation(10,10);
		startButton.setSize(100,30);
		this.add(this.startButton);

		this.initRingPoint();
	}


	public void setMessageKind(int messageKind)
	{
		//if (Constants.messages.length <= messageKind) { return; }
		this.messageString = Constants.messages[messageKind];
	}

	public void addLineOfmessage(PC fromPC, PC toPC)
	{
		this.branches.add(new Pair<Integer,Integer>(fromPC.id,toPC.id));
	}

	/*
	 *
	 */
	public void initRingPoint()
	{
		int width = this.getWidth();
		int height = this.getHeight();
		int center_x = width / 2;
		int center_y = height / 2;
		double d_angle = 360.0d / this.model.NODE_NUM;
		double ringCircumference = 2 * Math.PI * Constants.RING_R;
		nodeR = (int)((ringCircumference / this.model.NODE_NUM) / 2);
		if (nodeR > Constants.NODE_R_MAX) { nodeR = Constants.NODE_R_MAX; }

		for(int i=0; i<this.model.NODE_NUM; i++)
		{
			double aRadian = Math.toRadians(d_angle * i);
			int x = center_x + (int)(Constants.RING_R * Math.cos(aRadian));
			int y = center_y + (int)(Constants.RING_R * Math.sin(aRadian));
			this.points.add(new Point(x,y));
		}
	}

	/**
	 * ブランチたちを描画する.
	 */
	public void paintBranches(Graphics aGraphics)
	{
		Graphics2D aGraphics2D = (Graphics2D) aGraphics;
		if (this.messageString.equals("")) aGraphics2D.setColor(Color.GREEN);
		else aGraphics2D.setColor(Color.GRAY);
		aGraphics2D.setStroke(new BasicStroke(3.5f));

		FontMetrics aFontMetrics = aGraphics2D.getFontMetrics();
		Rectangle2D aRect = aFontMetrics.getStringBounds(this.messageString, aGraphics2D);
		aGraphics2D.setFont(new Font("ＭＳ 明朝", Font.BOLD, 16));

		for(Pair<Integer,Integer> aPair: this.branches)
		{
			Point beginP = this.points.get(aPair.first);
			Point endP = this.points.get(aPair.second);
			aGraphics2D.draw(new Line2D.Double(beginP.x, beginP.y, endP.x, endP.y));

			int cx = (beginP.x+endP.x)/2;
			int cy = (beginP.y+endP.y)/2;
			aGraphics2D.setColor(Color.WHITE);
			aGraphics2D.fillRect(cx,cy - aFontMetrics.getAscent(),
					(int)aRect.getWidth(), (int)aRect.getHeight());
			if (this.messageString.equals("")) aGraphics2D.setColor(Color.GREEN);
			else aGraphics2D.setColor(Color.GRAY);
			aGraphics2D.drawString(this.messageString, cx,cy);
		}
	}

	/**
	 * ノードたちを描画する.
	 */
	public void paintNodes(Graphics aGraphics)
	{
		Graphics2D aGraphics2D = (Graphics2D)aGraphics;
		aGraphics2D.setStroke(new BasicStroke(3.5f));

		for(int i=0; i<this.model.NODE_NUM; i++)
		{
			Point aPoint = this.points.get(i);
			int x = aPoint.x - nodeR/2;
			int y = aPoint.y - nodeR/2;
			if (this.model.pcs[i].isNIRF) {
				aGraphics.setColor(Color.GREEN);
				aGraphics.fillOval(x, y, this.nodeR, this.nodeR);
				aGraphics.setColor(Color.GRAY);
				aGraphics.drawOval(x, y, this.nodeR, this.nodeR);
			}
			else {
				aGraphics.setColor(Color.WHITE);
				aGraphics.fillOval(x, y, this.nodeR, this.nodeR);
				aGraphics.setColor(Color.GRAY);
				aGraphics.drawOval(x, y, this.nodeR, this.nodeR);
			}
		}
	}

	/**
	 * グラフを描画する.
	 */
	public void paintComponent(Graphics aGraphics)
	{
		super.paintComponent(aGraphics);
		this.paintBranches(aGraphics);
		this.paintNodes(aGraphics);
		branches.clear(); //すべて削除
	}

}
