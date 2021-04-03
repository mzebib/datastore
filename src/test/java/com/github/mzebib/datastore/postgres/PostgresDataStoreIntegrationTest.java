package com.github.mzebib.datastore.postgres;

import com.github.mzebib.datastore.model.AddressDAO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author mzebib
 */
public class PostgresDataStoreIntegrationTest {

    private static final String RESOURCE = "hibernate.cfg.xml";

    private static PostgresDataStore dataStore;

    @BeforeClass
    public static void setUp() {
        PostgresDataStoreConfig dataStoreConfig = new PostgresDataStoreConfig();
        dataStoreConfig.setResource(RESOURCE);

        dataStore = new PostgresDataStore(dataStoreConfig);
        dataStore.connect();
    }

    @AfterClass
    public static void tearDown() {
        dataStore.disconnect();
    }

    private AddressDAO createAddressDAO() {
        AddressDAO addressDAO = new AddressDAO();
        addressDAO.setCity("New York");
        addressDAO.setState("NY");
        addressDAO.setCountry("USA");

        return addressDAO;
    }

    @Test
    public void testInsert() {
        AddressDAO addressDAO = createAddressDAO();

        addressDAO = dataStore.insert(addressDAO);
        assertNotNull(addressDAO);
    }

    @Test
    public void testUpdate() {
        AddressDAO addressDAO = dataStore.insert(createAddressDAO());

        addressDAO.setDescription("Test");
        addressDAO = dataStore.update(addressDAO);

        assertNotNull(addressDAO);
        assertNotNull(addressDAO.getDescription());
        assertEquals("Test", addressDAO.getDescription());
    }

    @Test
    public void testLookupById() {
        AddressDAO addressDAO = dataStore.insert(createAddressDAO());

        AddressDAO result = dataStore.lookupById(AddressDAO.class, addressDAO.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(addressDAO.getId(), result.getId());
    }

    @Test
    public void testQuery() {
        dataStore.insert(createAddressDAO());

        CriteriaQuery criteriaQuery
                = dataStore.connect().createEntityManager().getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(AddressDAO.class));

        List<AddressDAO> result = dataStore.query(AddressDAO.class, criteriaQuery);

        assertNotNull(result);
        assertTrue(!result.isEmpty());
    }

    @Test
    public void testDelete() {
        AddressDAO addressDAO = dataStore.insert(createAddressDAO());

        assertTrue(dataStore.delete(addressDAO));
    }

}