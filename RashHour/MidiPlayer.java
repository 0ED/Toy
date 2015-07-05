import java.io.IOException;
import java.io.File;
import javax.sound.sampled.*;

public class MidiPlayer extends Thread {
	private String filename;
	public MidiPlayer(String filename) { this.filename = filename; }
	public void run() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(this.filename));
			byte [] data = new byte [ais.available()];
			ais.read(data);
			ais.close();
			AudioFormat af = ais.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
			SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(af);
			line.start();
			line.write(data, 0, data.length);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}
