package ru.vasilev.testtask.validators;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class that validates file paths passed as command line arguments.
 * Instance generates set of valid files.
 * If file is not valid, skips such file.
 *
 * @author Vasilev K.V.
 * @version 1.0
 */
public class FilesValidator {
    String[] paths;
    Collection<File> files;

    private static final String DUPLICATION_ERROR =
            "File with name '%s' is duplicated!%n";
    private static final String FILE_ERROR =
            "File with name '%s' does not exists or is not a correct file!%n";
    private static final String ONE_FILE_WARNING =
            "Got only one correct file passed as param!";

    public FilesValidator(String[] paths) {
        this.paths = paths;
        files = new HashSet<>();

        collectValidFiles();
    }

    public boolean hasNoValidFiles() {
        return files.size() == 0;
    }

    public Collection<File> getValidFiles() {
        return files;
    }

    private void collectValidFiles() {
        for (String path : paths) {
            File file = new File(path);

            if (file.exists() && file.isFile()) {
                if (files.contains(file)) {
                    System.out.printf(DUPLICATION_ERROR, path);
                } else {
                    files.add(file);
                }
            } else {
                System.out.printf(FILE_ERROR, path);
            }
        }

        if (files.size() == 1) {
            System.out.println(ONE_FILE_WARNING);
        }
    }
}
