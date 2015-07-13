import java.io.IOException;
import java.io.File;
import javax.sound.sampled.*;

public class MidiPlayer extends Thread {
	private String filename;
	private SourceDataLine line;
	private byte[] midiData;
	public MidiPlayer(String filename) {
		this.filename = filename;
		try {
			AudioInputStream aInputStream = AudioSystem.getAudioInputStream(new File(this.filename));
			this.midiData = new byte [aInputStream.available()];
			aInputStream.read(midiData);
			aInputStream.close();
			AudioFormat audioFormat = aInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			this.line = (SourceDataLine)AudioSystem.getLine(info);
			this.line.open(audioFormat);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	public void run() {
		try {
			this.line.start();
			this.line.write(this.midiData, 0, this.midiData.length);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	public void stopMidi() {
		try { this.line.stop(); }
		catch (Exception e) { e.printStackTrace(); }
	}
}
