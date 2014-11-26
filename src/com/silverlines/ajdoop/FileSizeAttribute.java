package com.silverlines.ajdoop;

import java.io.File;

public class FileSizeAttribute implements Attribute<Long>{

    @Override
    public String getAttributeName() {
	return "filesize";
    }

    @Override
    public Long getAttributeValue(File inputFile) {
	return inputFile.length();
    }

}
