package com.jhonystein.pedidex.resource;

import com.jhonystein.pedidex.model.Produto;
import com.jhonystein.pedidex.service.ProdutoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("produtos")
public class ProdutoResource {
    
    @Autowired
    private ProdutoService service;
    
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE })
    public List<Produto> findAll() {
        return service.findAll();
    }
    
    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE },
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Produto insert(@RequestBody Produto produto) {
        return service.inserir(produto);
    }
}
