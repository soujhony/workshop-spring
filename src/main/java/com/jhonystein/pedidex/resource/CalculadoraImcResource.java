package com.jhonystein.pedidex.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calcular")
public class CalculadoraImcResource {
    
    @GetMapping
    public String calcular(@RequestParam("peso") Double peso, 
            @RequestParam("altura") Double altura) {
        Double imc = peso /(altura * altura);
        return String.format("Seu IMC é %.2f", imc);
    }
    
}
