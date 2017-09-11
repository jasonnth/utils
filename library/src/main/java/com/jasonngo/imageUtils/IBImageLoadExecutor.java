package com.jasonngo.imageUtils;

/**
 * Abstract class to re-use for image loader error handling
 */
public abstract class IBImageLoadExecutor {
    abstract public void execute();
    public void handleException(final Exception e){
    }
}
