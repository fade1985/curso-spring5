package com.atmira.springboot.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atmira.springboot.app.models.entity.Cliente;
import com.atmira.springboot.app.models.entity.Factura;
import com.atmira.springboot.app.models.entity.ItemFactura;
import com.atmira.springboot.app.models.entity.Producto;
import com.atmira.springboot.app.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
@Secured("ROLE_ADMIN")
public class FacturaController {
    
    @Autowired
    private IClienteService clienteService;
    
    @GetMapping("/ver/{id}")
    public String ver(
        @PathVariable(value = "id") Long id,
        Model model,
        RedirectAttributes flash){
        
        // Factura factura = clienteService.findFacturaById(id);
        Factura factura = clienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id);
        
        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no existe en la base de datos!");
            return "redirect:/listar";
        }
        
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));
        
        return "factura/ver";
    }
    
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
        
        Factura factura = new Factura();
        factura.setCliente(cliente);
        
        model.put("factura", factura);
        model.put("titulo", "Crear factura");
        
        return "factura/form";
    }
    
    @GetMapping(value = "/cargar-productos/{term}", produces = "application/json")
    public @ResponseBody List<Producto> cargarProductos(
        @PathVariable(value = "term") String term){
        
        return clienteService.findByNombre(term);
    }
    
    @PostMapping("/form")
    public String guardarFactura(
        @RequestParam(name = "item_id[]", required = false) Long[] itemId,
        @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
        @Valid Factura factura,
        BindingResult result,
        Model model,
        RedirectAttributes flash,
        SessionStatus status){
        
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear factura");
            return "factura/form";
        }
        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error: La factura NO puede no tener líneas");
            return "factura/form";
        }
        
        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.findProductoById(itemId[i]);
            
            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);
            
        }
        
        clienteService.saveFactura(factura);
        status.setComplete();
        
        flash.addFlashAttribute("success", "Factura creada con éxito");
        
        return "redirect:/ver/" + factura.getCliente().getId();
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(
        @PathVariable(value = "id") Long id,
        RedirectAttributes flash){
        
        Factura factura = clienteService.findFacturaById(id);
        
        if (factura != null) {
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success", "Factura eliminada con éxito!");
            return "redirect:/ver/" + factura.getCliente().getId();
        }
        flash.addFlashAttribute("error", "La factura no existe en la base de datos, no se pudo eliminar");
        return "redirect:/listar";
    }
}
