package com.gb.cloudcore;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DirElement implements Serializable, Comparable<DirElement>{
    private String elementName;
    private Long elementSize;
    private String lastModifiedTime;
    private Boolean isDirectory;


    @Override
    public int compareTo(DirElement o) {
        int result = o.isDirectory.compareTo(this.isDirectory);
        return result == 0 ? this.elementName.compareTo(o.elementName) : result;
    }
}
