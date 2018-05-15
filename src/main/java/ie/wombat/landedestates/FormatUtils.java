package ie.wombat.landedestates;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {

	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String formatText (String in) {
		return in.replaceAll("\r\n", "\n").replaceAll("\n\n", "\n<p>\n");
	}
	public static String formatTimestamp (Date d) {
		if (d==null) {
			return "(null)";
		}
		return timestampFormat.format(d);
	}
	public static String formatClassName (String className) {
		if (className==null) {
			return "(null)";
		}
		String[] p = className.split("\\.");
		if (p.length==0) {
			return "(none)";
		}
		return p[p.length-1];
	}
}
