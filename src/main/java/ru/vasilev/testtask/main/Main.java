package ru.vasilev.testtask.main;

import ru.vasilev.testtask.sorter.Sorter;
import ru.vasilev.testtask.typeconverters.IntegerConverter;
import ru.vasilev.testtask.typeconverters.StringConverter;
import ru.vasilev.testtask.validators.ArgumentsValidator;
import ru.vasilev.testtask.validators.FilesValidator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ArgumentsValidator av = new ArgumentsValidator(args);

        if (av.hasErrors()) {
            return;
        }
        FilesValidator fv = new FilesValidator(av.getFilesPaths());

        if (fv.hasNoValidFiles()) {
            return;
        }

        try {
            if ("-s".equals(av.getType())) {
                new Sorter<>(av.getOutputPath(), av.isAscOrder(), fv.getValidFiles(), new StringConverter());
            } else {
                new Sorter<>(av.getOutputPath(), av.isAscOrder(), fv.getValidFiles(), new IntegerConverter());
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't write to output file!", e);
        }
    }
}