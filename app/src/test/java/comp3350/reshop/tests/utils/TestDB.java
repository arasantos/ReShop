package comp3350.reshop.tests.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import comp3350.reshop.application.Main;

public class TestDB {
    private static final File DB_SRC = new File("src/main/assets/db/RESHOP.script");

    public static File copyDB() throws IOException {
        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC, target);
        Main.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        return target;
    }
}
