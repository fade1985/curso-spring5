package com.atmira.springboot.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atmira.springboot.app.models.dao.IClienteDao;
import com.atmira.springboot.app.models.dao.IProductoDao;
import com.atmira.springboot.app.models.entity.Cliente;
import com.atmira.springboot.app.models.entity.Producto;
import com.atmira.springboot.app.service.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {
    
    @Autowired
    private IClienteDao clienteDao;
    
    @Autowired
    private IProductoDao productoDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll(){
        return (List<Cliente>) clienteDao.findAll();
    }
    
    @Override
    @Transactional
    public void save(
        Cliente cliente){
        clienteDao.save(cliente);
        
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(
        Long id){
        return clienteDao.findOne(id);
    }
    
    @Override
    @Transactional
    public void delete(
        Long id){
        clienteDao.delete(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(
        Pageable pageable){
        return clienteDao.findAll(pageable);
    }
    
    @Override
    public List<Producto> findByNombre(
        String term){
        return productoDao.findByNombre(term);
    }
    
}
