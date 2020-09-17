package ru.vasilev.testtask.types;

import ru.vasilev.testtask.interfaces.TypeConverter;

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