package com.jhonystein.pedidex.resource;

import com.jhonystein.pedidex.model.Session;
import com.jhonystein.pedidex.model.Usuario;
import com.jhonystein.pedidex.service.UsuarioService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
    @GetMapping("/foto")
    public ResponseEntity<String> getFoto() {
        String fotoBase64 = service.getFoto(getUserDetails().getUsername());
        return fotoBase64 != null ? 
                ResponseEntity.ok(fotoBase64) : 
                ResponseEntity.notFound().build();
    }
    
    @PostMapping("/foto")
    public String saveFoto(@RequestParam MultipartFile foto) throws IOException {
        return service.saveFoto(getUserDetails().getUsername(), foto.getBytes());
    }
    
    @GetMapping("/@me")
    public Session getMe() {
        return new Session(getUserDetails().getUsername(), null);
    }
    
    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
