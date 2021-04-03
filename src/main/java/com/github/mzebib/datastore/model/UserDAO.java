package com.github.mzebib.datastore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author mzebib
 */
@Entity
@Table(name = UserDAO.TABLE_NAME)
public class UserDAO extends BaseDAO {

    // Table name
    public static final String TABLE_NAME = "user";

    // Column names
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";

    @Column(name = COL_USERNAME, unique = true, nullable = false)
    private String username;
    @Column(name = COL_EMAIL, unique = true, nullable = false)
    private String email;
    @Column(name = COL_FIRST_NAME)
    private String firstName;
    @Column(name = COL_LAST_NAME)
    private String lastName;

    public UserDAO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDAO userDAO = (UserDAO) o;
        return Objects.equals(id, userDAO.id) &&
                Objects.equals(username, userDAO.username) &&
                Objects.equals(email, userDAO.email) &&
                Objects.equals(firstName, userDAO.firstName) &&
                Objects.equals(lastName, userDAO.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, firstName, lastName);
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
