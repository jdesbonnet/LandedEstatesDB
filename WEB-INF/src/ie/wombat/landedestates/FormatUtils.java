package ie.wombat.landedestates;

public class FormatUtils {

	public static String formatText (String in) {
		return in.replaceAll("\r\n", "\n").replaceAll("\n\n", "\n<p>\n");
	}
}
