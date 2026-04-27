package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;

@Converter(autoApply = false)
public class MenuItemSizeConverter implements AttributeConverter<MenuItemSize, String> {

    @Override
    public String convertToDatabaseColumn(MenuItemSize attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public MenuItemSize convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return MenuItemSize.valueOf(dbData.trim().toUpperCase(Locale.ROOT));
    }
}
