package com.atmira.springboot.app.models.dao;

import java.util.List;

import com.atmira.springboot.app.models.entity.Cliente;

public interface IClienteDao {
    
    public List<Cliente> findAll();
}
