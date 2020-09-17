package ru.vasilev.testtask.validator;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

public class FilesValidator {
    String[] filePaths;
    Set<File> files;

    private static final String DUPLICATION_ERROR =
            "File with name '%s' is duplicated!";
    private static final String FILE_ERROR =
            "File with name '%s' does not exists or is not a correct file!";
    private static final String ONE_FILE_WARNING =
            "Got only one correct file passed as param!";

    public FilesValidator(String[] filePaths) {
        this.filePaths = filePaths;
        files = new LinkedHashSet<>();

        collectValidFiles();
    }

    public boolean hasNoValidFiles() {
        return files.size() == 0;
    }

    public Set<File> getValidFiles() {
        return files;
    }

    private void collectValidFiles() {
        for (String fileName : filePaths) {
            File file = new File(fileName);

            if (file.exists() && file.isFile()) {
                if (files.contains(file)) {
                    System.out.printf(DUPLICATION_ERROR + "%n", fileName);
                } else {
                    files.add(file);
                }
            } else {
                System.out.printf(FILE_ERROR + "%n", fileName);
            }
        }
        if (files.size() == 1) {
            System.out.println(ONE_FILE_WARNING);
        }
    }
}
