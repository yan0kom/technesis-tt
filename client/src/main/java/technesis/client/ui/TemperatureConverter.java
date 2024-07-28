package technesis.client.ui;

import javafx.util.StringConverter;

public class TemperatureConverter extends StringConverter<Float> {
    static final String DEGREE_CELSIUS = "\u2103";

    @Override
    public String toString(Float val) {
        return (val == null ? "0" : val.toString()) + DEGREE_CELSIUS;
    }

    @Override
    public Float fromString(String str) {
        try {
            if (str != null && !str.isBlank()) {
                if (str.endsWith(DEGREE_CELSIUS)) {
                    return Float.parseFloat(str.substring(0, str.length() - 1));
                } else {
                    return Float.parseFloat(str);
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return 0.0f;
    }
}
