package com.gb.cloudcore;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class UserStorageStructure implements Serializable {
    private User user;
    private List<String> userStorageStructure;
}
