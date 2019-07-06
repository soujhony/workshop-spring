package com.jhonystein.pedidex.resource;

import com.jhonystein.pedidex.model.Session;
import com.jhonystein.pedidex.model.Usuario;
import com.jhonystein.pedidex.service.UsuarioService;
import javax.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioResource {
    
    @Autowired
    private UsuarioService service;
    
    @PostMapping(path = "/login")
    public Session login (@RequestBody Usuario usuario) {
        return service.login(usuario);
    }
 
    @PostMapping(path = "/register")
    public Session register (@RequestBody Usuario usuario) {
        return service.register(usuario);
    }
}
