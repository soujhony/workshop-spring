package com.jhonystein.pedidex.service;

import com.jhonystein.pedidex.model.Produto;
import com.jhonystein.pedidex.repository.ProdutoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;
    
    public Produto inserir(Produto produto) {
        return repository.save(produto);
    }
    
    public Produto update (Produto produto) {
        return repository.save(produto);
    }
    
    public List<Produto> findAll() {
        return repository.findAll();
    }
    
}
