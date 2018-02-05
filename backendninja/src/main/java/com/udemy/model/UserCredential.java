package com.udemy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: Auto-generated Javadoc
/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Data

/*
 * (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Instantiates a new user credential.
 */
@NoArgsConstructor

/**
 * Instantiates a new user credential.
 *
 * @param username the username
 * @param password the password
 */
@AllArgsConstructor

/*
 * (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
public class UserCredential {
    
    /** The username. */
    private String username;
    
    /** The password. */
    private String password;
}
