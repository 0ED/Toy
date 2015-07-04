import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.math.BigInteger;
import java.awt.Dimension;
import java.awt.Point;
import il.technion.ewolf.kbr.RandomKeyFactory;
import il.technion.ewolf.kbr.Key;

public class KademliaModel
{
	protected int NODE_NUM = 18;
	protected String[] libnames;
	protected PC[] pcs;
	protected int kBucketsLen;
	protected BigInteger pcLen;
	protected KademliaView view;

	public KademliaModel()
	{
		this.view = null;
		this.libnames = new String[]{"A.jar","B.jar","C.jar","D.jar","non"};
		this.pcs = new PC[NODE_NUM];
		this.kBucketsLen = 8*2; //16
		this.pcLen = Constants.TWO.pow(kBucketsLen);
	}
	public void setView(KademliaView aView) {
		this.view = aView;
		this.prepare();
	}

	public void prepare()
	{
		RandomKeyFactory aFactory=null;
		try {
			//2^(2*8)=65536台分PCは対応可能
			aFactory = new RandomKeyFactory(this.kBucketsLen/8,new Random(), "SHA");
		}
		catch(Exception e) { e.printStackTrace(); }

		List<Key> keys = new ArrayList<Key>();
		for(int i=0; i<NODE_NUM; i++)
		{
			String ipString = "10.0.0." + Integer.toString(i); //未使用
			keys.add(aFactory.create(ipString));
		}
		Collections.sort(keys, new KeyComparator());

		for(int i=0; i<NODE_NUM; i++)
		{
			int lib_i = new Random().nextInt(libnames.length);
			String ipString = "10.0.0." + Integer.toString(i); //未使用
			Key aKey = keys.get(i);
			this.pcs[i] = new PC(this,this.view,libnames[lib_i], aKey, i);
			//System.out.println(ipString + " " + (aKey.getInt().add(this.pcLen.divide(Constants.TWO))) + " " + aKey.toBinaryString());
		}
	}

	public void run()
	{
		//スタート
		for(int i=0; i<NODE_NUM; i++) {
			System.out.print(Constants.RED);
			System.out.println("Joined Node " + i);
			System.out.print(Constants.BLACK);

			//RandomKeyFactory aFactory = new RandomKeyFactory();
			this.pcs[i].isNIRF = true; // NIRFネットワークとして参加してるか

			Thread aThread = new Thread(pcs[i]); //
			aThread.start();
			try { Thread.sleep(2000); } //参加するまでの待ち時間
			catch(Exception e) { e.printStackTrace(); }
		}
	}

	public void update()
	{ 
		Point aPoint = this.view.getLocation();
		Dimension aDimension = this.view.getSize();
		this.view.paintImmediately(0,0, aDimension.width, aDimension.height); 
	}
}
