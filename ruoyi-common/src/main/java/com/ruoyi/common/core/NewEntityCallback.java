package com.ruoyi.common.core;

/**
 * When a new entity object is created, if it implements
 * this interface, it will be called back.
 *
 * @author DokiYolo
 */
public interface NewEntityCallback {
    void callback();
}
