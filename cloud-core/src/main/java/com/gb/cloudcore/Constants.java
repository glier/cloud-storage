package com.gb.cloudcore;

import io.netty.util.internal.SystemPropertyUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

    public static final Path BASE_PATH = Paths.get(SystemPropertyUtil.get("user.dir") + "/storage");
    public static final int SERVER_PORT = 8189;
    public static final int CHUNKED_FILE_LENGTH = 1024 * 10;

}
