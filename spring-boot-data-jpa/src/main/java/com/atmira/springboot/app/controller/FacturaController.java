package com.atmira.springboot.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atmira.springboot.app.models.entity.Cliente;
import com.atmira.springboot.app.models.entity.Factura;
import com.atmira.springboot.app.models.entity.Producto;
import com.atmira.springboot.app.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
    
    @Autowired
    private IClienteService clienteService;
    
    @GetMapping("/form/{clienteId}")
    public String crear(
        @PathVariable(value = "clienteId") Long id,
        Map<String, Object> model,
        RedirectAttributes flash){
        
        Cliente cliente = clienteService.findOne(id);
        
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        
        Factura factura = Factura.builder().cliente(cliente).build();
        
        model.put("factura", factura);
        model.put("titulo", "Crear factura");
        
        return "factura/form";
    }
    
    @GetMapping(value = "/cargar-productos({term}", produces = "application/json")
    public @ResponseBody List<Producto> cargarProductos(
        @PathVariable(value = "term") String term){
        
        return clienteService.findByNombre(term);
    }
}
