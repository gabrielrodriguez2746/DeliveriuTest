package grodriguez.com.deliveriutest.utils;

import android.widget.EditText;

import java.util.List;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class FieldValidator {

    public static boolean isValid(EditText field) {
        return field.getText() != null
                && field.getText().toString().trim() != null
                && !field.getText().toString().trim().isEmpty();
    }

    public static boolean isValid(List<?> list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isValid(String string) {
        return string != null && !string.trim().isEmpty() && !string.equalsIgnoreCase("null");
    }
}

