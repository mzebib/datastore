package com.github.mzebib.datastore.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author mzebib
 */
@MappedSuperclass
public abstract class BaseDAO implements DAO<Long> {

    public static final String COL_ID = "id";

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = COL_ID)
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
