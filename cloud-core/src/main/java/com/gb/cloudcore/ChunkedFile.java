package com.gb.cloudcore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChunkedFile implements Serializable {

    private static final long serialVersionUID = 1L;
    private User user;
    private String storagePath;
    private String clientPath;
    private int starPos;
    private byte[] bytes;
    private int endPos;
    private boolean fromServer;

}
