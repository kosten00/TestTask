package ru.vasilev.testtask.typeconverters;

import ru.vasilev.testtask.interfaces.TypeConverter;

/**
 * Implementation of {@link TypeConverter} interface to convert from/to String type.
 *
 * @author Vasilev K.V.
 * @version 1.0
 */
public class StringConverter implements TypeConverter<String> {

    @Override
    public String toType(String s) {
        return s;
    }

    @Override
    public String fromType(String value) {
        return value;
    }
}