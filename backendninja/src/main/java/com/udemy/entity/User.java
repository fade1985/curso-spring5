package com.udemy.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
@Entity
@Table(name = "users")

/*
 * (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/*
 * (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Instantiates a new user.
 */
@NoArgsConstructor

/**
 * Instantiates a new user.
 *
 * @param username the username
 * @param password the password
 * @param enabled the enabled
 * @param userRole the user role
 */
@AllArgsConstructor
public class User {
    
    /** The username. */
    @Id
    @Column(name = "username", unique = true, nullable = false, length = 45)
    private String username;
    
    /** The password. */
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    
    /** The enabled. */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    
    /** The user role. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> userRole = new ArrayList<UserRole>();
    
    /**
     * Instantiates a new user.
     *
     * @param username the username
     * @param password the password
     * @param userRole the user role
     */
    public User(String username, String password, List<UserRole> userRole) {
        super();
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }
    
}
