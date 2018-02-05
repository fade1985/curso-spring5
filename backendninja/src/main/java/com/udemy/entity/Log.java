package com.udemy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * The Class Log.
 */
@Entity
@Table(name = "log")

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
 * Instantiates a new log.
 */
@NoArgsConstructor

/**
 * Instantiates a new log.
 *
 * @param id the id
 * @param date the date
 * @param details the details
 * @param username the username
 * @param url the url
 */
@AllArgsConstructor
public class Log {
    
    /** The id. */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    /** The date. */
    @Column(name = "date")
    private Date date;
    
    /** The details. */
    @Column(name = "details")
    private String details;
    
    /** The username. */
    @Column(name = "username")
    private String username;
    
    /** The url. */
    @Column(name = "url")
    private String url;
}
