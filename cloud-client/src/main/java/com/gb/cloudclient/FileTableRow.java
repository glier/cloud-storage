package com.gb.cloudclient;

import com.gb.cloudcore.DirElement;
import javafx.beans.property.*;


public class FileTableRow extends DirElement {
    private final StringProperty elementName;
    private final LongProperty elementSize;
    private final StringProperty lastModifiedTime;
    private final BooleanProperty isDirectory;

    public FileTableRow(SimpleStringProperty elementName, SimpleLongProperty elementSize, SimpleStringProperty lastModifiedTime, SimpleBooleanProperty isDirectory) {
        super(elementName.getValue(), elementSize.getValue(), lastModifiedTime.getValue(), isDirectory.getValue());
        this.elementName = elementName;
        this.elementSize = elementSize;
        this.lastModifiedTime = lastModifiedTime;
        this.isDirectory = isDirectory;
    }

    public StringProperty elementNameProperty() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName.set(elementName);
    }

    public LongProperty elementSizeProperty() {
        return elementSize;
    }

    public void setElementSize(long elementSize) {
        this.elementSize.set(elementSize);
    }

    public StringProperty lastModifiedTimeProperty() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime.set(lastModifiedTime);
    }

    public BooleanProperty isDirectoryProperty() {
        return isDirectory;
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory.set(isDirectory);
    }
}
