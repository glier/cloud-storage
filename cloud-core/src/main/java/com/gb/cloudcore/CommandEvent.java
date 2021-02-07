package com.gb.cloudcore;

import java.io.Serializable;

public enum CommandEvent implements Serializable {
    GET_USER_DIR_STRUCTURE,
    FILE_DOWNLOAD,
    FILE_DELETE,
    FILE_RENAME,
    DIR_CREATE
}
