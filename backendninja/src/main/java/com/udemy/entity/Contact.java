package com.udemy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: Auto-generated Javadoc
/**
 * The Class Contact.
 */
@Entity
@Table(name = "Contact")

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Data

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Instantiates a new contact.
 */
@NoArgsConstructor

/**
 * Instantiates a new contact.
 *
 * @param id the id
 * @param firstname the firstname
 * @param lastname the lastname
 * @param telephone the telephone
 * @param city the city
 */
@AllArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Contact {
    
    /** The id. */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    /** The firstname. */
    @Column(name = "firstname")
    private String firstname;
    
    /** The lastname. */
    @Column(name = "lastname")
    private String lastname;
    
    /** The telephone. */
    @Column(name = "telephone")
    private String telephone;
    
    /** The city. */
    @Column(name = "city")
    private String city;
    
}
