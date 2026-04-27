package org.coffeeshop.purchaseorders.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

/**
 * @ModifiedBy Umunna David
 * updated enum keywords to match Java naming conventions and added JSON annotations for proper serialization/deserialization
/**
 * Enum defining the available size options for menu item types.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
public enum MenuItemSize {
    Regular,
    Large;

    @JsonCreator
    public static MenuItemSize fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return MenuItemSize.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
