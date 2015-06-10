import java.util.Comparator;
import il.technion.ewolf.kbr.Key;
import java.math.BigInteger;

public class KeyComparator implements Comparator<Key>
{
	@Override public int compare(Key k1, Key k2)
	{
		return k1.getInt().compareTo(k2.getInt()) < 0 ? -1 : 1;
	}
}
