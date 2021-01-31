package com.gb.cloudserver.util;

import com.gb.cloudcore.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageUtil {
    private static final Logger logger = LogManager.getLogger(StorageUtil.class);

    public static UserStorageStructure getUserStorageStructure(User user, String path) throws IOException {
        List<DirElement> pathList = new ArrayList<>();
        Path startDir = Paths.get(getCurrentPath(user, path));
        logger.info(startDir);

        Files.walk(Paths.get(startDir.toString()), 1, FileVisitOption.FOLLOW_LINKS)
                .forEach(f -> {
                    String name = startDir.relativize(f).toString();
                    try {
                        pathList.add(new DirElement(
                                name.equals("") ? ".." : name,
                                Files.size(f),
                                Files.getLastModifiedTime(f).toString(),
                                f.toFile().isDirectory()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        Collections.sort(pathList);

        pathList.forEach(p -> {
            System.out.println(p.getElementName() + " " + p.getIsDirectory());
        });

        return new UserStorageStructure(user, pathList);
    }

    public static void DownloadFile(ChunkedFile file) {

    }

    public static String getCurrentPath(User user, String path) {
        StringBuilder currentPath = new StringBuilder();
        currentPath
                .append(Constants.BASE_PATH)
                .append("/")
                .append(user.getUserName())
                .append(path);
        return currentPath.toString();
    }

}
