package com.gb.cloudserver.util;

import com.gb.cloudcore.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
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

    public static String getCurrentPath(User user, String path) {
        StringBuilder currentPath = new StringBuilder();
        currentPath
                .append(Constants.BASE_PATH)
                .append("/")
                .append(user.getUserName())
                .append(path);
        return currentPath.toString();
    }

    public static void renameDirOrFile(User user, String oldPath, String newPath) {
        Path source = Paths.get(getCurrentPath(user, oldPath));
        Path target = Paths.get(getCurrentPath(user, newPath));
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static void newDir(User user, String clientPath) {
        Path newFileOrDir = Paths.get(getCurrentPath(user, clientPath));

        try {
            Files.createDirectory(newFileOrDir);
            logger.info("CREATE DIR " + newFileOrDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
