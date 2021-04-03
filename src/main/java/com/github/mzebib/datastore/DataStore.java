package com.github.mzebib.datastore;

import com.github.mzebib.datastore.config.DataStoreConfig;
import com.github.mzebib.datastore.exception.DataStoreException;

import java.util.List;

/**
 * @author mzebib
 */
public interface DataStore<C extends DataStoreConfig, V, D, I, Q> {

    C getConfig();

    void setConfig(C config);

    V connect()
            throws NullPointerException, IllegalArgumentException, DataStoreException;

    void disconnect()
            throws NullPointerException, IllegalArgumentException, DataStoreException;


    <E extends D> E insert(E entity)
            throws NullPointerException, IllegalArgumentException, DataStoreException;

    <E extends D> List<E> insert(List<E> entities)
            throws NullPointerException, IllegalArgumentException, DataStoreException;


    <E extends D> E update(E entity)
            throws NullPointerException, IllegalArgumentException, DataStoreException;

    <E extends D> List<E> update(List<E> entities)
            throws NullPointerException, IllegalArgumentException, DataStoreException;


    <E extends D> E lookupById(Class<E> entityType, I id)
            throws NullPointerException, IllegalArgumentException, DataStoreException;

    <E extends D> List<E> query(Class<E> entityType, Q queryCriteria)
            throws NullPointerException, IllegalArgumentException, DataStoreException;


    <E extends D> boolean delete(E entity)
            throws NullPointerException, IllegalArgumentException, DataStoreException;

    <E extends D> boolean delete(List<E> entities)
            throws NullPointerException, IllegalArgumentException, DataStoreException;

}
