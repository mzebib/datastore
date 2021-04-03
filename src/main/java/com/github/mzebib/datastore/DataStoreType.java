package com.github.mzebib.datastore;

/**
 * @author mzebib
 */
public enum DataStoreType {
    POSTGRES("postgres")

    ;

    private String name;

    DataStoreType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
