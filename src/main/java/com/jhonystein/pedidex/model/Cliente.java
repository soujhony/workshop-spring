package com.jhonystein.pedidex.model;

import javax.validation.constraints.Pattern;

public class Cliente {

    private Long id;
    @Pattern(regexp = "[0-9]{11,14}")
    private String documento;
    private String nome;
    private String email;
    private String telefone;
    
}
