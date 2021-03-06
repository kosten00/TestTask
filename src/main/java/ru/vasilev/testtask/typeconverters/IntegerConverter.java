package ru.vasilev.testtask.typeconverters;

import ru.vasilev.testtask.interfaces.TypeConverter;

/**
 * Implementation of {@link TypeConverter} interface to convert from/to String type.
 *
 * @author Vasilev K.V.
 * @version 1.0
 */
public class IntegerConverter implements TypeConverter<Integer> {

    @Override
    public Integer toType(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public String fromType(Integer value) {
        return String.valueOf(value);
    }
}