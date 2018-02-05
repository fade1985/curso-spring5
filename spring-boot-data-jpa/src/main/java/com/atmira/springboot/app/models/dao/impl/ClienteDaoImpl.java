package com.atmira.springboot.app.models.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.atmira.springboot.app.models.dao.IClienteDao;
import com.atmira.springboot.app.models.entity.Cliente;

@Repository
public class ClienteDaoImpl implements IClienteDao {
    
    @PersistenceContext
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Cliente> findAll(){
        return em.createQuery("from Cliente").getResultList();
    }
    
}
