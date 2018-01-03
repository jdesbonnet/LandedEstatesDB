package ie.wombat.landedestates;

public class TextUtil {

	/**
	 * Replace double single quote with HTML i tag.
	 * 
	 * @param s
	 * @return
	 */
	public static String markingsToHtml(String s) {
		
		if (s == null) {
			return "";
		}
		
		char[] ca = s.toCharArray();
		StringBuffer buf = new StringBuffer();
		boolean italicsFlag = false;
		
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == '\'' && ca[i + 1] == '\'') {
				if (italicsFlag) {
					italicsFlag = false;
					buf.append("</i>");
				} else {
					italicsFlag = true;
					buf.append("<i>");
				}
				i++;
				continue;
			}
			buf.append(ca[i]);
		}
		if (italicsFlag) {
			buf.append("</i>");
		}

		return buf.toString();
	}

	/**
	 * Replace HTML 'i' tag with double single quote
	 * 
	 * @param s
	 * @return
	 */
	public static String htmlToMarkings(String s) {
		
		if (s==null) {
			return "";
		}

		char[] ca = s.toCharArray();
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == '<' && ca[i + 1] == 'i' && ca[i + 2] == '>') {

				buf.append("''");
				i += 2;
				continue;
			}

			if (ca[i] == '<' && ca[i + 1] == '/' && ca[i + 2] == 'i'
					&& ca[i + 3] == '>') {
				buf.append("''");
				i += 3;
				continue;
			}
			
			/*
			if (ca[i] == '<' && ca[i+1]=='p' && ca[i+2]=='>') {
				buf.append ("\n\n");
				i+=2;
				continue;
			}
			*/

			buf.append(ca[i]);
		}
		return buf.toString();

	}
	
	/**
	 * Make sure a string is safe for inclusion as a text node in XML.
	 * @param s
	 * @return
	 */
	public static String xmlSafe (String s) {
		if (s == null) return "";
		s = s.replaceAll("&","&amp;");
		s = s.replaceAll("<","&lt;");
		s = s.replaceAll(">","&gt;");
		return s;
	}
	
}
