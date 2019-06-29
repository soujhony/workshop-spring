package com.jhonystein.pedidex.repository;

import com.jhonystein.pedidex.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository 
    extends JpaRepository<Produto, Long>{
}
