package ru.vasilev.testtask.sorter;

import ru.vasilev.testtask.interfaces.TypeConverter;

import java.io.*;

import java.util.*;

/**
 * Class that reads sorted values from files, passed through constructor,
 * and than writes to output file.
 * <p>
 * If files are not sorted correctly - skips unsorted values.
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

    public Sorter(String output, boolean isAscOrder, Set<File> files, TypeConverter<T> converter) throws IOException {
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
     * Gets file with min or max read value (based on args parameters),
     * writes (merges) value to output file.
     * Catches 'end of file' EOFException and than removes files from maps.
     * Catches IOException and removes file, that can't be read.
     * After no files left closes {@link BufferedWriter} writer.
     * <p>
     * throws IOException if output file can't be written.
     */
    private void merge() throws IOException {
        while (filesCount != 0) {
            File file = orderedByValue();
            writer.write(converter.fromType(lastReadValues.get(file)));

            int nextIndex = 1 + lastReadLineIndices.get(file);
            try {
                String nextValue = readNextValue(file, nextIndex);

                while (!isCorrectLineIndex(file, nextValue)) {
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
     * Checks if next read value of correct type and follows sorting order by catching
     * IllegalArgumentException.
     * If exception it thrown - skips such values.
     *
     * @param file      File that have max or min value on current iteration.
     * @param nextValue New value read from file, that needs to be valid.
     * @return true if next value is valid, otherwise false.
     */
    private boolean isCorrectLineIndex(File file, String nextValue) {
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
     * Helper method to check if next value follows correct order.
     *
     * @param nextValue last read value from file. Equals null after first line read.
     * @param lastValue last valid value written to output.
     * @return true if order is correct, otherwise false.
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

    private void remove(File file, String message) {
        lastReadValues.remove(file);
        lastReadLineIndices.remove(file);

        System.out.printf(message, file.getName());

        filesCount--;
    }

    private void mapFileData(File file, String value, int index) {
        lastReadValues.put(file, converter.toType(value));
        lastReadLineIndices.put(file, index);
    }

    /**
     * Helper method to get next file, which value should be merged to output file.
     *
     * @return file with min or max value on current iteration.
     */
    private File orderedByValue() {
        return isAscOrder
                ? Collections.min(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey()
                : Collections.max(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
