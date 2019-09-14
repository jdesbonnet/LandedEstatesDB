package ie.wombat.landedestates;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import ie.wombat.imagetable.Image;
import ie.wombat.imagetable.ImageData;

/**
 * Used by GSON to exclude some fields from the JSON serialization (eg image data)
 * during export.
 * 
 * @author Joe Desbonnet
 *
 */
public class ExportExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		
		
		if (f.getDeclaringClass() == House.class) {
			if (f.getName().equals("images")) {
				return true;
			}
		}
		if (f.getDeclaringClass() == Estate.class) {
			if (f.getName().equals("houses")) {
				return true;
			}
		}
		
		if (f.getDeclaringClass() == Image.class) {
			if (f.getDeclaredType() == ImageData.class) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
