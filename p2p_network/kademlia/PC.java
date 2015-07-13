import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.math.BigInteger;

import il.technion.ewolf.kbr.Key;
import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.Node;
import il.technion.ewolf.kbr.KeyFactory;
import il.technion.ewolf.kbr.openkad.KadNetModule;

public class PC extends Node implements Runnable
{
	public boolean isNIRF;
	public String libnames;
	public int id; //後でKeyに変更
	public Map<Integer,ArrayList<PC>> kBuckets; //Keyの文字列(toBinaryString)とPCのリスト
	public Map<String,ArrayList<PC>> dht; //ライブラリ,PCたち
	public BigInteger[] twoPows; //160
	public KademliaView view;
	public KademliaModel model;


	/*
	 * コンストラクタ
	 */
	public PC(KademliaModel aModel,
			KademliaView aView,
			String libnames, Key aKey, int id)
	{
		super(aKey);
		this.model = aModel;
		this.view = aView;
		this.isNIRF = false;
		this.libnames = libnames;
		this.kBuckets = new TreeMap<Integer,ArrayList<PC>>(); //Keyの文字列(toBinaryString)とPCのリスト

		this.dht = new TreeMap<String,ArrayList<PC>>(); //ライブラリ,PCのたち
		this.twoPows = new BigInteger[this.model.kBucketsLen+1]; //160
		for(int i=0; i < this.model.kBucketsLen+1; i++) { this.twoPows[i]=Constants.TWO.pow(i); }
		this.id = id;
	}

	public boolean ping() { return this.isNIRF; }

	/*
	 * Key同士の
	 */
	public int getDistance(Key fromKey, Key toKey)
	{
		Key distanceXorKey = fromKey.xor(toKey);

		//距離を求める
		int distance = -1;
		for(int i=0; i < this.model.kBucketsLen; i++)
		{
			BigInteger distanceXor = distanceXorKey.getInt();
			distanceXor = distanceXor.add(this.model.pcLen.divide(Constants.TWO)); // 2^X + (2^X)/2
			if (this.twoPows[i] == null) { continue; }

			//twoPows[i] <= distance && distance < twoPows[i]
			if (this.twoPows[i].compareTo(distanceXor) <= 0 && distanceXor.compareTo(twoPows[i+1]) < 0) {
				distance=i; break;
			}
		}
		return distance;
	}

	/*
	 * fromPCからtoPCに向けてメッセージを送り, toPCのkBucketsにfromPCを登録する
	 * KBucketsにaPCを追加
	 */
	public void addToKBuckets(PC fromPC, PC toPC)
	{
		int distance = getDistance(fromPC.getKey(), toPC.getKey());

		//ノードを追加する
		if (toPC.kBuckets.containsKey(distance))
		{
			ArrayList<PC> aKBucket = toPC.kBuckets.get(distance);
			if (!aKBucket.contains(fromPC)) //要素がないとき
			{
				if (aKBucket.size() >= Constants.K_LEN) //K_LENをこえて溢れる
				{
					PC beginPC = aKBucket.get(0);
					if (beginPC.ping())
					{
						beginPC = aKBucket.remove(0); //先頭ノードを後方ノードに移動
						aKBucket.add(beginPC);
					}
					else {
						aKBucket.remove(0); //先頭ノードを削除して,後方に新規ノードに追加
						aKBucket.add(fromPC);
					}
				}
				else { aKBucket.add(fromPC); } //K_LENに収まるなら
			}
		}
		else {
			//何もない時
			ArrayList<PC> aKBucket = new ArrayList<PC>();
			aKBucket.add(fromPC);
			toPC.kBuckets.put(distance, aKBucket);
		}
	}

	/*
	 * DHTにaPCのaKeyとlibnamesを登録する
	 */
	public void store(String aLibname, PC fromPC, PC toPC)
	{
		if (toPC.dht.containsKey(aLibname)) {
			ArrayList<PC> pcs = toPC.dht.get(aLibname); //前のリストを上書き
			pcs.add(fromPC);
			toPC.dht.put(aLibname, pcs);
		}
		else {
			ArrayList<PC> pcs = new ArrayList<PC>();
			pcs.add(fromPC);
			toPC.dht.put(aLibname, pcs);
		}
	}

