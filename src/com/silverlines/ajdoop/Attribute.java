package com.silverlines.ajdoop;

import java.io.File;

public interface Attribute<T> {

    /**
     * Get the name of the attribute
     * @return
     */
    public String getAttributeName();
    
    /**
     * Get the value of the attribute
     * @return
     */
    public T getAttributeValue(File inputFile);
    
    

}
