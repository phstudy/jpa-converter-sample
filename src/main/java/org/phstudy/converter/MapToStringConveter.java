package org.phstudy.converter;

import org.postgresql.util.HStoreConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;

@Converter(autoApply = true)
public class MapToStringConveter implements AttributeConverter<Map<String, String>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        return HStoreConverter.toString(attribute);
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        return HStoreConverter.fromString(dbData);
    }
}
