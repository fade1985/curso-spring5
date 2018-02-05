package com.atmira.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.atmira.springboot.app.models.dao.IClienteDao;
import com.atmira.springboot.app.models.entity.Cliente;

@Controller
public class ClienteController {
    
    @Autowired
    @Qualifier("clienteDaoJPA")
    private IClienteDao clienteDao;
    
    @GetMapping(value = "/listar")
    public String listar(
        Model model){
        
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteDao.findAll());
        return "listar";
    }
    
    @GetMapping(value = "/form")
    public String crear(
        Map<String, Object> model){
        
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }
    
    @PostMapping(value = "/form")
    public String guardar(
        @Valid Cliente cliente,
        BindingResult result){
        if (result.hasErrors()) {
            return "form";
        }
        clienteDao.save(cliente);
        return "redirect:listar";
    }
}
