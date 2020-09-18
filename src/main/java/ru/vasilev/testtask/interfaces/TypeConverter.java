package ru.vasilev.testtask.interfaces;

/**
 * Converts type from String (after reading from file)
 * and to String (to write to file).
 *
 * @param <T> comparable type.
 *
 * @author Vasilev K.V.
 * @version 1.0
 */
public interface TypeConverter<T extends Comparable<T>> {

    T toType(String s);

    String fromType(T value);
}
