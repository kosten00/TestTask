package ru.vasilev.testtask.main;

import ru.vasilev.testtask.sorter.Sorter;
import ru.vasilev.testtask.types.IntegerConverter;
import ru.vasilev.testtask.types.StringConverter;
import ru.vasilev.testtask.validators.ArgumentsValidator;
import ru.vasilev.testtask.validators.FilesValidator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ArgumentsValidator av = new ArgumentsValidator(args);

        if (av.hasErrors()) {
            return;
        }
        FilesValidator fv = new FilesValidator(av.getFilesPaths());

        if (fv.hasNoValidFiles()) {
            return;
        }

        if ("-s".equals(av.getType())) {
            new Sorter<>(av.getOutputPath(), av.isAscOrder(), fv.getValidFiles(), new StringConverter());

            return;
        }
        new Sorter<>(av.getOutputPath(), av.isAscOrder(), fv.getValidFiles(), new IntegerConverter());
    }
}