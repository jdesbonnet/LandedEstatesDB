package ie.wombat.landedestates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimelineAnalysis {
	
	private static Logger log = LoggerFactory.getLogger(TimelineAnalysis.class);
	
	public static Set<String> parseTextForDate (String in) {
		
		Set<String> ret = new HashSet<>();
		
		if (in == null || in.length()==0) {
			return ret;
		}
		
		Pattern yearPattern = Pattern.compile("\\s\\d\\d\\d\\d");		
		Matcher m = yearPattern.matcher(in);
		
		while (m.find()) {
			String year = in.substring(m.start(), m.end()).trim();
			ret.add(year);
		}
		
		// "nnn acres" is a common anti-pattern for years. Remove these.
		Pattern acresPattern = Pattern.compile("\\d\\d\\d\\d\\+?\\s+acres");		
		m = acresPattern.matcher(in);
		while (m.find()) {
			String acres = in.substring(m.start(), m.end()).trim();
			String[] p = acres.split("\\s+");
			String year = p[0].substring(0, 4);
			ret.remove(year);
		}
		
		return ret;
	}
	
}