	/*
	 * 最初参加する際に全部のIPアドレスに対して, NIRFに参加してるノードを探す.
	 * 最初のランダムにブロードキャストし,一つのノードにメッセージを送る.
	 * 最後にその一つのノードを返す
	 */
	public PC pingBroadcast()
	{
		List<PC> randomPCs = Arrays.asList(Arrays.copyOf(this.model.pcs, this.model.pcs.length));
		Collections.shuffle(randomPCs);

		for (PC toPC : randomPCs) {
			if (toPC == this.model.pcs[this.id]) { continue; }
			if (toPC.ping())
			{
				PC fromPC = this.model.pcs[this.id];
				this.addToKBuckets(fromPC, toPC);
				this.addToKBuckets(toPC, fromPC);
				this.model.sendMessage(fromPC, toPC, Constants.PING_MSG);
				this.model.updateAndSleep();

				//this.store(fromPC.libnames, fromPC, toPC); //DHTを登録する
				return toPC;
			}
		}
		return null;
	}


	/*
	 *
	 * @param 参照したい経路表を持つノード
	 * @param K個のノードを補完する場所
	 * @param 距離
	 */
	public List<PC> getKNode(PC aPC, List<PC> pcK, int distance, boolean[] isVisit)
	{
		if (isVisit[distance]) { return pcK; }

		if (aPC.kBuckets.containsKey(distance))
		{
			List<PC> aKBucket = new ArrayList(aPC.kBuckets.get(distance));
			if (pcK.size() > Constants.K_LEN) { return pcK; }

			int remainK = Constants.K_LEN - pcK.size();
			for(int i=0; i<remainK; i++)
			{
				if (aKBucket.isEmpty()) { break; }
				PC addingPC = aKBucket.remove(0);
				pcK.add(addingPC);
			}
		}
		isVisit[distance] = true;

		return pcK;
	}

	/*
	 * 自身の経路表を元に指定されたKeyに近いノードたち一覧を経路表に追加する。
	 */
	public List<PC> findNode(PC fromPC, PC toPC)
	{
		int distance = this.getDistance(fromPC.getKey(), toPC.getKey()); //0~160
		//System.out.println(distance);

		//K個のノードを選択する
		int afterDistance = distance;
		int beforeDistance = distance;
		List<PC> pcK = new ArrayList<PC>();
		boolean[] isVisit = new boolean[160];
		while(true) {
			if (isVisit[beforeDistance] || isVisit[afterDistance]) {
				break;
			}
			if (afterDistance == beforeDistance) {
				//K個に収まる分だけpcKに追加
				pcK = this.getKNode(toPC,pcK,distance, isVisit); //toPCのテーブルにK個収まる分だけ, pcKに追加する
			}
			else {
				pcK = this.getKNode(toPC,pcK,beforeDistance,isVisit); //toPCのテーブルにK個収まる分だけ, pcKに追加する
				pcK = this.getKNode(toPC,pcK,afterDistance,isVisit); //toPCのテーブルにK個収まる分だけ, pcKに追加する
			}

			if ((beforeDistance-1) < 0) { beforeDistance=(160-1); }
			else { beforeDistance = beforeDistance - 1; }
			afterDistance = (afterDistance+1) % 160;
		}

		this.addToKBuckets(toPC, fromPC); //toPCのテーブルにfromPCを追加
		this.model.sendMessage(fromPC, toPC, Constants.FIND_NODE_MSG); //色付け

		return pcK;
	}

	/*
	 * NIRFアプリを起動する
	 */
	public void run()
	{
		boolean isFirst = true;
		PC fromPC = this.model.pcs[this.id];
		PC toPC = null;

		//Whileで回すのが基本
		if (isFirst) {
			toPC = this.pingBroadcast();
			isFirst = false;
		}

		 //pingが返ってきたなら,ノードの一覧とstoreしてくれ情報を返す
		if (toPC != null) {
			//System.out.println(this.id + " -> " + toPC.id);
			List<PC> pcK = this.findNode(fromPC, toPC);
			//this.store(fromPC.libname, fromPC, toPC);
			this.model.updateAndSleep();

			//K個のノードを経路表に追加
			for(PC aPC : pcK) {
				if (aPC == fromPC) { continue; } //送り先のPCとKBucketsのPCがかぶる時追加しない
				this.findNode(fromPC, aPC); //K個のノードにFind
			}
			this.model.updateAndSleep();
		}

		this.model.setAllRouting();
		this.model.updateAndSleep();
		//this.model.showKBucketAll();
		//this.model.showKBucket(this.id);
	}


	/*
	public void sendMessageToBroadcast(List<PC> connectPCs, String message)
	{
	}
	*/
}
