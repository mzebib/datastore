package com.github.mzebib.datastore.postgres;

import com.github.mzebib.datastore.config.DataStoreConfig;

import java.io.File;
import java.net.URL;

/**
 * @author mzebib
 */
public class PostgresDataStoreConfig extends DataStoreConfig {

    private String resource;
    private File configFile;
    private URL url;

    public PostgresDataStoreConfig() {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
