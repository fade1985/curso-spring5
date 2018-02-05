package com.udemy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.udemy.service.EjercicioService;

@Controller
@RequestMapping("/ejercicio")
public class EjercicioController {
    
    @Autowired
    EjercicioService ejercicioService;
    
    @GetMapping("/method")
    public String log(
        Model model){
        ejercicioService.log();
        model.addAttribute("cadena", "EJERCICIOOOOO");
        return "mensaje";
    }
    
    @GetMapping("/")
    public RedirectView redirect(){
        return new RedirectView("/ejercicio/method");
    }
}
