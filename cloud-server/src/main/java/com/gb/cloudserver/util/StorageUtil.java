package com.gb.cloudserver.util;

import com.gb.cloudcore.Constants;
import com.gb.cloudcore.User;
import com.gb.cloudcore.UserStorageStructure;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class StorageUtil {

    public static UserStorageStructure getUserStorageStructure(User user) throws IOException {
        List<String> pathList = new ArrayList<>();
        Path startDir = Paths.get(String.valueOf(Constants.BASE_PATH), "/" + user.getUserName());

        Files.walkFileTree(startDir,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        pathList.add(file.toString());
                        return super.visitFile(file, attrs);
                    }

                });

        return new UserStorageStructure(user, pathList);
    }

}
