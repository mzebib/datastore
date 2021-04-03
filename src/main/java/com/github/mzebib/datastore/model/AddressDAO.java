package com.github.mzebib.datastore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author mzebib
 */
@Entity
@Table(name = AddressDAO.TABLE_NAME)
public class AddressDAO extends BaseDAO {

    // Table name
    public static final String TABLE_NAME = "address";

    // Column names
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_STREET = "street";
    public static final String COL_CITY = "city";
    public static final String COL_STATE = "state";
    public static final String COL_ZIP_CODE = "zip_code";
    public static final String COL_COUNTRY = "country";

    @Column(name = COL_DESCRIPTION)
    private String description;
    @Column(name = COL_STREET)
    private String street;
    @Column(name = COL_CITY)
    private String city;
    @Column(name = COL_STATE)
    private String state;
    @Column(name = COL_ZIP_CODE)
    private String zipCode;
    @Column(name = COL_COUNTRY)
    private String country;

    public AddressDAO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZIPCode() {
        return zipCode;
    }

    public void setZIPCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDAO that = (AddressDAO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(street, that.street) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, street, city, state, zipCode, country);
    }

    @Override
    public String toString() {
        return "AddressDAO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}