package com.atmira.springboot.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import com.atmira.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
    
    private final Logger log = LoggerFactory.getLogger(ClienteController.class);
    
    @Autowired
    private IClienteService clienteService;
    
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(
        @PathVariable String filename){
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
    
    @GetMapping(value = "/ver/{id}")
    public String ver(
        @PathVariable(value = "id") Long id,
        Map<String, Object> model,
        RedirectAttributes flash){
        
        Cliente cliente = clienteService.findOne(id);
        
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        
        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        
        return "ver";
        
    }
    
    @GetMapping(value = "/listar")
    public String listar(
        @RequestParam(name = "page", defaultValue = "0") int page,
        Model model){
        
        Pageable pageRequest = new PageRequest(page, 5);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        
        PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
        
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
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
                Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
                File archivo = rootPath.toFile();
                
                if (archivo.exists() && archivo.canRead()) {
                    archivo.delete();
                }
            }
            
            String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
            Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);
            
            Path rootAbsolutePath = rootPath.toAbsolutePath();
            
            log.info("rootPath: " + rootPath.toString());
            log.info("rootAbsolutePath: " + rootAbsolutePath.toString());
            
            try {
                // byte[] bytes = foto.getBytes();
                // Path rutaCompleta = Paths.get(rootPath + File.separator +
                // foto.getOriginalFilename());
                // Files.write(rutaCompleta, bytes);
                Files.copy(foto.getInputStream(), rootAbsolutePath);
                flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
                cliente.setFoto(uniqueFilename);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito";
        
        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/listar";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(
        @PathVariable(name = "id") Long id,
        RedirectAttributes flash){
        
        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito");
            
            Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
            File archivo = rootPath.toFile();
            
            if (archivo.exists() && archivo.canRead()) {
                if (archivo.delete()) {
                    flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito!");
                }
            }
        }
        return "redirect:/listar";
        
    }
}
