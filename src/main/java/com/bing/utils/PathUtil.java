package com.bing.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
    public static String getName(String path) throws IOException {
        Path path1 = Paths.get(path);
        return path1.getFileName().toString();
    }
}
