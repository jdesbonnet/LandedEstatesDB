/*
 * (c) 2008 Joe Desbonnet
 */
package ie.wombat.imagetable;

/**
 * @author joe
 *
 */
public class HexUtil {
	public static final String byteArrayToHex (byte[] ba)
	{
		StringBuffer buf = new StringBuffer (ba.length*2);
		int j;
		for (int i = 0; i < ba.length; i++) {
			j = (int)ba[i] & 0xff;
			if (j < 16) {
				buf.append ("0");
			}
			buf.append (Integer.toHexString(j));
		}
		return buf.toString();
	}
	public static final byte[] hexToByteArray (String hex) {
		int nbyte = hex.length() / 2;
		byte[] ba = new byte[nbyte];
		int j;
		for (int i = 0; i < nbyte; i++) {
			String s= hex.substring(i*2,i*2+2);
			j = Integer.parseInt(s,16);
			ba[i] = (byte)(j & 0xff);
		}
		return ba;
	}
}
