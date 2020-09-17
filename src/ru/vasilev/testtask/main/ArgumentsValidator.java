package ru.vasilev.testtask.main;

import java.util.Arrays;

public class ArgumentsValidator {
    private boolean hasErrors;
    private boolean isAscOrder = true;
    private String type;
    private String outputPath;
    private String[] filesPaths;

    private static final String WRONG_ARGS_COUNT_ERROR =
            "Required minimum of 3 arguments to run a program. Found: %d";
    private static final String NO_REQUIRED_ARGUMENT_ERROR =
            "Missing required argument for input data type! (-s or -i)";
    private static final String NO_FILES_PATHS_ERROR =
            "Arguments do not contain input files paths!";

    public ArgumentsValidator(String[] args) {
        validate(args);
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public boolean isAscOrder() {
        return isAscOrder;
    }

    public String getType() {
        return type;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String[] getFilesPaths() {
        return filesPaths;
    }

    private void validate(String[] args) {
        if (args.length < 3) {
            System.out.printf(WRONG_ARGS_COUNT_ERROR, args.length);

            hasErrors = true;
            return;
        }
        type = "";
        int outputPathStartIndex = 0;
        for (int i = 0; i < 2; i++) {
            if (args[i].matches("-[ad]")) {
                isAscOrder = !"-d".equals(args[i]);
                outputPathStartIndex++;

                continue;
            }
            if (args[i].matches("-[si]")) {
                outputPathStartIndex++;
                type = args[i];
            }
        }
        if (type.equals("")) {
            System.out.println(NO_REQUIRED_ARGUMENT_ERROR);

            hasErrors = true;
            return;
        }
        if (outputPathStartIndex == (args.length - 1)) {
            System.out.println(NO_FILES_PATHS_ERROR);

            hasErrors = true;
            return;
        }
        outputPath = args[outputPathStartIndex];

        filesPaths = Arrays.copyOfRange(args, outputPathStartIndex + 1, args.length);
    }
}
