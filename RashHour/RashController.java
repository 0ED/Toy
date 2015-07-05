import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class RashController extends MouseAdapter
{
	public RashModel model;
	public RashView view;
	public LightCycle movingCycle;
	public Point backPoint;
	public MidiPlayer midiPlayer;

    public RashController(RashModel aModel, RashView aView) {
		this.model = aModel;
		this.view = aView;
		this.midiPlayer = new MidiPlayer(Constants.menuMusic);
		this.midiPlayer.start();
		this.movingCycle = null; this.backPoint = null;
		this.view.addMouseListener(this);
		this.view.addMouseMotionListener(this);
    }

	public void mousePressed(MouseEvent anEvent) {
		if (this.model.isStartMenu) {
			if (this.model.startButtonRect.contains(anEvent.getPoint())) {
				this.model.isStartMenu = false;
				this.midiPlayer.stop();
				this.view.repaint();
				this.midiPlayer = new MidiPlayer(Constants.menuMusic);
				this.midiPlayer.start();
			}
		}
		else {
			for (LightCycle aCycle : this.model.lightCycles) {
				if (aCycle.rect.contains(anEvent.getPoint())) this.movingCycle = aCycle;
			}
			this.backPoint = anEvent.getPoint();
		}
	}

	public void mouseMoved(MouseEvent anEvent) {
		if (this.model.isStartMenu) {
			if (this.model.startButtonRect.contains(anEvent.getPoint())) {
				if (! this.model.isHoverStartButton) {
					this.model.isHoverStartButton = true;
					this.view.repaint();
				}
			}
			else {
				if (this.model.isHoverStartButton) {
					this.model.isHoverStartButton = false;
					this.view.repaint();
				}
			}
		}
	}

	public void mouseDragged(MouseEvent anEvent)
	{
		if (this.movingCycle == null) { return; }
		int offsetX=0, offsetY=0;
		if (this.movingCycle.angle % 2 == 0) { offsetY = anEvent.getPoint().y - this.backPoint.y; } //縦
		else { offsetX = anEvent.getPoint().x - this.backPoint.x; } //横

		Rectangle movingRect = new Rectangle(this.movingCycle.rect);
		movingRect.translate(offsetX,offsetY);
		if (! this.model.whiteBoardRect.contains(movingRect)) { return; }

		for (LightCycle aCycle : this.model.lightCycles) {
			if (aCycle == this.movingCycle) { continue; }
			if (movingRect.intersects(aCycle.rect)) { return; }
		}
		this.movingCycle.rect = movingRect;
		this.view.repaint();
		this.backPoint = anEvent.getPoint();
	}

	public void mouseReleased(MouseEvent anEvent)
	{
		if (this.movingCycle == null) { return; }
		Point aPoint = this.movingCycle.rect.getLocation();
		Point pp = this.model.whiteBoardRect.getLocation();
		for(int i = 0; i < Constants.MAP_SIZE; i++)
		{
			if (pp.x <= aPoint.x && aPoint.x <= pp.x + this.model.interval) {
				if (pp.x <= aPoint.x && aPoint.x <= pp.x + this.model.interval/2) aPoint.x = pp.x;
				else aPoint.x = pp.x + this.model.interval;
			}
			if (pp.y <= aPoint.y && aPoint.y <= pp.y + this.model.interval) {
				if (pp.y <= aPoint.y && aPoint.y <= pp.y + this.model.interval/2) aPoint.y = pp.y;
				else aPoint.y = pp.y + this.model.interval;
			}
			pp.translate(this.model.interval, this.model.interval);
		}
		this.movingCycle.rect.setLocation(aPoint);
		this.view.repaint();
		this.movingCycle = null;
	}
}
