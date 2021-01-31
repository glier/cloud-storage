package com.gb.cloudcore;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Command implements Serializable {
    private CommandEvent command;
    private User user;
    private String serverPath;
    private String clientPath;
}
