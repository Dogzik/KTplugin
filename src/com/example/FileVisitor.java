package com.example;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FileVisitor extends SimpleFileVisitor<Path> {
    private int[] sources = new int[2];
    private int[] KTSources = new int[2];
    public int isTest = 0;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        ++sources[isTest];
        if (file.toString().endsWith(".kt")) {
            ++KTSources[isTest];
        }
        return CONTINUE;
    }

    public int[] getSources() {
        return sources;
    }

    public int[] getKTSources() {
        return KTSources;
    }
}
