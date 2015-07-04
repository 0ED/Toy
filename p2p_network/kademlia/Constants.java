import java.math.BigInteger;

public class Constants {
	public static String BLACK = "\u001b[00;30m";
	public static String RED = "\u001b[00;31m";
	public static String GREEN = "\u001b[00;32m";
	public static String YELLOW = "\u001b[00;33m";
	public static String BLUE = "\u001b[00;34m";
	public static BigInteger TWO = new BigInteger("2");
	public static int K_LEN = 4;
	public static int NODE_R_MAX = 80; //ノードの半径の最大
	public static int RING_R = 300;
	public static int WIN_WIDTH = 2*RING_R + NODE_R_MAX;
	public static int WIN_HEIGHT = WIN_WIDTH + 55;
	public static int PING_MSG = 0;
	public static int FIND_NODE_MSG = 1;
	public static int STORE_MSG = 2;
	public static int FIND_VALUE_MSG = 3;
	public static String[] messages = {"Ping()","FindNode()","Store()","FindValue()"};
	public static int SLEEP_TIME = 1200;
}
