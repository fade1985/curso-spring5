package com.atmira.springboot.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atmira.springboot.app.models.entity.Cliente;
import com.atmira.springboot.app.service.IClienteService;
import com.atmira.springboot.app.service.IUploadFileService;
import com.atmira.springboot.app.util.paginator.PageRender;

import lombok.extern.java.Log;

@Controller
@SessionAttributes("cliente")
@Log
public class ClienteController {
    
    @Autowired
    private IClienteService clienteService;
    
    @Autowired
    private IUploadFileService uploadFileService;
    
    @Secured("ROLE_USER")
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(
        @PathVariable String filename){
        
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/ver/{id}")
    public String ver(
        @PathVariable(value = "id") Long id,
        Map<String, Object> model,
        RedirectAttributes flash){
        
        // Cliente cliente = clienteService.findOne(id);
        Cliente cliente = clienteService.fetchByIdWithFacturas(id);
        
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        
        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        
        return "ver";
        
    }
    
    @GetMapping(value = { "/listar", "/" })
    public String listar(
        @RequestParam(name = "page", defaultValue = "0") int page,
        Model model,
        Authentication authentication){
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        
        if (authentication != null) {
            log.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null) {
            log.info(
                    "Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Hola usuario autenticado, tu username es: "
                            .concat(auth.getName()));
        }
        
        if (hasRole("ROLE_ADMIN")) {
            log.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            log.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }
        
        PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
        
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }
    
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/form")
    public String crear(
        Map<String, Object> model){
        
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Crear Cliente");
        return "form";
    }
    
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/form/{id}")
    public String editar(
        @PathVariable(value = "id") Long id,
        Map<String, Object> model,
        RedirectAttributes flash){
        
        Cliente cliente = null;
        if (id > 0) {
            
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
            return "redirect:/listar";
        }
        
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }
    
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/form")
    public String guardar(
        @Valid Cliente cliente,
        BindingResult result,
        Model model,
        SessionStatus status,
        RedirectAttributes flash,
        @RequestParam("file") MultipartFile foto){
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario del cliente");
            return "form";
        }
        
        if (!foto.isEmpty()) {
            
            if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
                    && cliente.getFoto().length() > 0) {
                
                uploadFileService.delete(cliente.getFoto());
            }
            
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
            cliente.setFoto(uniqueFilename);
            
        }
        
        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito";
        
        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/listar";
    }
    
    @Secured("ROLE_ADMIN")
    @GetMapping("/eliminar/{id}")
    public String eliminar(
        @PathVariable(name = "id") Long id,
        RedirectAttributes flash){
        
        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito");
            
            if (uploadFileService.delete(cliente.getFoto())) {
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito!");
            }
        }
        
        return "redirect:/listar";
        
    }
    
    private boolean hasRole(
        String role){
        
        SecurityContext context = SecurityContextHolder.getContext();
        
        if (context == null) {
            return false;
        }
        
        Authentication auth = context.getAuthentication();
        
        if (auth == null) {
            return false;
        }
        
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        
        return authorities.contains(new SimpleGrantedAuthority(role));
        
        /*
         * for (GrantedAuthority authority : authorities) { if
         * (authority.getAuthority().equals(role)) {
         * log.info("Hola usario".concat(auth.getName()).concat(" tu role es: ")
         * .concat(authority.getAuthority())); return true; } } return false;
         */
    }
}
