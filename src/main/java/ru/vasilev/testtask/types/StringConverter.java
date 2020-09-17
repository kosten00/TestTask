package ru.vasilev.testtask.types;

import ru.vasilev.testtask.interfaces.TypeConverter;

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