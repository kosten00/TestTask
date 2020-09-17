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

    private final static String READING_ERROR =
            "File '%s' can't be read!";
    private final static String REACHED_EOF_WARN =
            "File '%s' reached EOF!%n";
    private final static String WRITING_ERROR =
            "Can't write to output file!";

    public Sorter(String output, boolean isAscOrder, Set<File> files, TypeConverter<T> converter) throws IOException {
        this.isAscOrder = isAscOrder;
        this.converter = converter;

        lastReadLineIndices = new LinkedHashMap<>();
        lastReadValues = new LinkedHashMap<>();
        writer = new PrintWriter(new FileWriter(new File(output)));

        initFiles(files);
        sort();
    }

    private void initFiles(Set<File> files) {
        for (File file : files) {
            try {
                popNextLine(file, 0);
                filesCount++;
            } catch (EOFException e) {
                System.out.printf(REACHED_EOF_WARN, file.getName());
            }
        }
    }

    private void popNextLine(File file, int index) throws EOFException {
        String value = null;
        try {
            value = read(file, index);
        } catch (IOException e) {
            System.out.printf(READING_ERROR, file.getName());
            remove(file);
        }
        if (value == null) {
            throw new EOFException();
        }
        mapData(file, converter.toType(value), ++index);
    }

    private void mapData(File file, T lastValue, int nextIndex) {
        lastReadValues.put(file, lastValue);
        lastReadLineIndices.put(file, nextIndex);
    }

    private void remove(File file) {
        lastReadValues.remove(file);
        lastReadLineIndices.remove(file);

        filesCount--;
    }

    private String read(File file, int nextIndex) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int start = 0;
            while (start != nextIndex) {
                br.readLine();

                start++;
            }
            return br.readLine();
        }
    }

    private void sort() {
        while (filesCount != 0) {
            File file = orderedByValue();

            String value = converter.fromType(lastReadValues.get(file));
            try {
                popNextLine(file, lastReadLineIndices.get(file));
            } catch (EOFException e) {
                System.out.printf(REACHED_EOF_WARN, file.getName());
                remove(file);
            }
            writeSortedValue(value);
        }
    }

    private void writeSortedValue(String value) {
        try {
            if (filesCount == 0) {
                writer.write(value);
                writer.close();
            }
            writer.write(value + System.lineSeparator());
        } catch (IOException e) {
            System.out.println(WRITING_ERROR);
        }
    }

    private File orderedByValue() {
        if (isAscOrder) {
            return Collections.min(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey();
        }
        return Collections.max(lastReadValues.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
