package ru.vasilev.testtask.interfaces;

public interface TypeConverter<T extends Comparable<T>>{

   T toType(String s);

   String fromType(T value);
}
