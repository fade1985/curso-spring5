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

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Instantiates a new contact model.
 */
@NoArgsConstructor

/**
 * Instantiates a new contact model.
 *
 * @param id the id
 * @param firstName the first name
 * @param lastName the last name
 * @param telephone the telephone
 * @param city the city
 */
@AllArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
public class ContactModel {
    
    /** The id. */
    private int id;
    
    /** The first name. */
    private String firstName;
    
    /** The last name. */
    private String lastName;
    
    /** The telephone. */
    private String telephone;
    
    /** The city. */
    private String city;
}
