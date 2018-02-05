package com.atmira.springboot.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @Value("${app.controller.titulo}")
    private String titulo;
    
    @GetMapping("/index")
    public String index(
        Model model){
        model.addAttribute("titulo", titulo);
        return "index";
    }
}
