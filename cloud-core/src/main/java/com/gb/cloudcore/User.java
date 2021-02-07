package com.gb.cloudcore;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class User implements Serializable {
    private String userName;
    private String password;
}
