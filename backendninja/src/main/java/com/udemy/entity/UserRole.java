package com.udemy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * The Class UserRole.
 */
@Entity
@Table(name = "user_role", uniqueConstraints = @UniqueConstraint(columnNames = { "role", "user" }))

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
 * Instantiates a new user role.
 */
@NoArgsConstructor

/**
 * Instantiates a new user role.
 *
 * @param userRoleId the user role id
 * @param user the user
 * @param role the role
 */
@AllArgsConstructor
public class UserRole {
    
    /** The user role id. */
    @Id
    @GeneratedValue
    @Column(name = "user_role_id", unique = true, nullable = false)
    private Integer userRoleId;
    
    /** The user. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;
    
    /** The role. */
    @Column(name = "role", nullable = false, length = 45)
    private String role;
    
    /**
     * Instantiates a new user role.
     *
     * @param user the user
     * @param role the role
     */
    public UserRole(User user, String role) {
        super();
        this.user = user;
        this.role = role;
    }
}
