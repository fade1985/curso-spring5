package com.udemy.repository;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udemy.entity.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserRepository.
 */
@Repository
@Qualifier("userRepository")
public interface UserRepository extends JpaRepository<User, Serializable> {
    
    /**
     * Find by username.
     *
     * @param username the username
     * @return the user
     */
    public abstract User findByUsername(
        String username);
}
