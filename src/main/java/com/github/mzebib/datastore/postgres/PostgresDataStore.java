package com.github.mzebib.datastore.postgres;

import com.github.mzebib.datastore.DataStore;
import com.github.mzebib.datastore.exception.DataStoreException;
import com.github.mzebib.datastore.model.DAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mzebib
 */
public class PostgresDataStore
        implements DataStore<PostgresDataStoreConfig, SessionFactory, DAO, Long, CriteriaQuery<? extends DAO>> {

    private static final Logger log = LoggerFactory.getLogger(PostgresDataStore.class);

    private PostgresDataStoreConfig dataStoreConfig;
    private SessionFactory sessionFactory;

    public PostgresDataStore() {
    }

    public PostgresDataStore(PostgresDataStoreConfig dataStoreConfig) {
        setConfig(dataStoreConfig);
    }

    @Override
    public PostgresDataStoreConfig getConfig() {
        return dataStoreConfig;
    }

    @Override
    public void setConfig(PostgresDataStoreConfig dataStoreConfig) {
        this.dataStoreConfig = dataStoreConfig;
    }

    @Override
    public SessionFactory connect()
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (sessionFactory == null) {
            synchronized (this) {
                if (sessionFactory == null) {

                    if (dataStoreConfig == null) {
                        throw new NullPointerException("Missing Postgres configuration");
                    }

                    String resource = dataStoreConfig.getResource();
                    File configFile = dataStoreConfig.getConfigFile();
                    URL url = dataStoreConfig.getUrl();

                    if (resource == null && configFile == null && url == null) {
                        throw new NullPointerException("Missing Postgres configuration (resource, file or URL)");
                    }

                    log.info("Connecting to Postgres database...");

                    Configuration configuration = new Configuration();

                    try {
                        if (resource != null) {
                            sessionFactory = configuration.configure(resource).buildSessionFactory();
                        } else if (configFile != null) {
                            sessionFactory = configuration.configure(configFile).buildSessionFactory();
                        } else {
                            sessionFactory = configuration.configure(url).buildSessionFactory();
                        }
                    } catch (PersistenceException e) {
                        log.error("Connection failed", e);
                        throw new DataStoreException("Connection failed: " + e.getMessage());
                    }

                    log.info("Connected to Postgres database");
                }
            }
        }

        return sessionFactory;
    }

    @Override
    public void disconnect()
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
                sessionFactory = null;
                log.info("Disconnected from Postgres database");
            } catch (PersistenceException e) {
                log.error("Disconnect failed", e);
                throw new DataStoreException("Disconnect failed: " + e.getMessage());
            }
        }
    }

    @Override
    public <E extends DAO> E insert(E entity)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entity == null) throw new NullPointerException("Entity is null");

        Session session = connect().openSession();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Long id = (Long) session.save(entity);
            entity.setId(id);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Insert failed", e);
            throw new DataStoreException("Insert failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return entity;
    }

    @Override
    public <E extends DAO> List<E> insert(List<E> entities)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entities == null || entities.isEmpty()) throw new NullPointerException("Missing entities");

        Session session = connect().openSession();
        Transaction transaction = null;

        List<DAO> ret = new ArrayList<>();

        try {
            transaction = session.beginTransaction();

            for (E entity : entities) {
                Long id = (Long) session.save(entity);
                entity.setId(id);
                ret.add(entity);
            }
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Insert failed", e);
            throw new DataStoreException("Insert failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return (List<E>) ret;
    }

    @Override
    public <E extends DAO> E update(E entity)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entity == null) throw new NullPointerException("Entity is null");

        Session session = connect().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Update failed", e);
            throw new DataStoreException("Update failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return entity;
    }

    @Override
    public <E extends DAO> List<E> update(List<E> entities)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entities == null || entities.isEmpty()) throw new NullPointerException("Missing entities");

        Session session = connect().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            for (E entity : entities) {
                session.update(entity);
            }
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Update failed", e);
            throw new DataStoreException("Update failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return entities;
    }

    @Override
    public <E extends DAO> E lookupById(Class<E> entityType, Long id)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entityType == null) throw new NullPointerException("Class is null");
        if (id == null) throw new NullPointerException("ID is null");

        Session session = connect().openSession();
        session.setDefaultReadOnly(true);

        E entity;

        try {
            entity = (E) session.get(entityType.getName(), id);
        } catch (PersistenceException e) {
            log.error("Lookup by ID failed", e);
            throw new DataStoreException("Lookup by ID failed failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return entity;
    }

    @Override
    public <E extends DAO> List<E> query(Class<E> entityType, CriteriaQuery<? extends DAO> queryCriteria)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entityType == null) throw new NullPointerException("Class is null");
        if (queryCriteria == null) throw new NullPointerException("Query criteria is null");

        Session session = connect().openSession();
        session.setDefaultReadOnly(true);

        List<DAO> results = null;

        try {
            Query q = session.createQuery(queryCriteria);
            q.setReadOnly(true);

            results = q.list();
        } catch (PersistenceException e) {
            log.error("Query failed", e);
            throw new DataStoreException("Query failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return (List<E>) results;
    }

    @Override
    public <E extends DAO> boolean delete(E entity)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entity == null) throw new NullPointerException("Entity is null");

        Session session = connect().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Delete failed", e);
            throw new DataStoreException("Delete failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return true;
    }

    @Override
    public <E extends DAO> boolean delete(List<E> entities)
            throws NullPointerException, IllegalArgumentException, DataStoreException {
        if (entities == null || entities.isEmpty()) throw new NullPointerException("Missing entities");

        Session session = connect().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            for (E entity : entities) {
                session.delete(entity);
            }
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Delete failed", e);
            throw new DataStoreException("Delete failed: " + e.getMessage());
        } finally {
            session.close();
        }

        return true;
    }

}
