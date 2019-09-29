package ie.wombat.landedestates;

import javax.persistence.AttributeConverter;

/**
 * Convert boolean attributes to 'Y', 'N' in database column.
 * @author joe
 *
 */
public class BooleanToYNConverter implements AttributeConverter<Boolean, String>{

	/**
	 * Return "Y" for Boolean.TRUE, "N" for Boolean.FALSE, null for null.
	 * 
	 * @param b Boolean
	 */
	@Override
	public String convertToDatabaseColumn(Boolean b) {
		if (b == null) {
			return null;
		}
		return b.booleanValue() ? "Y":"N";
	}
	public Boolean convertToEntityAttribute(String s) {
		if (s == null) {
			return null;
		}
		return ("Y".equals(s));
	}

}
