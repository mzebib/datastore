package com.github.mzebib.datastore.model;

import java.io.Serializable;

/**
 * @author mzebib
 */
public interface DAO<I> extends Serializable {

    I getId();

    void setId(I id);
}
