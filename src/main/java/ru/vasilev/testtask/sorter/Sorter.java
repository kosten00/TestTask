package ru.vasilev.testtask.sorter;

import ru.vasilev.testtask.interfaces.TypeConverter;

import java.io.*;

import java.util.*;

public class Sorter<T extends Comparable<T>> {
    private final Map<File, T> lastReadValues;
    private final Map<File, Integer> lastReadLineIndices;

    private final Writer writer;
    private final TypeConverter<T> converter;
    private final boolean isAscOrder;

    private int filesCount;

    private static final String READING_ERROR =
            "File '%s' can't be read!%n";
    private static final String REACHED_EOF_WARN =
            "File '%s' reached EOF!%n";
    private static final String WRITING_ERROR =
            "Can't write to output file!";
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

        mapFiles(files);
        merge();
    }

    private void mapFiles(Set<File> files) {
        for (File file : files) {
            try {
                mapData(file, getNextLine(file, 0), 0);

                filesCount++;
            } catch (EOFException e) {
                System.out.printf(REACHED_EOF_WARN, file.getName());
            }
        }
    }

    private String getNextLine(File file, int index) throws EOFException {
        String value = null;
        try {
            value = read(file, index);
        } catch (IOException e) {
            System.out.printf(READING_ERROR, file.getName());
        }

        if (value == null) {
            throw new EOFException();
        }
        return value;
    }

    private void mapData(File file, String value, int index) {
        lastReadValues.put(file, converter.toType(value));
        lastReadLineIndices.put(file, index);
    }

    private void merge() {
        try {
            while (filesCount != 0) {
                File file = orderedByValue();

                writer.write(converter.fromType(lastReadValues.get(file)));

                try {
                    int nextIndex = lastReadLineIndices.get(file) + 1;
                    String value = getNextLine(file, nextIndex);

                    while (!isCorrectLineIndex(file, value)) {
                        nextIndex++;

                        value = getNextLine(file, nextIndex);
                    }

                    mapData(file, value, nextIndex);
                } catch (EOFException e) {
                    System.out.printf(REACHED_EOF_WARN, file.getName());

                    --filesCount;
                    remove(file);
                }

                if (filesCount != 0) {
                    writer.write(System.lineSeparator());
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(WRITING_ERROR);
        }
    }

    private boolean isCorrectLineIndex(File file, String value) {
        try {
            T lastCorrectValue = lastReadValues.get(file);
            T nextValue = converter.toType(value);

            if (hasCorrectOrder(nextValue, lastCorrectValue)) {
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

    private boolean hasCorrectOrder(T nextValue, T lastValue) {
        if (lastValue == null) {
            return true;
        }
        boolean isBigger = nextValue.compareTo(lastValue) > 0;
        boolean isEqual = nextValue.compareTo(lastValue) == 0;

        return (isAscOrder && (isBigger || isEqual))
                || (!isAscOrder && (!isBigger || isEqual));
    }

    private void remove(File file) {
        lastReadValues.remove(file);
        lastReadLineIndices.remove(file);
    }

    private String read(File file, int index) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int start = 0;

            while (start != index) {
                br.readLine();

                start++;
            }

            return br.readLine();
        }
    }

    private File orderedByValue() {
        return isAscOrder
                ? Collections.min(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey()
                : Collections.max(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
