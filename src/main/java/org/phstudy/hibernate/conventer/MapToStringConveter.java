package org.phstudy.hibernate.conventer;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.backtothefront.HstoreHelper;

@Converter(autoApply = true)
// Hibernate bug#HHH-8804, Map can not use generics
public class MapToStringConveter implements AttributeConverter<Map, String> {

	@Override
	public String convertToDatabaseColumn(Map attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return "";
		}
		return HstoreHelper.toString(attribute);
	}

	@Override
	public Map<String, String> convertToEntityAttribute(String dbData) {
		// Hibernate will convert Hstore to String...
		String str = (String) dbData;
		if (str.trim().length() == 0) {
			return new HashMap<String, String>();
		}
		return HstoreHelper.toMap((String) dbData);
	}
}
