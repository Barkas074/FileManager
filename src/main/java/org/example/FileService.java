package org.example;

import java.io.File;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final File file;
    private final String fileName;
    private final String absolutePath;
    private final String size;
    private String date;

    public FileService(File file, String size, FileTime fileTime) {
        this.file = file;
        this.size = size;
        formatDateTime(fileTime);
        fileName = file.getName();
        absolutePath = file.getAbsolutePath();
    }

    public File getFile() {
        return file;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getFileName() {
        return fileName;
    }

    private void formatDateTime(FileTime fileTime) {
        date = fileTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DATE_FORMATTER);
    }
}
