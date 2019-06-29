package com.jhonystein.pedidex.resource;

import com.jhonystein.pedidex.model.Produto;
import com.jhonystein.pedidex.service.ProdutoService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("produtos")
public class ProdutoResource {
    
    @Autowired
    private ProdutoService service;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Produto> findAll() {
        return service.findAll();
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Produto insert(@RequestBody Produto produto) {
        return service.inserir(produto);
    }
    
    @PutMapping(path = "{id}", 
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> update(@PathVariable("id") Long id, @RequestBody Produto produto) {
        if (!id.equals(produto.getId()))
            return ResponseEntity
                    .badRequest().build();
        
        return ResponseEntity.ok(service.update(produto));
    }

    @GetMapping(path = "{id}", 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Produto getOne(@PathVariable("id") Long id) {
        return service.findOne(id);
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable("id") Long id) {
        service.remove(id);
    }
    
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Map<String, List<String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, List<String>> erros = new HashMap<>();

        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String value = Optional.ofNullable(v.getInvalidValue())
                    .orElse("NULL").toString();
            erros.put(printPropertyPath(v.getPropertyPath()), Arrays.asList(value, v.getMessage()));
        }
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros);
    }
    
    private String printPropertyPath(Path path) {
        if (path == null) {
            return "UNKNOWN";
        }

        String propertyPath = "";
        Path.Node parameterNode = null;
        // Construct string representation of property path.
        // This will strip away any other nodes added by RESTEasy (method, parameter, ...).
        for (Path.Node node : path) {
            if (node.getKind() == ElementKind.PARAMETER) {
                parameterNode = node;
            }

            if (node.getKind() == ElementKind.PROPERTY) {
                if (!propertyPath.isEmpty()) {
                    propertyPath += ".";
                }
                propertyPath += node;
            }
        }

        if (propertyPath.isEmpty() && parameterNode != null) {
            // No property path constructed, assume this is a validation failure on a request parameter.
            propertyPath = parameterNode.toString();
        }
        return propertyPath;
    }
}
