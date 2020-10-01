package ru.vasilev.testtask.sorter;

import ru.vasilev.testtask.interfaces.TypeConverter;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that reads values from multiple files line by line
 * (one line = one value), and then writes to output file
 * with specified sorting order.
 * <p>
 * Lines are converted to specified {@link Comparable} type
 * using {@link TypeConverter} converter.
 * If type is not supported by passed converter, such value is skipped.
 * <p>
 * Files are considered to be sorted in the same order as passed
 * parameter to constructor of this class.
 * Otherwise incorrectly sorted values from files are skipped.
 *
 * @param <T> comparable type.
 * @author Vasilev K.V.
 * @version 1.0
 */
public class Sorter<T extends Comparable<T>> {

    //Maps file instance with last read value from it.
    private final Map<File, T> lastReadValues;

    //Maps file instance and last read index from it.
    private final Map<File, Integer> lastReadLineIndices;

    private final Writer writer;
    private final TypeConverter<T> converter;
    private final boolean isAscOrder;

    private int filesCount;

    private static final String READING_ERROR =
            "File '%s' can't be read!%n";
    private static final String REACHED_EOF_WARN =
            "File '%s' reached end of file!%n";
    private static final String UNEXPECTED_TYPE_ERROR =
            "Last read line in file '%s' has incorrect type! Skipping this line.%n";
    private static final String INCORRECT_ORDER_ERROR =
            "File '%s' has incorrect order! Skipping current line.%n";

    public Sorter(String output, boolean isAscOrder, Collection<File> files, TypeConverter<T> converter) throws IOException {
        this.isAscOrder = isAscOrder;
        this.converter = converter;

        lastReadLineIndices = new LinkedHashMap<>();
        lastReadValues = new LinkedHashMap<>();
        writer = new PrintWriter(new FileWriter(new File(output)));

        for (File file : files) {
            try {
                mapFileData(file, readNextValue(file, 0), 0);

                filesCount++;
            } catch (EOFException e) {
                System.out.printf(REACHED_EOF_WARN, file.getName());
            } catch (IOException e) {
                System.out.println(READING_ERROR);
            }
        }

        merge();
    }

    /**
     * Gets file with min or max value read (based on constructor order
     * parameter), writes value to output file.
     * <p>
     * Catches 'end of file' EOFException and removes file from maps.
     * <p>
     * Catches IOException and removes file, that can't be read, from maps.
     * <p>
     * Closes {@link BufferedWriter} writer after no files left.
     *
     * @throws IOException if output file can't be written.
     */
    private void merge() throws IOException {
        while (filesCount != 0) {
            File file = orderedByValue();
            writer.write(converter.fromType(lastReadValues.get(file)));
            int nextIndex = 1 + lastReadLineIndices.get(file);

            try {
                String nextValue = readNextValue(file, nextIndex);

                while (!isCorrectLine(file, nextValue)) {
                    nextIndex++;
                    nextValue = readNextValue(file, nextIndex);
                }

                mapFileData(file, nextValue, nextIndex);
            } catch (EOFException e) {
                remove(file, REACHED_EOF_WARN);
            } catch (IOException e) {
                remove(file, READING_ERROR);
            }

            if (filesCount != 0) {
                writer.write(System.lineSeparator());
            }
        }
        writer.close();
    }

    /**
     * Creates autocloseable reader {@link BufferedReader} instance
     * in 'try with resources' block.
     * Reads from file. Throws EOFException if read value equals null
     * (reaches end of file).
     *
     * @param file  File instance to iterate through.
     * @param index number of a line that needs to be read.
     * @return String with value read.
     * @throws IOException if file can't be read.
     */
    private String readNextValue(File file, int index) throws IOException {
        String value;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int start = 0;
            while (start != index) {
                br.readLine();
                start++;
            }
            value = br.readLine();
        }

        if (value == null) {
            throw new EOFException();
        }
        return value;
    }

    /**
     * Checks if next value read follows sorting
     * order and of correct type by catching IllegalArgumentException
     * thrown from {@link TypeConverter} converter.
     *
     * @param file      File that have max or min value on current iteration.
     * @param nextValue New value read from file, that should be checked.
     * @return true if next value is correct, otherwise false.
     */
    private boolean isCorrectLine(File file, String nextValue) {
        try {
            if (hasCorrectOrder(converter.toType(nextValue), lastReadValues.get(file))) {
                return true;
            } else {
                System.out.printf(INCORRECT_ORDER_ERROR, file.getName());

                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.printf(UNEXPECTED_TYPE_ERROR, file.getName());

            return false;
        }
    }

    /**
     * Helper method to check if next value follows specified order.
     *
     * @param nextValue last read value from file. Equals null on first line read.
     * @param lastValue last valid value written to output.
     * @return true if order equals to specified, otherwise false.
     */
    private boolean hasCorrectOrder(T nextValue, T lastValue) {
        if (lastValue == null) {
            return true;
        }
        boolean isGreater = nextValue.compareTo(lastValue) > 0;
        boolean isEqual = nextValue.compareTo(lastValue) == 0;

        return (isAscOrder && (isGreater || isEqual))
                || (!isAscOrder && (!isGreater || isEqual));
    }

    /**
     * Helper method to get next file, which value should be written to output file.
     *
     * @return file with min or max value (based on specified order) on current iteration.
     */
    private File orderedByValue() {
        return isAscOrder
                ? Collections.min(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey()
                : Collections.max(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private void mapFileData(File file, String value, int index) {
        lastReadValues.put(file, converter.toType(value));
        lastReadLineIndices.put(file, index);
    }

    private void remove(File file, String message) {
        lastReadValues.remove(file);
        lastReadLineIndices.remove(file);

        System.out.printf(message, file.getName());

        filesCount--;
    }
}
