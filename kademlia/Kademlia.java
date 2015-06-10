import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.math.BigInteger;
import il.technion.ewolf.kbr.RandomKeyFactory;
import il.technion.ewolf.kbr.Key;

public class Kademlia
{
	protected int NODE_NUM = 12;
	protected String[] libnames;
	protected PC[] pcs;
	protected int kBucketsLen;
	protected BigInteger pcLen;

	public Kademlia() {
		this.libnames = new String[]{"A.jar","B.jar","C.jar","D.jar","non"};
		this.pcs = new PC[NODE_NUM];
		this.kBucketsLen = 8*2; //16
		this.pcLen = Constants.TWO.pow(kBucketsLen);
	}

	public void run()
	{
		RandomKeyFactory aFactory=null;
		try {
			//2^(2*8)=65536台分PCは対応可能
			aFactory = new RandomKeyFactory(this.kBucketsLen/8,new Random(), "SHA");
		}
		catch(Exception e) { e.printStackTrace(); }

		for(int i=0; i<NODE_NUM; i++)
		{
			int lib_i = new Random().nextInt(libnames.length);
			String ipString = "10.0.0." + Integer.toString(i); //未使用
			Key aKey = aFactory.create(ipString);
			pcs[i] = new PC(this,libnames[lib_i], aKey, i);
			//System.out.println(ipString + " " + (aKey.getInt().add(this.pcLen.divide(Constants.TWO))) + " " + aKey.toBinaryString());
		}

		//スタート
		for(int i=0; i<NODE_NUM; i++) {
			System.out.print(Constants.RED);
			System.out.println("Joined Node " + i);
			System.out.print(Constants.BLACK);

			//RandomKeyFactory aFactory = new RandomKeyFactory();
			pcs[i].isNIRF = true; // NIRFネットワークとして参加してるか
			Thread aThread = new Thread(pcs[i]); //
			aThread.start();
			try { Thread.sleep(1200); } //参加するまでの待ち時間
			catch(Exception e) { e.printStackTrace(); }
		}
	}

	public static void main(String[] args)
	{
		Kademlia app = new Kademlia();
		app.run();
	}
}
